package net.stuxcrystal.airblock.bukkit;

import net.stuxcrystal.airblock.commands.contrib.Permissions;
import net.stuxcrystal.airblock.commands.core.backend.ExecutorHandle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Backend for Bukkit Command Executors.
 */
public class BukkitExecutorBackend extends ExecutorHandle<CommandSender> implements Permissions {

    /**
     * The UUID that is for the console.
     */
    private static final UUID CONSOLE_UUID = UUID.randomUUID();

    /**
     * The basic handle of the executor.
     *
     * @param handle The handle for the executor.
     */
    public BukkitExecutorBackend(CommandSender handle) {
        super(handle);
    }

    @Override
    public String getName() {
        return this.getHandle().getName();
    }

    @Override
    public void sendMessage(String message) {
        this.getHandle().sendMessage(message);
    }

    @Override
    public boolean isAdmin() {
        return this.getHandle().isOp();
    }

    @Override
    public boolean isConsole() {
        return !(this.getHandle() instanceof Player);
    }

    @Override
    public InetSocketAddress getAddress() {
        if (!this.isConsole()) {
            return null;
        }

        Player p = (Player)this.getHandle();
        return p.getAddress();
    }

    @Nullable
    @Override
    public UUID getUniqueIdentifier() {
        if (this.isConsole())
            return CONSOLE_UUID;
        return ((Player)this.getHandle()).getUniqueId();
    }

    public boolean hasPermission(String node) {
        return this.getHandle().hasPermission(node);
    }
}
