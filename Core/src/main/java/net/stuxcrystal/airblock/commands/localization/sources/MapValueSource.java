package net.stuxcrystal.airblock.commands.localization.sources;

import lombok.NonNull;
import net.stuxcrystal.airblock.utils.Optional;
import net.stuxcrystal.airblock.commands.localization.ValueSource;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Uses maps for determining the value source.
 */
public class MapValueSource implements ValueSource {

    /**
     * The map that holds the values.
     */
    private final Map<String, Object> values = new HashMap<String, Object>();

    /**
     * Creates the value source.
     * @param values The values.
     */
    public MapValueSource(@NonNull Map<?, ?> values) {
        for (Map.Entry<?, ?> entry : values.entrySet()) {
            this.values.put(entry.getKey().toString(), entry.getValue());
        }
    }

    @Nullable
    @Override
    public Optional<?> get(@NonNull String value) {
        if (!values.containsKey(value))
            return Optional.empty();
        return Optional.from(this.values.get(value));
    }
}
