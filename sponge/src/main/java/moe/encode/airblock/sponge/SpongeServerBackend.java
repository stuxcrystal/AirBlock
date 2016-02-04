package moe.encode.airblock.sponge;

import moe.encode.airblock.commands.contrib.scheduler.Task;
import moe.encode.airblock.commands.core.CommandImplementation;
import moe.encode.airblock.commands.core.backend.ExecutorHandle;
import moe.encode.airblock.commands.core.backend.MinecraftVersion;
import moe.encode.airblock.commands.contrib.scheduler.Scheduler;
import moe.encode.airblock.commands.core.backend.BackendHandle;
import moe.encode.airblock.commands.core.settings.Environment;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Sponge Server Support.
 */
public class SpongeServerBackend extends BackendHandle<Object, CommandSource> implements Scheduler {

    /**
     * Actual logger.
     */
    private Logger logger;

    /**
     * The name of the plugin.
     */
    private String name;

    public SpongeServerBackend(Object plugin, Logger logger, String name) {
        super(plugin);
    }

    @Override
    public ExecutorHandle<CommandSource> getConsole() {
        return new SpongeCommandSender(Sponge.getServer().getConsole());
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public ExecutorHandle<CommandSource>[] getExecutors() {
        Collection<Player> players = Sponge.getServer().getOnlinePlayers();
        ExecutorHandle[] result = new ExecutorHandle[players.size()];

        int i = 0;
        for (Player p : players) {
            result[i++] = new SpongeCommandSender(p);
        }
        return result;
    }

    @Override
    public void runLater(Runnable runnable) {
        org.spongepowered.api.scheduler.Task.Builder builder = Sponge.getScheduler().createTaskBuilder();
        builder.execute(runnable);
        builder.submit(this.getHandle());
    }

    @Override
    public <R> R callInMainThread(Callable<R> callable) throws Throwable {
        // Prevent deadlock.
        if (Environment.getInstance().getBackend().isInMainThread()) {
            return callable.call();
        }

        FutureTask<R> fut = new FutureTask<R>(callable);
        this.runLater(fut);
        return fut.get();
    }

    @Override
    public ExecutorHandle<CommandSource> wrap(CommandSource handle) {
        return new SpongeCommandSender(handle);
    }

    @Override
    public void registerCommand(String name, CommandImplementation implementation) {
        Sponge.getCommandManager().register(this.getHandle(), new SpongeCommand(this, implementation, name), name);
    }

    @Override
    public MinecraftVersion getVersion() {
        return MinecraftVersion.fromString("1.8.0");
    }

    @Override
    public String getName() {
        return name;
    }

    public Task scheduleTask(Runnable runnable, int delay, int repeat, boolean async) {
        org.spongepowered.api.scheduler.Task.Builder builder = Sponge.getScheduler().createTaskBuilder();
        SpongeTaskWrapper.SpongeTaskRunner runner = new SpongeTaskWrapper.SpongeTaskRunner(runnable, repeat!=0);
        builder.execute(runnable);

        if (async)
            builder.async();

        if (delay != 0)
            builder.delay(delay, TimeUnit.MILLISECONDS);

        if (repeat != 0)
            builder.interval(repeat, TimeUnit.MILLISECONDS);

        SpongeTaskWrapper result = new SpongeTaskWrapper(builder.submit(this.getHandle()));
        runner.setTask(result);
        return result;
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
}
