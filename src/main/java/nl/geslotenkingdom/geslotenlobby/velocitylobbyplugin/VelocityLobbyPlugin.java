package nl.geslotenkingdom.geslotenlobby.velocitylobbyplugin;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.velocitypowered.api.command.CommandManager;
import lombok.Getter;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(id = "velocitylobbyplugin", name = "VelocityLobbyPlugin", version = "1.0",
        description = "A plugin that send a player to the lobby or hub", authors = {"TCOOfficiall"})

public class VelocityLobbyPlugin {
    @Inject
    @Getter
    @DataDirectory
    private Path configPath;
    private Toml toml;
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


    @Inject private VelocityLobbyPlugin(ProxyServer s, CommandManager commandManager, Logger logger) {
        this.toml = loadConfig(configPath);
        if (toml == null) {
            logger.warn("Failed to load config.toml. Shutting down.");
            return;
        }

        servername = toml.getString("lobby-server");

        commandManager.register(new HubCommand(s), "hub");
        commandManager.register(new HubCommand(s), "lobby");
        logger.info("Plugin has enabled!");
    }
}
