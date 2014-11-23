package net.stuxcrystal.airblock.commands.localization.sources;

import lombok.NonNull;
import net.stuxcrystal.airblock.utils.Optional;
import net.stuxcrystal.airblock.commands.localization.ValueSource;

import javax.annotation.Nullable;

/**
 * A value source that uses prefixes.
 */
public class PrefixValueSource implements ValueSource {

    /**
     * The prefix that should be used.
     */
    private final String prefix;

    /**
     * The value source that should be used.
     */
    private final ValueSource source;

    /**
     * Creates a new value source that uses prefixes.
     * @param prefix The prefix.
     * @param source The source.
     */
    public PrefixValueSource(@NonNull String prefix, @NonNull ValueSource source) {
        this.prefix = prefix;
        this.source = source;
    }


    @Nullable
    @Override
    public Optional<?> get(@NonNull String value) {
        if (!value.startsWith(this.prefix))
            return Optional.empty();
        return this.source.get(value.substring(this.prefix.length()));
    }
}
