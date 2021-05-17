package zlurpymining.zlurpymining;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import zlurpymining.zlurpymining.Ores.Commands.OresCommands;
import zlurpymining.zlurpymining.Ores.Commands.OresTab;
import zlurpymining.zlurpymining.Ores.Listeners.*;
import zlurpymining.zlurpymining.Ores.Util;
import zlurpymining.zlurpymining.Ores.Objects.*;
import zlurpymining.zlurpymining.Ores.Packets.Packets;
import zlurpymining.zlurpymining.Ores.Packets.PlayerEvents;
import zlurpymining.zlurpymining.Ores.Packets.PlayerPacketHandler;
import zlurpymining.zlurpymining.Ores.Tasks.ProgressTask;
import zlurpymining.zlurpymining.Ores.Tasks.RegenTask;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
© Copyright TranspireDev 2021-2021. All Rights Reserved

This class and all its contents belongs to, and solely to Zlurpy and PlasmaMan916.
The copying of this class is prohibited.
 */

public final class ZlurpyMining extends JavaPlugin {
    private static ZlurpyMining instance;
    private HashMap<String, FileConfiguration> customConfigs = new HashMap<String, FileConfiguration>();
    private HashMap<String, Ore> allRegenLocs = new HashMap<>();
    private static Util util;
    private ProgressTask fph = new ProgressTask();
    private static PlayerPacketHandler packetHandler;
    private RegenTask task;
    private HashMap<Regen, Ore> currentRegens = new HashMap<>();
    private static Map<Player, Boolean> toggles = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        instance = this;
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            this.util = new Util();
            loadConfig();
            loadCommands();
            loadListeners();
            OresHandler.loadOres();
            task = new RegenTask();
            fph.start();
            packetHandler = new PlayerPacketHandler();
            for (Player p : this.getServer().getOnlinePlayers()) {
            packetHandler.addPlayer(p);
            Packets.togglePlayer(p);
        }
        packetHandler.start();
    }
    @Override
    public void onDisable() {
        fph.resetBlockAnimations();
        try {
            OresHandler.doSave();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Player p : this.getServer().getOnlinePlayers()) {
            packetHandler.removePlayer(p);
            Packets.removePlayer(p);
        }
        task.cancel();
        fph.stop();
        packetHandler.stop();
        instance = null;
    }

    private void loadCommands() {
        this.getCommand("ores").setExecutor(new OresCommands());
        this.getCommand("ores").setTabCompleter(new OresTab());
    }

    private void loadListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BlockBreak(), this);
        pluginManager.registerEvents(new BlockPlace(), this);
        pluginManager.registerEvents(new MiningListener(), this);
        pluginManager.registerEvents(new PlayerEvents(), this);
    }

    public HashMap<Regen, Ore> getCurrentRegens() {
        return currentRegens;
    }

    public static Util getUtil() {
        return util;
    }

    public HashMap<String, FileConfiguration> getCustomConfigs() {
        return this.customConfigs;
    }

    private void loadConfig() {
        saveDefaultConfig();
    }
    public HashMap<String, Ore> getAllRegenLocs() {
        return allRegenLocs;
    }
    public static PlayerPacketHandler getPacketHandler() {
        return packetHandler;
    }

    public static ZlurpyMining getInstance() {
        return ZlurpyMining.instance;
    }

    public static Map<Player, Boolean> getToggles() {
        return toggles;
    }

    public ProgressTask getFph() {
        return fph;
    }
}