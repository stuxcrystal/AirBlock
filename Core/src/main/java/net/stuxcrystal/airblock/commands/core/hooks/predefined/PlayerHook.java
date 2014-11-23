package net.stuxcrystal.airblock.commands.core.hooks.predefined;

import lombok.*;
import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.core.hooks.Hook;

/**
 * Basic implementation of a player hook.
 */
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public abstract class PlayerHook implements Hook {

    @Getter
    @Setter
    public Executor executor;

}
