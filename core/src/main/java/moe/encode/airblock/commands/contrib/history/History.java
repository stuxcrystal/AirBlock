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

package moe.encode.airblock.commands.contrib.history;

import java.util.List;

/**
 * Defines the history of the player.
 */
public interface History {

    /**
     * Undos the last action.
     */
    public boolean undoAction();

    /**
     * Redos the next action.
     */
    public boolean redoAction();

    /**
     * Execute this action.
     *
     * @param action The action to execute.
     */
    public void executeAction(Action action);

    /**
     * Get all actions in the history.
     * @return A list of all actions.
     */
    public List<Action> getActions();

    /**
     * The last executed action that has not been undone.
     * @return The last action.
     */
    public Action getLastAction();

    /**
     * Returns the next action that would have been executed
     * when {@link #redoAction()} is being executed.
     *
     * @return {@code null} if there is no action to redoAction.
     */
    public Action getNextAction();
}
