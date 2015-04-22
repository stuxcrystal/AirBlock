package net.stuxcrystal.airblock.commands.core.backend;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Contains the Minecraft-Version
 */
@Getter
@EqualsAndHashCode
public class MinecraftVersion implements Comparable<MinecraftVersion> {

    /**
     * <p>The Major Minecraft Version.</p>
     */
    private final int major;

    /**
     * The minor version.
     */
    private final int minor;

    /**
     * <p>The sub version.</p>
     * <p>
     *     Please note that the subversion -1 denotes that we do not care about the subversion.
     * </p>
     */
    private final int sub;

    /**
     * Creates a new minecraft version instance.
     * @param major The major version.
     * @param minor The minor version.
     * @param sub   The subversion.
     */
    public MinecraftVersion(int major, int minor, int sub) {
        this.major = major;
        this.minor = minor;
        this.sub = sub;
    }

    /**
     * Creates a new minecraft version instance.
     * @param major The major version.
     * @param minor The minor version.
     */
    public MinecraftVersion(int major, int minor) {
        this(major, minor, -1);
    }

    /**
     * Converts a version string.
     * @param version The version.
     * @return The parsed minecraft version.
     */
    public static MinecraftVersion fromString(String version) throws NumberFormatException, IllegalArgumentException {
        String[] parts = version.split("\\.");
        if (parts.length < 2 || parts.length > 3) {
            throw new IllegalArgumentException("Invalid argument");
        }

        int subversion = -1;
        if (parts.length == 3) {
            subversion = Integer.parseInt(parts[2]);
        }
        return new MinecraftVersion(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), subversion);
    }

    public String toString() {
        return "" + this.major + "." + this.minor + "." + this.sub;
    }

    @Override
    public int compareTo(MinecraftVersion o) {
        if (this.major != o.major) {
            return ((Integer) this.major).compareTo(o.major);
        }

        if (this.minor != o.minor) {
            return ((Integer) this.minor).compareTo(o.minor);
        }

        if (!(this.sub == 0 || o.sub == 0)) {
            return ((Integer) this.sub).compareTo(o.sub);
        }
        return 0;
    }
}
