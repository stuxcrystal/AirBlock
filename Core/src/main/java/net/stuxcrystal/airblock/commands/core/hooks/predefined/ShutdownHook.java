package net.stuxcrystal.airblock.commands.core.hooks.predefined;

import net.stuxcrystal.airblock.commands.core.settings.Environment;

/**
 * The shutdown hook.
 */
public class ShutdownHook extends EnvironmentHook {

    public ShutdownHook(Environment environment) {
        super(environment);
    }
}
