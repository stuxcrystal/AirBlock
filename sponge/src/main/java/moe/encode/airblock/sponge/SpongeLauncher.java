package moe.encode.airblock.sponge;

import com.google.inject.Inject;
import moe.encode.airblock.configuration.storage.storage.ConfigurationStorage;
import moe.encode.airblock.configuration.storage.storage.multi.MultiStorage;
import moe.encode.airblock.BackendEntryPoint;
import moe.encode.airblock.Bootstrapper;
import moe.encode.airblock.EntryPoint;
import moe.encode.airblock.configuration.parser.files.yaml.YamlGenerator;
import moe.encode.airblock.configuration.storage.storage.simple.LocalStorage;
import moe.encode.airblock.configuration.storage.storage.simple.ResourceStreamStorage;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.nio.file.Path;

/**
 * Base-Class for AirBlock-Based Plugins.<p />
 *
 * Subclass this class as your main plugin class.
 */
public class SpongeLauncher implements BackendEntryPoint {

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    private final String name;

    public SpongeLauncher(String name) {
        this.name = name;
    }

    @Listener(order=Order.FIRST)
    public void initAirBlock(GameInitializationEvent event) {
        // Create a bridged handler.
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("net.stuxcrystal.airblock." + this.name);
        logger.setUseParentHandlers(false);
        logger.addHandler(new SpongeLoggingBridgeHandler(this.logger));

        SpongeServerBackend backend = new SpongeServerBackend(this, logger, this.name);
        Bootstrapper.begin(this, backend);
    }

    @Listener(order=Order.FIRST)
    public void startAirBlock(GameAboutToStartServerEvent event) {
        Bootstrapper.start(this);
    }

    @Listener(order=Order.EARLY)
    public void loginAirBlock(ClientConnectionEvent.Join event) {
        Bootstrapper.login(this, event.getTargetEntity());
    }

    @Listener(order=Order.LATE)
    public void logoffAirBlock(ClientConnectionEvent.Disconnect event) {
        Bootstrapper.logoff(this, event.getTargetEntity());
    }

    @Listener(order=Order.LAST)
    public void stopAirBlock(GameStoppingEvent event) {
        Bootstrapper.end(this);
    }

    public void init(EntryPoint entryPoint) {

    }

    public void begin(EntryPoint entryPoint) {

    }

    public void reloading(EntryPoint entryPoint) {

    }

    public void reloaded(EntryPoint entryPoint) {

    }

    public void end(EntryPoint entryPoint) {

    }

    public void deinit(EntryPoint entryPoint) {

    }

    public ConfigurationStorage getBaseConfigurationStorage() {
        return new MultiStorage(
                new LocalStorage(this.defaultConfig.toFile(), ".yml", new YamlGenerator()),

                new ConfigurationStorage[] {
                        // May not work as expected.
                        new ResourceStreamStorage(this.getClass().getClassLoader(), "/", ".yml", new YamlGenerator())
                }
        );
    }
}
