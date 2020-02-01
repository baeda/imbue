package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.ImbueJunit5Extension;
import dk.skrypalle.imbue.Link;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ImbueJunit5Extension.class)
class LazyMultiLinkTest {

    @Link
    private Supplier<SingleDependent> lazyStringSupplier;

    @Link
    private Supplier<SingleSingleton> lazyStringHolder;

    @Link
    private Iterable<MultiLink> multiLink;

    @Link
    private Supplier<Iterable<MultiLink>> lazyMultiLink;

    @Test
    void lazy_dependent_link__is_properly_linked() {
        assertThat(lazyStringSupplier)
                .isNotNull();

        var firstStringHolder = lazyStringSupplier.get();
        var secondStringHolder = lazyStringSupplier.get();

        assertThat(firstStringHolder)
                .isNotNull()
                .isNotSameAs(secondStringHolder);
    }

    @Test
    void lazy_singleton_link__is_properly_linked() {
        assertThat(lazyStringHolder)
                .isNotNull();

        var firstStringHolder = lazyStringHolder.get();
        var secondStringHolder = lazyStringHolder.get();

        assertThat(firstStringHolder)
                .isNotNull()
                .isSameAs(secondStringHolder);
    }

    @Test
    void multiple_implementations__are_properly_linked() {
        assertThat(multiLink)
                .isNotNull();

        var resultList = StreamSupport.stream(multiLink.spliterator(), false)
                .collect(Collectors.toList());

        assertThat(resultList)
                .hasSize(2);
    }

    @Test
    void multiple_implementations__are_properly_scoped() {
        assertThat(multiLink)
                .isNotNull();

        var pass1 = StreamSupport.stream(multiLink.spliterator(), false)
                .collect(Collectors.toList());
        var pass2 = StreamSupport.stream(multiLink.spliterator(), false)
                .collect(Collectors.toList());

        var passes = new ArrayList<>(pass1);
        passes.addAll(pass2);

        var dependentImpls = passes.stream()
                .filter(a -> a instanceof DependentMultiLinkImpl)
                .collect(Collectors.toList());
        var singletonImpls = passes.stream()
                .filter(a -> a instanceof SingletonMultiLinkImpl)
                .collect(Collectors.toList());

        assertThat(dependentImpls)
                .hasSize(2)
                .allMatch(Objects::nonNull);
        assertThat(dependentImpls.get(0))
                .isNotSameAs(dependentImpls.get(1));

        assertThat(singletonImpls)
                .hasSize(2)
                .allMatch(Objects::nonNull);
        assertThat(singletonImpls.get(0))
                .isSameAs(singletonImpls.get(1));
    }

    @Test
    void lazy_multiple_implementations__are_properly_linked() {
        assertThat(lazyMultiLink)
                .isNotNull();

        var multiLink = lazyMultiLink.get();
        assertThat(multiLink)
                .isNotNull();

        var resultList = StreamSupport.stream(multiLink.spliterator(), false)
                .collect(Collectors.toList());

        assertThat(resultList)
                .hasSize(2);
    }

}
