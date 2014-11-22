package net.stuxcrystal.airblock.commands.contrib.history;

import net.stuxcrystal.airblock.commands.backend.ExecutorHandle;
import net.stuxcrystal.airblock.commands.core.components.Component;
import net.stuxcrystal.airblock.commands.core.components.Components;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

import java.util.List;

/**
 * Defines the history component.
 */
@Components(History.class)
public class HistoryComponent {

    /**
     * Contains the environment under which the history-component operates.
     */
    private final Environment environment;

    /**
     * Creates a new history-component holder.
     * @param environment The environment.
     */
    public HistoryComponent(Environment environment) {
        this.environment = environment;
    }

    /**
     * Undoes the last action.
     * @param executor The last action that has been undone.
     * @return {@code true} if the action has been undone.
     */
    @Component
    public boolean undoAction(ExecutorHandle executor) {
        HistoryContainer container = executor.wrap(this.environment).getSessions().getSession(HistoryContainer.class);
        return container.undo();
    }

    /**
     * Redoes the next action.
     * @param executor The executor which action should be done.
     * @return {@code true} if the action has been redone.
     */
    @Component
    public boolean redoAction(ExecutorHandle executor) {
        HistoryContainer container = executor.wrap(this.environment).getSessions().getSession(HistoryContainer.class);
        return container.redo();
    }

    /**
     * Executes the action
     *
     * @param executor The executor which executes the action.
     * @param action   The action to execute.
     */
    @Component
    public void executeAction(ExecutorHandle executor, Action action) {
        HistoryContainer container = executor.wrap(this.environment).getSessions().getSession(HistoryContainer.class);
        container.execute(action);
    }

    /**
     * Lists all actions.
     *
     * @param executor The executor to query.
     * @return A list of all actions.
     */
    @Component
    public List<Action> getActions(ExecutorHandle executor) {
        HistoryContainer container = executor.wrap(this.environment).getSessions().getSession(HistoryContainer.class);
        return container.getActions();
    }

    /**
     * The last executed action that has not been undone.
     *
     * @param executor The executor to query.
     * @return The last action.
     */
    @Component
    public Action getLastAction(ExecutorHandle executor) {
        HistoryContainer container = executor.wrap(this.environment).getSessions().getSession(HistoryContainer.class);
        return container.getLastAction();
    }

    /**
     * Returns the next action that would have been executed
     * when {@link #redoAction(net.stuxcrystal.airblock.commands.backend.ExecutorHandle)} is being executed.
     *
     * @return {@code null} if there is no action to redoAction.
     */
    @Component
    public Action getNextAction(ExecutorHandle executor) {
        HistoryContainer container = executor.wrap(this.environment).getSessions().getSession(HistoryContainer.class);
        return container.getNextAction();
    }

}
