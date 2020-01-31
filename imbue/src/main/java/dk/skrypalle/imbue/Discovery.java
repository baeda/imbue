package dk.skrypalle.imbue;

import dk.skrypalle.imbue.Logger.Level;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;

import static dk.skrypalle.imbue.FileUtils.listFiles;
import static org.apache.commons.lang3.StringUtils.endsWith;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.replaceChars;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

final class Discovery {

    private static final Logger log = LoggerFactory.getLogger();

    private static final String JAVA_CLASS_PATH = "java.class.path";
    private static final List<Class<?>> ALL_CLASSES = TimingUtil.time(
            "class-discovery",
            Discovery::discoverClasses,
            Level.INFO
    );

    private static final Map<Class<?>, Object> implMap = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Object> leafMap = new ConcurrentHashMap<>();

    private static List<Class<?>> getAllClasses() {
        return ALL_CLASSES;
    }

    static <T> List<Class<T>> getImplementationsOf(Class<T> type) {
        @SuppressWarnings("unchecked")
        var result = (List<Class<T>>) implMap.computeIfAbsent(
                type,
                Discovery::discoverImplementationsOf
        );
        return result;
    }

    static <T> List<Class<T>> getLeafClassesOf(Class<T> type) {
        @SuppressWarnings("unchecked")
        var result = (List<Class<T>>) leafMap.computeIfAbsent(
                type,
                Discovery::discoverLeafClassesOf
        );
        return result;
    }

    static List<Class<?>> discoverImplementationsOf(Class<?> type) {
        var result = new ArrayList<Class<?>>();

        for (var clazz : getAllClasses()) {
            if (ReflectionUtils.isAssignableFrom(type, clazz)) {
                System.out.println();
            }

            if (ReflectionUtils.isAssignableFrom(clazz, type)) {
                result.add(clazz);
            }
        }
        return result;
    }

    static List<Class<?>> discoverLeafClassesOf(Class<?> type) {
        var implementations = getImplementationsOf(type);

        var result = new ArrayList<Class<?>>();
        for (var matchingClass : implementations) {
            var modifiers = matchingClass.getModifiers();
            if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
                continue;
            }


            result.add(matchingClass);
        }

        return result;
    }

    private static List<Class<?>> discoverClasses() {
        String javaClassPath = System.getProperty(JAVA_CLASS_PATH);
        if (isBlank(javaClassPath)) {
            return Collections.emptyList();
        }

        var result = new ArrayList<Class<?>>();
        for (String path : javaClassPath.split(File.pathSeparator)) {
            discoverClassesInPath(new File(path), result);
        }

        return Collections.unmodifiableList(result);
    }

    private static void discoverClassesInPath(File path, Collection<? super Class<?>> out) {
        if (path == null) {
            return;
        }

        if (path.isDirectory()) {
            discoverClassesInDirectory(path, out);
        } else {
            discoverClassesInJarFile(path, out);
        }
    }

    private static void discoverClassesInDirectory(File path, Collection<? super Class<?>> out) {
        if (path == null || !path.isDirectory()) {
            return;
        }

        for (var file : listFiles(path, Discovery::endsWithJar, false)) {
            discoverClassesInJarFile(file, out);
        }

        int absoluteBasePathEnd = path.getAbsolutePath().length() + 1;
        for (var file : listFiles(path, Discovery::endsWithClass, true)) {
            var fileName = file.getAbsolutePath().substring(absoluteBasePathEnd);
            var className = convertFileNameToClassName(fileName);
            safeDiscoverClass(className, out);
        }
    }

    private static void discoverClassesInJarFile(File path, Collection<? super Class<?>> out) {
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
            log.error(e, "unable to open %s as jar-file", path);
        }
    }

    private static String convertFileNameToClassName(String fileName) {
        var withoutExtension = substringBeforeLast(fileName, ".");
        return replaceChars(withoutExtension, "/\\", "..");
    }

    private static void safeDiscoverClass(String className, Collection<? super Class<?>> out) {
        try {

            out.add(Class.forName(className));

        } catch (ClassNotFoundException e) {
            log.debug("class '%s' not found.", className);
        } catch (LinkageError e) {
            log.debug(
                    "error while discovering class '%s': %s",
                    className,
                    e.getClass().getCanonicalName()
            );
        }
    }

    private static boolean endsWithJar(File file) {
        return endsWithJar(file.getName());
    }

    private static boolean endsWithJar(String string) {
        return endsWith(string, ".jar");
    }

    private static boolean endsWithClass(File file) {
        return endsWithClass(file.getName());
    }

    private static boolean endsWithClass(String string) {
        return endsWith(string, ".class");
    }

    private Discovery() { /* static utility */ }

}
