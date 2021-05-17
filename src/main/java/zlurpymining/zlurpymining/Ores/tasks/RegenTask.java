package zlurpymining.zlurpymining.Ores.tasks;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import zlurpymining.zlurpymining.Ores.objects.Ore;
import zlurpymining.zlurpymining.Ores.objects.Regen;
import zlurpymining.zlurpymining.ZlurpyMining;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
© Copyright TranspireDev 2021-2021. All Rights Reserved

This class and all its contents belongs to, and solely to Zlurpy and PlasmaMan916.
The copying of this class is prohibited.
 */

public class RegenTask extends BukkitRunnable {

    ZlurpyMining main = ZlurpyMining.getInstance();

    public RegenTask() {
        this.runTaskTimer(main, 0, 5);
    }

    private static Map<Location, Hologram> holograms = new ConcurrentHashMap<>();

    public static void deleteHologram(Location loc) {
        if (holograms.containsKey(loc)) {
            holograms.get(loc).delete();
            holograms.remove(loc);
        }
    }

    public static Boolean doesHologramExist(Location loc) {
        if (holograms.containsKey(loc)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void run() {
        for (Regen regen : main.getCurrentRegens().keySet()) {
            if (System.currentTimeMillis() >= regen.getEnd()) {
                regen.regen();
            }
        }
    }
}
