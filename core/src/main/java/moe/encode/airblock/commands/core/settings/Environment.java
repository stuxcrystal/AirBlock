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

package moe.encode.airblock.commands.core.settings;

import lombok.Data;
import lombok.NonNull;
import moe.encode.airblock.commands.Commands;
import moe.encode.airblock.commands.core.backend.BackendHandle;
import moe.encode.airblock.commands.core.components.ComponentManager;
import moe.encode.airblock.commands.core.services.ServiceManager;
import moe.encode.airblock.commands.localization.TranslationManager;
import moe.encode.airblock.commands.arguments.ArgumentConverter;
import moe.encode.airblock.commands.arguments.split.ArgumentSplitter;
import moe.encode.airblock.commands.arguments.split.SimpleSplit;
import moe.encode.airblock.commands.Backend;
import moe.encode.airblock.commands.contrib.scheduler.Scheduler;
import moe.encode.airblock.commands.contrib.scheduler.fallback.FallbackScheduler;
import moe.encode.airblock.commands.core.exceptions.ExceptionHandlerBag;
import moe.encode.airblock.commands.core.hooks.HookManager;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/**
 * Defines an environment.
 */
@Data
public class Environment implements CommandSettings {

    /**
     * Contains the argument converter.
     */
    @NonNull
    public ArgumentConverter argumentConverter = ArgumentConverter.getSimpleArgumentConverter();

    /**
     * The argument splitter.
     */
    @NonNull
    public ArgumentSplitter argumentSplitter = new SimpleSplit();

    /**
     * The manager for components.
     */
    @NonNull
    public ComponentManager componentManager = new ComponentManager();

    /**
     * Contains the translation manager.
     */
    @NonNull
    public TranslationManager translationManager = new TranslationManager();

    /**
     * Contains the hook manager.
     */
    @NonNull
    public HookManager hookManager = new HookManager(this);

    /**
     * Added service manager.
     */
    @NonNull
    public ServiceManager serviceManager = new ServiceManager();

    /**
     * The actual backend.
     */
    @NonNull
    public final Backend backend;

    /**
     * The exception handler
     */
    public ExceptionHandlerBag handler = new ExceptionHandlerBag();

    /**
     * Creates a new environment object.
     * @param backend The backend.
     */
    public Environment(BackendHandle<?, ?> backend) {
        this.backend = new Backend(backend, this);
        backend.setEnvironment(this);
    }

    @Nonnull
    @Override
    public Environment getEnvironment() {
        return this;
    }

    @Nonnull
    @Override
    public Logger getLogger() {
        return this.backend.getLogger();
    }

    /**
     * Registers a new component.
     * @param wrapper        The wrapper that should be used.
     * @param interfaceCls   The component that should be registered.
     */
    public void registerComponent(Class<?> wrapper, Object interfaceCls) {
        this.getComponentManager().register(wrapper, interfaceCls);
    }

    /**
     * Returns the scheduler of the environment.
     * @return The scheduler of the environment.
     */
    public Scheduler getScheduler() {
        if (!this.getBackend().hasComponent(Scheduler.class))
            this.registerComponent(Backend.class, new FallbackScheduler(this));
        return this.getBackend().getComponent(Scheduler.class);
    }

    @Nonnull
    @Override
    public ExceptionHandlerBag getExceptionHandlers() {
        return this.handler;
    }

    /**
     * Returns a new commands object for this environment.
     * @return A new commands object for this environment.
     */
    @Nonnull
    public Commands createCommands() {
        return new Commands(this);
    }

}
