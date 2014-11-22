package net.stuxcrystal.airblock.commands.localization.sources;

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.core.utils.Optional;
import net.stuxcrystal.airblock.commands.localization.ValueSource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a value source that retrieves its values from other sources.
 */
public class MultiValueSource implements ValueSource {

    /**
     * The sources that are accessed by the multi-value source.
     */
    private final List<ValueSource> sources = new ArrayList<ValueSource>();

    @Nullable
    @Override
    public Optional<?> get(@NonNull String value) {
        for (ValueSource source : this.sources) {
            Optional<?> res = source.get(value);
            if (!Optional.isEmpty(res))
                return res;
        }
        return Optional.empty();
    }


    /**
     * Adds a source to the translation manager.
     * @param source The source that has been added.
     */
    public void addSource(@NonNull ValueSource source) {
        this.sources.add(source);
    }

    /**
     * The source to remove.
     * @param source The source to remove.
     */
    public void removeSource(@NonNull ValueSource source) {
        this.sources.remove(source);
    }

}
