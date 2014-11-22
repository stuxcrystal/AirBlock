package net.stuxcrystal.airblock.commands.core.hooks.predefined;

import lombok.*;
import net.stuxcrystal.airblock.commands.core.hooks.Hook;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

/**
 * The environment that is affected.
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public abstract class EnvironmentHook implements Hook {

    /**
     * The environment that is affected.
     */
    @Getter
    @NonNull
    public final Environment environment;

}
