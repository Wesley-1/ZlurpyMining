package zlurpymining.zlurpymining.Ores.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zlurpymining.zlurpymining.Ores.CustomEvents.MiningEvent;
import zlurpymining.zlurpymining.OresHandler;
import zlurpymining.zlurpymining.Ores.objects.Ore;
import zlurpymining.zlurpymining.Ores.objects.Regen;
import zlurpymining.zlurpymining.ZlurpyMining;

import java.util.ArrayList;

/*
© Copyright TranspireDev 2021-2021. All Rights Reserved

This class and all its contents belongs to, and solely to Zlurpy and PlasmaMan916.
The copying of this class is prohibited.
 */

public class MiningListener implements Listener {
    ZlurpyMining main = ZlurpyMining.getPlugin(ZlurpyMining.class);

    @EventHandler
    public void onTMining(MiningEvent event) {
        Player player = event.getPlayer();
        String mix = event.getBlock().getWorld().getName() + "," + event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ();
        if (OresHandler.getLocToOre().containsKey(mix)) {
            Ore ore = OresHandler.getLocToOre().get(mix);
            if (ore == null) {
                player.sendMessage(toColor(main.getConfig().getString("messages.ore-null")));
            } else {
                if (player.getGameMode().equals(GameMode.CREATIVE)) {
                    event.getBlock().setType(Material.AIR);
                    OresHandler.getLocToOre().remove(mix);
                    OresHandler.getOreLocs().get(ore.getName()).remove(mix);
                    player.sendMessage(toColor(main.getConfig().getString("messages.ore-removed")));
                } else {
                    event.getBlock().setType(Material.BEDROCK);
                    ArrayList<String> commands = ore.pickCommands();
                    for(String s : commands){
                        s = s.replaceAll("%player%",event.getPlayer().getName());
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),s);
                        System.out.println(s);
                        }
                    }
                    Regen regen = new Regen(event.getBlock(), ore, System.currentTimeMillis(), System.currentTimeMillis() + ore.getRegenerationTime()*1000);
                    main.getCurrentRegens().put(regen, ore);
                }
            }
        }

    public static String toColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
