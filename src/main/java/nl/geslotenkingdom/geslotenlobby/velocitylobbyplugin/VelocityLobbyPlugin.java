package nl.geslotenkingdom.geslotenlobby.velocitylobbyplugin;


import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "velocitylobbyplugin", name = "VelocityLobbyPlugin", version = "1.0",
        description = "A plugin that send a player to the lobby or hub", authors = {"TCOOfficiall"})

public class VelocityLobbyPlugin {

    public static String servername;



    private Toml loadConfig(Path path) {
        File folder = path.toFile();
        File file = new File(folder, "config.toml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try (InputStream input = getClass().getResourceAsStream("/" + file.getName())) {
                if (input != null) {
                    Files.copy(input, file.toPath());
                } else {
                    file.createNewFile();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                return null;
            }
        }

        return new Toml().read(file);
    }


    @Inject
    private VelocityLobbyPlugin(ProxyServer s, CommandManager commandManager, Logger logger, @DataDirectory final Path folder) {
        Toml toml = loadConfig(folder);
        if (toml == null) {
            logger.warn("Failed to load config.toml. Shutting down.");
            return;
        }

        servername = toml.getString("lobby-server");

        commandManager.register("hub", new HubCommand(s));
        commandManager.register("lobby", new HubCommand(s));
        logger.info("Plugin has enabled!");
    }
}
