/*
 * AirBlock - Framework for Multi-Platform Minecraft-Plugins.
 * Copyright (C) 2014 stux!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.stuxcrystal.airblock.canary;

import net.canarymod.Canary;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.player.DisconnectionHook;
import net.canarymod.plugin.Plugin;
import net.canarymod.plugin.PluginListener;
import net.stuxcrystal.airblock.BackendEntryPoint;
import net.stuxcrystal.airblock.Bootstrapper;
import net.stuxcrystal.airblock.EntryPoint;
import net.stuxcrystal.airblock.configuration.parser.files.yaml.YamlGenerator;
import net.stuxcrystal.airblock.configuration.storage.storage.ConfigurationStorage;
import net.stuxcrystal.airblock.configuration.storage.storage.multi.MultiStorage;
import net.stuxcrystal.airblock.configuration.storage.storage.simple.ResourceStreamStorage;

/**
 * The launcher for the air-block framework.
 */
public class CanaryLauncher extends Plugin implements PluginListener, BackendEntryPoint {

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Empty Methods to be overriden when needen.

    /**
     * Called when the environment has been bootstrapped but before the main
     * EntryPoint has been initialized.
     *
     * @param entryPoint The main entrypoint.
     */
    @Override
    public void init(EntryPoint entryPoint) {

    }

    /**
     * Called after the EntryPoint has been initialized.
     *
     * @param entryPoint The main entrypoint.
     */
    @Override
    public void begin(EntryPoint entryPoint) {

    }

    /**
     * Called before the entry-point is being reloaded.
     *
     * @param entryPoint The main entrypoint.
     */
    @Override
    public void reloading(EntryPoint entryPoint) {

    }

    /**
     * Called after the entry-point has been reloaded.
     *
     * @param entryPoint The main entrypoint.
     */
    @Override
    public void reloaded(EntryPoint entryPoint) {

    }

    /**
     * Called before the EntryPoint is being disabled.
     *
     * @param entryPoint Before the entrypoint is being disabled.
     */
    @Override
    public void end(EntryPoint entryPoint) {

    }

    /**
     * Called before the entry-point is being disabled.
     *
     * @param entryPoint Before the entry-point is being disabled.
     */
    @Override
    public void deinit(EntryPoint entryPoint) {

    }

    /**
     * <p>The configuration-storage that is needed for preparing the
     * ConfigurationLoader.</p>
     * <p/>
     * <p>Access the actual configuration-loader using {@link net.stuxcrystal.airblock.Bootstrapper#getConfigurationLoader(net.stuxcrystal.airblock.BackendEntryPoint)}</p>
     *
     * @return The configuration loader.
     */
    @Override
    public ConfigurationStorage getBaseConfigurationStorage() {
        // We will allow autoloading from the resource-directory.
        return new MultiStorage(
                new CanaryConfigurationStorage(new YamlGenerator(), this),
                new ConfigurationStorage[]{
                    new ResourceStreamStorage(this.getClass().getClassLoader(), "/", ".yml", new YamlGenerator())
                }
        );
    }
}
