package nl.geslotenkingdom.geslotenlobby.velocitylobbyplugin;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

public class HubCommand implements RawCommand {

    private final ProxyServer server;

    public HubCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        Player player = (Player) invocation.source();
        String serverName = VelocityLobbyPlugin.servername;
        Optional <RegisteredServer> toConnect = server.getServer(serverName);
        if (!toConnect.isPresent()) {
            player.sendMessage(
                    Component.text("De server " + serverName + " bestaat niet.", NamedTextColor.RED));
            return;
        }
        player.createConnectionRequest(toConnect.get()).fireAndForget();
    }
}
