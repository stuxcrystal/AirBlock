package net.stuxcrystal.airblock.commands.localization.sources;

import lombok.NonNull;
import net.stuxcrystal.airblock.utils.Optional;
import net.stuxcrystal.airblock.commands.localization.ValueSource;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implements a value source that uses an array (or list) as
 * its source.
 */
public class ArrayValueSource implements ValueSource {

    /**
     * The holder of the values.
     */
    private final List<Object> values;

    /**
     * Creates a new value source that uses a list as its backend.
     * @param values The values.
     */
    public ArrayValueSource(@NonNull List<Object> values) {
        this.values = new ArrayList<Object>(values);
    }

    /**
     * Creates a new value source that uses an array as its backend.
     * @param values The values.
     */
    public ArrayValueSource(Object... values) {
        this(Arrays.asList(values));
    }

    @Nullable
    @Override
    public Optional<?> get(@NonNull String value) {
        if (!StringUtils.isNumeric(value))
            return Optional.empty();

        int index = Integer.valueOf(value);
        if (index < 0 || index >= this.values.size())
            return Optional.empty();
        return Optional.from(this.values.get(index));
    }
}
