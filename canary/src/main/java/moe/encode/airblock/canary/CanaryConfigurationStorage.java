package moe.encode.airblock.canary;

import moe.encode.airblock.configuration.parser.files.FileType;
import moe.encode.airblock.configuration.storage.location.ConfigurationLocation;
import moe.encode.airblock.configuration.storage.location.FileLocation;
import moe.encode.airblock.configuration.storage.storage.SingleStringSeparatedLocationStorage;
import net.canarymod.plugin.Plugin;

import java.io.File;

/**
 * The configuration storage that is a bridge to CanaryMod.
 */
public class CanaryConfigurationStorage extends SingleStringSeparatedLocationStorage {

    /**
     * The default file-type.
     */
    private final FileType ft;

    /**
     * The plugin whose configuration-files we will be using.
     */
    private Plugin plugin;

    /**
     * The separator character.
     */
    protected CanaryConfigurationStorage(FileType ft, Plugin plugin) {
        super(".", "");
        this.ft = ft;
        this.plugin = plugin;
    }

    /**
     * Returns the File-Type of the given configuration file.
     *
     * @param location   The location
     * @return The file type.
     */
    @Override
    public FileType getType(String location) {
        return this.ft;
    }

    /**
     * Stores the location of the configuration
     *
     * @param location   The location
     * @return The given configuration.
     */
    @Override
    public ConfigurationLocation getConfiguration(String location) {
        return new FileLocation(new File(this.plugin.getModuleConfig(location).getFilePath()));
    }
}
