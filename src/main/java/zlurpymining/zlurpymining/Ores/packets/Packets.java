package zlurpymining.zlurpymining.Ores.Packets;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import zlurpymining.zlurpymining.Ores.Objects.Regen;
import zlurpymining.zlurpymining.ZlurpyMining;

public class Packets {

    public static void togglePlayer(Player p) {
        boolean toggled = true;
        if (ZlurpyMining.getInstance().getToggles().containsKey(p)) {
            toggled = ZlurpyMining.getInstance().getToggles().get(p);
            ZlurpyMining.getInstance().getToggles().remove(p);
        }
        if (toggled) {
            toggled = false;
        } else {
            toggled = true;
        }
        ZlurpyMining.getInstance().getToggles().put(p, toggled);
    }

    public static void togglePlayer(Player p, boolean status) {
        if (ZlurpyMining.getInstance().getToggles().containsKey(p)) {
            ZlurpyMining.getInstance().getToggles().remove(p);
        }
        ZlurpyMining.getInstance().getToggles().put(p, status);
    }

    public static void removePlayer(Player p) {
        if (ZlurpyMining.getInstance().getToggles().containsKey(p)) {
            ZlurpyMining.getInstance().getToggles().remove(p);
        }
    }

    public static void removeRegen(Location loc, String mix) {
        for (Regen regen : ZlurpyMining.getInstance().getCurrentRegens().keySet()) {
            if (regen.getBlock().getLocation() == loc) {
                ZlurpyMining.getInstance().getCurrentRegens().remove(regen);
            }
        }
        if (ZlurpyMining.getInstance().getAllRegenLocs().containsKey(mix)) {
            ZlurpyMining.getInstance().getAllRegenLocs().remove(mix);
        }
    }
}
