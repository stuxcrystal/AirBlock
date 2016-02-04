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

package moe.encode.airblock.canary;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import moe.encode.airblock.commands.core.backend.ExecutorHandle;
import moe.encode.airblock.commands.contrib.Permissions;
import moe.encode.airblock.commands.contrib.locales.LocaleResolver;
import moe.encode.airblock.commands.core.components.Component;

import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.UUID;

/**
 * The basic executor backend.
 */
public class CanaryExecutorBackend extends ExecutorHandle<MessageReceiver>
    implements Permissions, LocaleResolver {

    /**
     * The UUID that is for the console.
     */
    private static final UUID CONSOLE_UUID = UUID.randomUUID();

    /**
     * The basic handle of the executor.
     *
     * @param handle The handle for the executor.
     */
    public CanaryExecutorBackend(MessageReceiver handle) {
        super(handle);
    }

    @Override
    public String getName() {

        return this.isConsole()?null:this.getHandle().getName();
    }

    @Override
    public void sendMessage(String message) {
        this.getHandle().message(message);
    }

    @Override
    public boolean isAdmin() {
        return this.isConsole() || ((Player) this.getHandle()).isAdmin();
    }

    @Override
    public boolean isConsole() {
        return !(this.getHandle() instanceof Player);
    }

    @Override
    public InetSocketAddress getAddress() {
        if (this.isConsole())
            return null;
        Player player = (Player) this.getHandle();
        try {
            return new InetSocketAddress(InetAddress.getByName(player.getIP()), 0);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Component(thread = Component.ExecutionThread.MAIN_THREAD)
    public boolean hasPermission(String node) {
        return this.getHandle().safeHasPermission(node);
    }

    @Override
    @Component(thread = Component.ExecutionThread.MAIN_THREAD)
    public Locale getLocale() {
        if (this.isConsole())
            return Locale.getDefault();
        return Locale.forLanguageTag(((Player)this.getHandle()).getLocale());
    }

    @Nullable
    @Override
    public UUID getUniqueIdentifier() {
        if (this.isConsole())
            return CanaryExecutorBackend.CONSOLE_UUID;
        return ((Player)this.getHandle()).getUUID();
    }
}
