package net.stuxcrystal.airblock.commands.core.hooks.predefined;

import net.stuxcrystal.airblock.commands.core.settings.Environment;

/**
 * Happens when the environment is reloading.
 */
public class ReloadHook extends EnvironmentHook {

    public ReloadHook(Environment environment) {
        super(environment);
    }
}
