package zlurpymining.zlurpymining.Ores.Listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import zlurpymining.zlurpymining.OresHandler;
import zlurpymining.zlurpymining.ZlurpyMining;


public class BlockBreak  implements Listener  {


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String mix = event.getBlock().getWorld().getName() + "," + event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ();
        if (OresHandler.getLocToOre().containsKey(mix) && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
            event.getBlock().getState().update(true);
        }
        if(OresHandler.getLocToOre().containsKey(mix) && event.getPlayer().getGameMode() == GameMode.CREATIVE){
            String m = event.getBlock().getWorld().getName() + "," + event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ();
            OresHandler.removeOre(event.getBlock().getLocation(),m);
        }
    }
}
