package dk.skrypalle.imbue;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static dk.skrypalle.imbue.FileUtils.listFiles;
import static dk.skrypalle.imbue.ReflectionUtils.getAllMembers;
import static dk.skrypalle.imbue.ReflectionUtils.isAssignableFrom;
import static dk.skrypalle.imbue.ReflectionUtils.isStatic;
import static org.apache.commons.lang3.StringUtils.endsWith;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.replaceChars;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

/**
 * TODO JAVADOC.
 */
public class ReflectiveDiscovery implements Discovery {

    private static final Logger log = LoggerFactory.getLogger();

    private static final String JAVA_CLASS_PATH = "java.class.path";

    private final List<Class<?>> allClasses;
    private final List<Class<? extends Annotation>> allScopes;
    private final Map<Type, Object> implMap;
    private final Map<Type, Object> leafMap;

    /**
     * TODO JAVADOC.
     */
    public ReflectiveDiscovery() {
        allClasses = TimingUtils.time(
                classes -> String.format("class-discovery of %d classes", classes.size()),
                this::discoverClasses
        );
        allScopes = TimingUtils.time(
                scopes -> String.format("scope-discovery of %d scopes", scopes.size()),
                this::discoverScopes
        );
        implMap = new ConcurrentHashMap<>();
        leafMap = new ConcurrentHashMap<>();
    }

    @Override
    public List<Class<?>> getAllClasses() {
        return allClasses;
    }

    @Override
    public List<Class<? extends Annotation>> getAllScopes() {
        return allScopes;
    }

    @Override
    public <T> List<Class<T>> getLeafClassesOf(Type type) {
        @SuppressWarnings("unchecked")
        var result = (List<Class<T>>) leafMap.computeIfAbsent(
                type,
                this::discoverLeafClassesOf
        );
        return result;
    }

    @Override
    public <T> List<LinkProviderInfo<T>> getProvidersFor(Type type) {
        var result = new ArrayList<LinkProviderInfo<T>>();
        for (var clazz : getAllClasses()) {
            getAllMembers(clazz, Class::getDeclaredMethods).stream()
                    .filter(m -> m.isAnnotationPresent(LinkProvider.class))
                    .filter(m -> m.getGenericReturnType().equals(type))
                    .map(this::<T>toProviderInfo)
                    .forEach(result::add);

            getAllMembers(clazz, Class::getDeclaredFields).stream()
                    .filter(f -> f.isAnnotationPresent(LinkProvider.class))
                    .filter(f -> f.getGenericType().equals(type))
                    .map(this::<T>toProviderInfo)
                    .forEach(result::add);
        }

        return List.copyOf(result);
    }

    @Override
    public <T> List<Class<T>> getImplementationsOf(Type type) {
        @SuppressWarnings("unchecked")
        var result = (List<Class<T>>) implMap.computeIfAbsent(
                type,
                this::discoverImplementationsOf
        );
        return result;
    }

    private List<Class<?>> discoverLeafClassesOf(Type type) {
        var implementations = getImplementationsOf(type);

        var result = new HashSet<Class<?>>();
        for (var matchingClass : implementations) {
            var modifiers = matchingClass.getModifiers();
            if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
                continue;
            }


            result.add(matchingClass);
        }

        return List.copyOf(result);
    }

    private List<Class<?>> discoverImplementationsOf(Type type) {
        var result = new HashSet<Class<?>>();
        for (var clazz : getAllClasses()) {
            if (type instanceof Class<?> && isAssignableFrom(clazz, (Class<?>) type)) {
                result.add(clazz);
            } else if (type instanceof ParameterizedType) {
                var parameterizedType = (ParameterizedType) type;
                var rawClasses = getImplementationsOf(parameterizedType.getRawType());
                for (var rawClass : rawClasses) {
                    if (parameterizedType.equals(rawClass.getGenericSuperclass())) {
                        result.add(rawClass);
                    }
                    for (var genericInterface : rawClass.getGenericInterfaces()) {
                        if (parameterizedType.equals(genericInterface)) {
                            result.add(rawClass);
                        }
                    }
                }
            }
        }
        return List.copyOf(result);
    }

    @SuppressWarnings("unchecked")
    private <T> LinkProviderInfo<T> toProviderInfo(Method method) {
        method.setAccessible(true);
        return new LinkProviderInfo<>(
                (Class<T>) method.getReturnType(),
                isStatic(method) ? null : method.getDeclaringClass(),
                method.getGenericParameterTypes(),
                method.getAnnotation(LinkProvider.class).value(),
                (target, args) -> (T) method.invoke(target, args)
        );
    }

    @SuppressWarnings("unchecked")
    private <T> LinkProviderInfo<T> toProviderInfo(Field field) {
        field.setAccessible(true);
        return new LinkProviderInfo<>(
                (Class<T>) field.getType(),
                isStatic(field) ? null : field.getDeclaringClass(),
                new Type[0],
                field.getDeclaredAnnotation(LinkProvider.class).value(),
                (target, args) -> (T) field.get(target)
        );
    }

    //region class discovery

    private List<Class<?>> discoverClasses() {
        String javaClassPath = System.getProperty(JAVA_CLASS_PATH);
        if (isBlank(javaClassPath)) {
            return List.of();
        }

        var result = new ArrayList<Class<?>>();
        for (String path : javaClassPath.split(File.pathSeparator)) {
            discoverClassesInPath(new File(path), result);
        }

        return List.copyOf(result);
    }

    private void discoverClassesInPath(File path, Collection<? super Class<?>> out) {
        if (path == null) {
            return;
        }

        if (path.isDirectory()) {
            discoverClassesInDirectory(path, out);
        } else {
            discoverClassesInJarFile(path, out);
        }
    }

    private void discoverClassesInDirectory(File path, Collection<? super Class<?>> out) {
        if (path == null || !path.isDirectory()) {
            return;
        }

        for (var file : listFiles(path, this::endsWithJar, false)) {
            discoverClassesInJarFile(file, out);
        }

        int absoluteBasePathEnd = path.getAbsolutePath().length() + 1;
        for (var file : listFiles(path, this::endsWithClass, true)) {
            var fileName = file.getAbsolutePath().substring(absoluteBasePathEnd);
            var className = convertFileNameToClassName(fileName);
            safeDiscoverClass(className, out);
        }
    }

    private void discoverClassesInJarFile(File path, Collection<? super Class<?>> out) {
        if (path == null || !path.canRead()) {
            return;
        }

        try (var jar = new JarFile(path)) {

            var entries = jar.entries();
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                var entryName = entry.getName();
                if (!endsWithClass(entryName)) {
                    continue;
                }
                var className = convertFileNameToClassName(entryName);
                safeDiscoverClass(className, out);
            }

        } catch (IOException e) {
            var message = String.format("unable to open %s as jar-file", path);
            log.error(message, e);
        }
    }

    private String convertFileNameToClassName(String fileName) {
        var withoutExtension = substringBeforeLast(fileName, ".");
        return replaceChars(withoutExtension, "/\\", "..");
    }

    private void safeDiscoverClass(String className, Collection<? super Class<?>> out) {
        try {

            out.add(Class.forName(className));

        } catch (LinkageError | ClassNotFoundException e) {
            // ignore
        }
    }

    private boolean endsWithJar(File file) {
        return endsWith(file.getName(), ".jar");
    }

    private boolean endsWithClass(File file) {
        return endsWithClass(file.getName());
    }

    private boolean endsWithClass(String string) {
        return endsWith(string, ".class");
    }

    //endregion class discovery

    //region scope discovery

    private List<Class<? extends Annotation>> discoverScopes() {
        @SuppressWarnings("unchecked")
        List<Class<? extends Annotation>> result = getAllClasses().stream()
                .filter(v -> isAssignableFrom(v, Annotation.class))
                .filter(v -> v.isAnnotationPresent(Scope.class))
                .map(v -> (Class<? extends Annotation>) v)
                .collect(Collectors.toUnmodifiableList());
        return result;
    }

    //endregion scope discovery

}
