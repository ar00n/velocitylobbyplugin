package nl.geslotenkingdom.geslotenlobby.velocitylobbyplugin;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class HubCommand implements Command {

    private final ProxyServer server;


    public HubCommand(ProxyServer server) {
        this.server = server;
    }


    @Override
    public void execute(@NonNull CommandSource source, String[] args) {
        Player player = (Player) source;
        String serverName = VelocityLobbyPlugin.servername;
        Optional <RegisteredServer> toConnect = server.getServer(serverName);
        if (!toConnect.isPresent()) {
            player.sendMessage(
                    TextComponent.of("De server " + serverName + " bestaat niet.", TextColor.RED));
            return;
        }
        player.createConnectionRequest(toConnect.get()).fireAndForget();
    }
}
