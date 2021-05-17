package zlurpymining.zlurpymining.Ores.Listeners;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import zlurpymining.zlurpymining.OresHandler;
import zlurpymining.zlurpymining.Ores.Util;
import zlurpymining.zlurpymining.Ores.objects.Ore;
import zlurpymining.zlurpymining.Ores.tasks.RegenTask;
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