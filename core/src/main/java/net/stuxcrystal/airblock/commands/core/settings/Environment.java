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

package net.stuxcrystal.airblock.commands.core.settings;

import lombok.Data;
import lombok.NonNull;
import net.stuxcrystal.airblock.commands.arguments.ArgumentConverter;
import net.stuxcrystal.airblock.commands.arguments.split.ArgumentSplitter;
import net.stuxcrystal.airblock.commands.arguments.split.SimpleSplit;
import net.stuxcrystal.airblock.commands.Backend;
import net.stuxcrystal.airblock.commands.core.backend.BackendHandle;
import net.stuxcrystal.airblock.commands.contrib.scheduler.Scheduler;
import net.stuxcrystal.airblock.commands.contrib.scheduler.fallback.FallbackScheduler;
import net.stuxcrystal.airblock.commands.core.components.ComponentManager;
import net.stuxcrystal.airblock.commands.core.hooks.HookManager;
import net.stuxcrystal.airblock.commands.localization.TranslationManager;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/**
 * Defines an environment.
 */
@Data
public class Environment implements CommandSettings {

    /**
     * Contains the instance of this singleton.
     */
    private static Environment INSTANCE;

    /**
     * Returns the instance of the environment.
     * @return The instance that has been created.
     */
    public static Environment getInstance() {
        return Environment.INSTANCE;
    }

    /**
     * Sets the instance of the environment.
     * @param environment The instance of the environment.
     */
    public static void setInstance(Environment environment) {
        Environment.INSTANCE = environment;
    }

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
     * The actual backend.
     */
    @NonNull
    public final Backend backend;

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
}
