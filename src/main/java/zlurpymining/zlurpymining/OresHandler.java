package zlurpymining.zlurpymining;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import zlurpymining.zlurpymining.Ores.Files.OresYml;
import zlurpymining.zlurpymining.Ores.Objects.Ore;
import zlurpymining.zlurpymining.Ores.Objects.Regen;
import zlurpymining.zlurpymining.Ores.Packets.Packets;
import zlurpymining.zlurpymining.Ores.Tasks.RegenTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
© Copyright TranspireDev 2021-2021. All Rights Reserved

This class and all its contents belongs to, and solely to Zlurpy and PlasmaMan916.
The copying of this class is prohibited.
 */

public class OresHandler {
    private static OresYml yml;
    private static Map<String, Ore> oreTypes = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ArrayList<String>> oreLocs = new ConcurrentHashMap<>();
    private static Map<String, Ore> locToOre = new ConcurrentHashMap<>();//This is somehow null

    public static FileConfiguration getYml() {
        return yml.getConfig();
    }

    public static Map<String, Ore> getLocToOre() {
        return locToOre;
    }

    protected static void loadOres() {
        yml = new OresYml(ZlurpyMining.getInstance());
        for (String string : getYml().getConfigurationSection("ores").getKeys(false)) {
            long regenTime = getYml().getLong("ores." + string + ".regeneration-time");
            int commandAmount = getYml().getInt("ores." + string + ".amount-given");
            ArrayList<String> commands = (ArrayList<String>) getYml().getStringList("ores." + string + ".commands");
            double hardness = getYml().getDouble("ores." + string + ".hardness");
            Material indentifier = Material.valueOf(getYml().getString("ores." + string + ".identifier.material"));
            ItemStack placeableItem = ZlurpyMining.getUtil().createItem(string + ".item");
            NBTItem nbtItem = new NBTItem(placeableItem);
            nbtItem.setString("type", string);
            Ore ore = new Ore(string, hardness, nbtItem.getItem(), indentifier, commands, commandAmount, regenTime);
            oreTypes.put(string, ore);
            ZlurpyMining tf = ZlurpyMining.getInstance();
            File file = new File(tf.getDataFolder(), string + "Locs.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String s : config.getStringList("locs")) {
                locToOre.put(s, ore);
                ArrayList<String> oldData = new ArrayList<>();
                if (oreLocs.containsKey(ore.getName())) {
                    oldData = oreLocs.get(ore.getName());
                    oreLocs.remove(ore.getName());
                }
                oldData.add(s);
                oreLocs.put(ore.getName(), oldData);
                ZlurpyMining.getInstance().getAllRegenLocs().put(s + ":" + ore.getName(), ore);
                String[] data = s.split(",");
                World w = Bukkit.getWorld(data[0]);
                Double x = Double.valueOf(data[1]);
                Double y = Double.valueOf(data[2]);
                Double z = Double.valueOf(data[3]);
                Location blockLocation = new Location(w, x, y, z);
                blockLocation.getBlock().setType(ore.getIndentifier());
            }
        }

    }

    protected static void doSave() throws IOException {
        saveRegenLocations();
        yml.save();
    }

    public static void addOre(Location loc, Ore ore) {
        String mix = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
        locToOre.put(mix, ore);
        ArrayList<String> oldData = new ArrayList<>();
        if (oreLocs.containsKey(ore.getName())) {
            oldData = oreLocs.get(ore.getName());
            oreLocs.remove(ore.getName());
        }
        if (!oldData.contains(mix)) {
            oldData.add(mix);
        }
        oreLocs.put(ore.getName(), oldData);
    }

    private static void saveRegenLocations() throws IOException {
        ZlurpyMining tf = ZlurpyMining.getInstance();
        for (Regen regen : ZlurpyMining.getInstance().getCurrentRegens().keySet()) {
            regen.regen();
        }
        for (String string : getOreLocs().keySet()) {
            File file = new File(tf.getDataFolder(), string + "Locs.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            ArrayList<String> locs = getOreLocs().get(string);
            config.set("locs", locs);
            config.save(file);
        }
    }

    public static void removeOre(Location loc, String mix) {
        Location holoLoc = loc.add(0.5, 2.6, 0.5);
        if (RegenTask.doesHologramExist(holoLoc)) {
            RegenTask.deleteHologram(holoLoc);
        }
        if (locToOre.containsKey(mix)) {
            locToOre.remove(mix);
        }
        for (String s : oreLocs.keySet()) {
            if (oreLocs.get(s).contains(mix)) {
                ArrayList<String> stuff = oreLocs.get(s);
                oreLocs.remove(s);
                stuff.remove(mix);
                oreLocs.put(s, stuff);
            }
        }
        Packets.removeRegen(loc, mix);
    }

    public static Map<String, ArrayList<String>> getOreLocs() {
        return oreLocs;
    }

    public static Map<String, Ore> getOreTypes() {
        return oreTypes;
    }
}