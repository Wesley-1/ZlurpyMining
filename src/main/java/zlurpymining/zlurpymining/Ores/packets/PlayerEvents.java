package zlurpymining.zlurpymining.Ores.packets;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import zlurpymining.zlurpymining.ZlurpyMining;

public class PlayerEvents implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        ZlurpyMining.getPacketHandler().addPlayer(event.getPlayer());
        Packets.togglePlayer(event.getPlayer(), true);
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        ZlurpyMining.getPacketHandler().removePlayer(event.getPlayer());
        Packets.removePlayer(event.getPlayer());
        ZlurpyMining.getInstance().getFph().resetBlockAnimations(event.getPlayer());
    }
}
