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
