package net.stuxcrystal.airblock.commands.core.list;

import lombok.NonNull;

import javax.annotation.concurrent.Immutable;

/**
 * Subclasses will implement ways to register new commands.
 */
@Immutable
public interface CommandRegistrar {

    /**
     * Registers all commands for the command registrar.
     * @param object The that should register new objects.
     */
    public void register(@NonNull CommandList list, @NonNull Object object);

}
