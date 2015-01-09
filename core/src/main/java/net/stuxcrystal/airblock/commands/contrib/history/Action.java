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

package net.stuxcrystal.airblock.commands.contrib.history;


import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.core.backend.ExecutorHandle;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

/**
 * Defines an action that can be undone and redone.
 */
public abstract class Action {

    /**
     * The executor of the action.
     */
    private final ExecutorHandle owner;

    /**
     * Contains the environment instance.
     */
    private final Environment environment;

    /**
     * Initializes the action.
     * @param owner The owner of the action.
     */
    protected Action(ExecutorHandle owner, Environment environment) {
        this.owner = owner;
        this.environment = environment;
    }

    /**
     * The executor of the action.
     * @return The executor of the action.
     */
    public Executor getOwner() {
        if (this.owner.isConsole())
            return this.environment.getBackend().getConsole();
        return this.environment.getBackend().getExecutorExact(this.owner.getName());
    }

    /**
     * Called on the first execution of the action.<p />
     *
     * Default: Calls redoAction.
     */
    public void firstExecution() {
        this.redo();
    }

    /**
     * Undo the action.
     */
    public abstract void undo();

    /**
     * Redo the action.
     */
    public abstract void redo();

    /**
     * Returns the description for an action.
     * @param executor The executor that requests the description
     * @return The description.
     */
    public abstract String getDescription(Executor executor);

    /**
     * Returns the description of the action.
     * @return The description.
     */
    public String getDescription() {
        return this.getDescription(this.environment.getBackend().getConsole());
    }

}
