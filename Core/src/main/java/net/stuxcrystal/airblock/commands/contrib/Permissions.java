package net.stuxcrystal.airblock.commands.contrib;

/**
 * <p>Interface for permissions.</p>
 * <p>
 *     Primarily designed for Handle-Implementations so they can provide their
 *     implementations of a permissions interface.
 * </p>
 */
public interface Permissions {

    /**
     * Checks if the given user has the permission needed to perform the method.
     * @param node  The node to check.
     * @return {@code true} if the executor has the permission needed.
     */
    public boolean hasPermission(String node);

}
