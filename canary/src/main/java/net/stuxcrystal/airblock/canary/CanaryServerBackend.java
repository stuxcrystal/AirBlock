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

import com.googlecode.miyamoto.AnnotationProxyBuilder;
import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.CanaryCommand;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.plugin.Plugin;
import net.canarymod.tasks.ServerTask;
import net.stuxcrystal.airblock.commands.core.backend.BackendHandle;
import net.stuxcrystal.airblock.commands.core.backend.ExecutorHandle;
import net.stuxcrystal.airblock.commands.core.CommandImplementation;
import net.stuxcrystal.airblock.commands.core.backend.MinecraftVersion;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The basic backend of the canary server
 */
public class CanaryServerBackend extends BackendHandle<Plugin, MessageReceiver> {

    /**
     * Contains the thread-id of the main thread.
     */
    private final long mTID;

    /**
     * Contains a bridge to the logger of Canary-Mod.
     */
    private final Logger logger;

    /**
     * Creates the new backendhandle.
     *
     * @param handle The backend handle.
     */
    public CanaryServerBackend(Plugin handle) {
        super(handle);
        this.mTID = Thread.currentThread().getId();
        this.logger = JULToLog4jBridge.bridge(handle.getLogman());
    }

    @Override
    public ExecutorHandle<MessageReceiver> getConsole() {
        return new CanaryExecutorBackend(Canary.getServer());
    }

    @Override
    public Logger getLogger() {
        return this.logger;
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

    @Override
    public void registerCommand(final String name, final CommandImplementation implementation) {
        // Prep
        AnnotationProxyBuilder<Command> command = AnnotationProxyBuilder.newBuilder(Command.class);
        command.setProperty("aliases", new String[] {name});
        command.setProperty("permissions", new String[0]);
        command.setProperty("description",
                implementation.getDescription(this.getConsole().wrap(this.getEnvironment()), name)
        );
        command.setProperty("tooltip",
                implementation.getDescription(this.getConsole().wrap(this.getEnvironment()), name)
        );
        command.setProperty("version", 2);

        // Start
        try {
            // Register the command.
            Canary.commands().registerCommand(
                    new CanaryCommand(command.getProxedAnnotation(), this.getHandle(), null) {
                        @Override
                        protected void execute(MessageReceiver caller, String[] parameters) {
                            implementation.execute(
                                    new CanaryExecutorBackend(caller).wrap(CanaryServerBackend.this.getEnvironment()),
                                    name,
                                    StringUtils.join(parameters, " ")
                            );
                        }
                    },
                    this.getHandle(),
                    false
            );
        } catch (CommandDependencyException e) {
            this.getLogger().log(Level.WARNING, "Failed to register command", e);
        }
    }

    @Override
    public MinecraftVersion getVersion() {
        return MinecraftVersion.fromString(Canary.getImplementationVersion().split("-")[0]);
    }

    @Override
    public String getName() {
        return this.getHandle().getName();
    }
}
