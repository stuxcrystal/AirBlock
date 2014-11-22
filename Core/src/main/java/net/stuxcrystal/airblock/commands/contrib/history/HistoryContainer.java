package net.stuxcrystal.airblock.commands.contrib.history;

import net.stuxcrystal.airblock.commands.contrib.sessions.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines a session that can undoAction and redoAction things.
 */
public class HistoryContainer extends Session {

    /**
     * Stores an action.
     * (Note: It uses the garbage collector to make sure no object is not collected.)
     */
    private class HistoryAction {

        /**
         * Reference to the action.
         */
        final Action action;

        /**
         * The previous action.
         */
        HistoryAction previous = null;

        /**
         * The next action.
         */
        HistoryAction next = null;

        /**
         * Creates a new link.
         * @param action The new action.
         */
        public HistoryAction(Action action) {
            this.action = action;
        }

    }

    /**
     * The first object in the history.
     */
    final private HistoryAction baseAction = new HistoryAction(null);

    /**
     * The current action.
     */
    private HistoryAction currentAction = baseAction;

    /**
     * The lock used to synchronize action accesses.
     */
    private final Object lock = new Object();

    /**
     * Make sure we can create a history container instance.
     */
    public HistoryContainer() {}

    /**
     * Executes the next action.<p />
     *
     * The action will be executed automatically.
     *
     * @param action The action to execute.
     */
    public void execute(Action action) {
        Objects.nonNull(action);
        this.updateAccessTime();

        synchronized (this.lock) {
            HistoryAction newAction = new HistoryAction(action);
            currentAction.next = newAction;
            newAction.previous = currentAction;
            currentAction = newAction;
        }
        action.redo();
    }

    /**
     * Redo the next action.
     * @return false if there was no action to redoAction, true otherwise.
     */
    public boolean redo() {
        this.updateAccessTime();

        HistoryAction nextAction;
        synchronized (this.lock) {
            if (currentAction.next == null)
                return false;
            nextAction = currentAction.next;
            currentAction = nextAction;
        }
        nextAction.action.redo();
        return true;
    }

    /**
     * Undo the last action.
     * @return true if there was no action to undoAction, true otherwise.
     */
    public boolean undo() {
        this.updateAccessTime();

        HistoryAction lastAction;
        synchronized (this.lock) {
            lastAction = currentAction;
            if (lastAction.action == null)
                return false;
            currentAction = lastAction.previous;
        }
        lastAction.action.undo();
        return true;
    }

    /**
     * Get all actions in the history.
     * @return A list of all actions.
     */
    public List<Action> getActions() {
        List<Action> result = new ArrayList<Action>();
        HistoryAction current = this.baseAction;
        do {
            if (current.action != null)
                result.add(current.action);
            current = current.next;
        } while (current.next != null);

        return result;
    }

    /**
     * The last executed action that has not been undone.
     * @return The last action.
     */
    public Action getLastAction() {
        return this.currentAction.action;
    }

    /**
     * Returns the next action that would have been executed
     * when {@link #redo()} is being executed.
     *
     * @return {@code null} if there is no action to redoAction.
     */
    public Action getNextAction() {
        if (this.currentAction.next == null)
            return null;
        return this.currentAction.next.action;
    }

}
