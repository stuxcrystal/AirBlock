package net.stuxcrystal.airblock.sponge;

import net.stuxcrystal.airblock.commands.contrib.Permissions;
import net.stuxcrystal.airblock.commands.contrib.locales.LocaleResolver;
import net.stuxcrystal.airblock.commands.core.backend.ExecutorHandle;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.UUID;

/**
 *
 */
public class SpongeCommandSender extends ExecutorHandle<CommandSource> implements Permissions, LocaleResolver {

    private static final UUID CONSOLE_UUID = UUID.randomUUID();

    /**
     * The basic handle of the executor.
     *
     * @param handle The handle for the executor.
     */
    public SpongeCommandSender(CommandSource handle) {
        super(handle);
    }

    @Override
    public String getName() {
        return this.getHandle().getName();
    }

    @Override
    public void sendMessage(String message) {
        this.getHandle().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
    }

    @Override
    public boolean isAdmin() {
        if (this.isConsole())
            return true;

        return ((Player) this.getHandle()).hasPermission("airblock.fakeop");
    }

    @Override
    public boolean isConsole() {
        return !(this.getHandle() instanceof Player);
    }

    @Override
    public InetSocketAddress getAddress() {
        if (this.isConsole())
            return null;

        Player p = (Player) this.getHandle();
        return p.getConnection().getAddress();
    }

    @Nullable
    @Override
    public UUID getUniqueIdentifier() {
        if (this.isConsole())
            return SpongeCommandSender.CONSOLE_UUID;

        return ((Player)this.getHandle()).getUniqueId();
    }

    public boolean hasPermission(String node) {
        return this.getHandle().hasPermission(node);
    }

    public Locale getLocale() {
        return this.getHandle().getLocale();
    }
}
