package net.stuxcrystal.airblock.canary;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.stuxcrystal.airblock.commands.backend.ExecutorHandle;
import net.stuxcrystal.airblock.commands.contrib.Permissions;
import net.stuxcrystal.airblock.commands.contrib.locales.LocaleResolver;
import net.stuxcrystal.airblock.commands.core.components.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * The basic executor backend.
 */
public class CanaryExecutorBackend extends ExecutorHandle<MessageReceiver>
    implements Permissions, LocaleResolver {

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
}
