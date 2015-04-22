package net.stuxcrystal.airblock.bukkit;

import net.stuxcrystal.airblock.commands.contrib.scheduler.Scheduler;
import net.stuxcrystal.airblock.commands.contrib.scheduler.Task;
import net.stuxcrystal.airblock.commands.contrib.threads.ThreadManager;
import net.stuxcrystal.airblock.commands.core.CommandImplementation;
import net.stuxcrystal.airblock.commands.core.backend.BackendHandle;
import net.stuxcrystal.airblock.commands.core.backend.ExecutorHandle;
import net.stuxcrystal.airblock.commands.core.backend.MinecraftVersion;
import net.stuxcrystal.airblock.commands.core.settings.Environment;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * Implementation of Bukkit.
 */
public class BukkitServerBackend extends BackendHandle<Plugin, CommandSender> implements Scheduler, ThreadManager {

    /**
     * Creates the new backendhandle.
     *
     * @param handle The backend handle.
     */
    public BukkitServerBackend(Plugin handle) {
        super(handle);
    }

    @Override
    public ExecutorHandle<CommandSender> getConsole() {
        return null;
    }

    @Override
    public Logger getLogger() {
        return this.getHandle().getLogger();
    }

    @Override
    public ExecutorHandle<CommandSender>[] getExecutors() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        BukkitExecutorBackend[] executorHandles = new BukkitExecutorBackend[players.size()];
        int index = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            executorHandles[index++] = this.wrap(player);
        }
        return executorHandles;
    }

    @Override
    public void runLater(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTask(this.getHandle(), runnable);
    }

    @Override
    public <R> R callInMainThread(Callable<R> callable) throws Throwable {
        if (Bukkit.isPrimaryThread()) {
            return callable.call();
        } else {
            return Bukkit.getScheduler().callSyncMethod(this.getHandle(), callable).get();
        }
    }

    @Override
    public BukkitExecutorBackend wrap(CommandSender handle) {
        return new BukkitExecutorBackend(handle);
    }

    @Override
    public void registerCommand(String name, final CommandImplementation implementation) {
        Bukkit.getPluginCommand(name).setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                implementation.execute(
                        BukkitServerBackend.this.wrap(sender).wrap(Environment.getInstance()),
                        label,
                        StringUtils.join(args, ' ')
                );
                return true;
            }
        });
    }

    @Override
    public MinecraftVersion getVersion() {
        return MinecraftVersion.fromString(Bukkit.getVersion().split(":")[1].split("\\)")[0]);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Task scheduleTask(Runnable runnable, int delay, int repeat, boolean async) {
        // Convert values.
        repeat = repeat/20;
        delay = delay/20;

        BukkitScheduler scheduler = Bukkit.getScheduler();
        BukkitTaskWrapper.BukkitTaskRunner runner = new BukkitTaskWrapper.BukkitTaskRunner(runnable, repeat!=0);
        BukkitTaskWrapper wrapper = null;

        if (async) {
            if (repeat == 0) {
                wrapper = new BukkitTaskWrapper(scheduler.runTaskLaterAsynchronously(this.getHandle(), runner, delay));
            } else {
                wrapper = new BukkitTaskWrapper(
                        scheduler.runTaskTimerAsynchronously(this.getHandle(), runner, delay, repeat)
                );
            }
        } else {
            if (repeat == 0) {
                wrapper = new BukkitTaskWrapper(scheduler.runTaskLater(this.getHandle(), runnable, delay));
            } else {
                wrapper = new BukkitTaskWrapper(scheduler.runTaskTimer(this.getHandle(), runnable, delay, repeat));
            }
        }

        runner.setTask(wrapper);
        return wrapper;
    }

    public Task scheduleTask(Runnable runnable, int delay) {
        return this.scheduleTask(runnable, delay, 0, false);
    }

    public Task scheduleTaskAsynchronously(Runnable runnable, int delay) {
        return this.scheduleTask(runnable, delay, 0, true);
    }

    public Task scheduleRepetitiveTask(Runnable runnable, int delay, int period) {
        return this.scheduleTask(runnable, delay, period, false);
    }

    public Task scheduleRepetitiveAsynchronousTask(Runnable runnable, int delay, int period) {
        return this.scheduleTask(runnable, delay, period, true);
    }

    public Task scheduleRepetitiveTask(Runnable runnable, int period) {
        return this.scheduleTask(runnable, 0, period, false);
    }

    public Task scheduleRepetitiveAsynchronousTask(Runnable runnable, int period) {
        return this.scheduleTask(runnable, 0, period, true);
    }

    public Task runTask(Runnable runnable) {
        return this.scheduleTask(runnable, 0, 0, false);
    }

    public Task runTaskAsync(Runnable runnable) {
        return this.scheduleTask(runnable, 0, 0, true);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void runAsynchronously(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getHandle(), runnable);
    }

    public boolean isInMainThread() {
        return Bukkit.isPrimaryThread();
    }
}
