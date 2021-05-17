package zlurpymining.zlurpymining.Ores.Listeners;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import zlurpymining.zlurpymining.OresHandler;
import zlurpymining.zlurpymining.Ores.Objects.Ore;
import zlurpymining.zlurpymining.ZlurpyMining;

public class BlockPlace implements Listener {


    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();
        NBTItem nbtItem = new NBTItem(item);

        if (!nbtItem.hasKey("type"))
            return;
        String type = nbtItem.getString("type");

        Ore ore = OresHandler.getOreTypes().get(type);
        String mix = event.getBlock().getWorld().getName() + "," + event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ();
        Location oreLoc = event.getBlock().getLocation();
        event.getBlock().setType(ore.getIndentifier());
        ZlurpyMining.getInstance().getAllRegenLocs().put(mix + ":" + ore.getName(), ore);
        OresHandler.addOre(oreLoc, ore);
    }
}