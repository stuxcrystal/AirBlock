package net.stuxcrystal.airblock.canary;

import net.canarymod.Canary;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.player.DisconnectionHook;
import net.canarymod.plugin.Plugin;
import net.canarymod.plugin.PluginListener;
import net.stuxcrystal.airblock.Bootstrapper;

/**
 * The launcher for the air-block framework.
 */
public class CanaryLauncher extends Plugin implements PluginListener {

    @Override
    public boolean enable() {
        CanaryServerBackend csb = new CanaryServerBackend(this);
        Bootstrapper.begin(this, csb);
        Canary.hooks().registerListener(this, this);
        return true;
    }

    @Override
    public void disable() {
        Bootstrapper.end(this);
    }

    @HookHandler
    public void passLogin(ConnectionHook hook) {
        Bootstrapper.login(this, hook.getPlayer());
    }

    @HookHandler
    public void passLogoff(DisconnectionHook hook) {
        Bootstrapper.logoff(this, hook.getPlayer());
    }
}
