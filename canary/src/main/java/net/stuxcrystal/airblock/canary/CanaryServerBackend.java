package net.stuxcrystal.airblock.canary;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.plugin.Plugin;
import net.canarymod.tasks.ServerTask;
import net.stuxcrystal.airblock.commands.backend.BackendHandle;
import net.stuxcrystal.airblock.commands.backend.ExecutorHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

/**
 * The basic backend of the canary server
 */
public class CanaryServerBackend extends BackendHandle<Plugin, MessageReceiver> {

    /**
     * Contains the thread-id of the main thread.
     */
    private long mTID = -1;

    /**
     * Creates the new backendhandle.
     *
     * @param handle The backend handle.
     */
    public CanaryServerBackend(Plugin handle) {
        super(handle);
        this.mTID = Thread.currentThread().getId();
    }

    @Override
    public ExecutorHandle<MessageReceiver> getConsole() {
        return new CanaryExecutorBackend(Canary.getServer());
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public ExecutorHandle<MessageReceiver>[] getExecutors() {
        List<CanaryExecutorBackend> result = new ArrayList<CanaryExecutorBackend>();
        for (Player player : Canary.getServer().getPlayerList()) {
            result.add(new CanaryExecutorBackend(player));
        }
        return result.toArray(new CanaryExecutorBackend[result.size()]);
    }

    @Override
    public void runLater(final Runnable runnable) {
        Canary.getServer().addSynchronousTask(new ServerTask(this.getHandle(), 0) {
            @Override
            public void run() {
                runnable.run();
            }
        });
    }

    @Override
    public <R> R callInMainThread(Callable<R> callable) throws Throwable {
        if (this.mTID == -1)
            throw new IllegalStateException("We do not know the main-thread id yet.");

        if (Thread.currentThread().getId() == this.mTID) {
            return callable.call();
        }

        FutureTask<R> ft = new FutureTask<R>(callable);
        this.runLater(ft);
        return ft.get();
    }

    @Override
    public ExecutorHandle<MessageReceiver> wrap(MessageReceiver handle) {
        return new CanaryExecutorBackend(handle);
    }
}
