package zlurpymining.zlurpymining.Ores.Tasks;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IBlockData;
import net.minecraft.server.v1_16_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import zlurpymining.zlurpymining.Ores.CustomEvents.MiningEvent;
import zlurpymining.zlurpymining.Ores.Util;
import zlurpymining.zlurpymining.OresHandler;
import zlurpymining.zlurpymining.Ores.Objects.Ore;
import zlurpymining.zlurpymining.Ores.Packets.PacketHandler;
import zlurpymining.zlurpymining.Ores.Packets.VoidChannelDuplexHandler;
import zlurpymining.zlurpymining.ZlurpyMining;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
© Copyright TranspireDev 2021-2021. All Rights Reserved

This class and all its contents belongs to, and solely to Zlurpy and PlasmaMan916.
The copying of this class is prohibited.
 */

public class ProgressTask extends Thread {

    private static Map<BlockPosition, Double> blockProgress = new ConcurrentHashMap<>();
    private static Map<BlockPosition, Double> oldBlockProgress = new ConcurrentHashMap<>();

    public static Map<BlockPosition, Double> getBlockProgress() {
        return blockProgress;
    }

    public static void copyOldData(BlockPosition location) {
        if (!blockProgress.containsKey(location)) {
            return;
        }
        if (oldBlockProgress.containsKey(location)) {
            oldBlockProgress.remove(location);
        }
        oldBlockProgress.put(location, blockProgress.get(location));
    }

    private boolean isOre(Block block) {
        String mix = block.getWorld().getName() + "," + block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ();
        try {
            OresHandler.getLocToOre().keySet();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        if (OresHandler.getLocToOre().containsKey(mix)) {
            Ore ore = OresHandler.getLocToOre().get(mix);
            if (ore == null) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private Ore getOre(Block block) {
        Location loc = block.getLocation();
        String mix = block.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
        if (OresHandler.getLocToOre().containsKey(mix)) {
            Ore ore = OresHandler.getLocToOre().get(mix);
            if (ore == null) {
                return null;
            } else {
                return ore;
            }
        } else {
            return null;
        }
    }

    private PacketHandler packetSender = new PacketHandler();

    public void resetBlockAnimations() {
        packetSender.resetBlockAnimations();
    }

    public void resetBlockAnimations(Player player) {
        packetSender.resetBlockAnimations(player);
    }

    private Object sync = new Object();

    @Override
    public void run() {
        super.run();
        packetSender.start();
        synchronized (sync) {
            while (true) {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (p != null) {
                        try {
                            Block bl = p.getTargetBlockExact(5);
                            if (bl != null) {
                                if (isOre(bl)) {
                                    Ore ore = getOre(bl);
                                    double hardnessMulti = 1d / (ore.getHardness() / 100d);
                                    BlockPosition pos = new BlockPosition(bl.getX(), bl.getY(), bl.getZ());
                                    if (VoidChannelDuplexHandler.getBlockBreak(pos)) {
                                        EntityPlayer pl = ((CraftPlayer) p).getHandle();
                                        World world = pl.world;
                                        IBlockData b = world.getType(pos);
                                        int currentTick;
                                        Field currentTickF = null;
                                        currentTickF = pl.playerInteractManager.getClass().getDeclaredField("currentTick");
                                        currentTickF.setAccessible(true);
                                        currentTick = currentTickF.getInt(pl.playerInteractManager);
                                        int lastDigTick;
                                        Field lastDigTickF = null;
                                        lastDigTickF = pl.playerInteractManager.getClass().getDeclaredField("lastDigTick");
                                        lastDigTickF.setAccessible(true);
                                        lastDigTick = lastDigTickF.getInt(pl.playerInteractManager);
                                        int k = currentTick - lastDigTick;
                                        float f = b.getDamage(pl, world, pos) * (float) (k + 1);
                                        Double progress = Double.valueOf(String.valueOf((f * 100F))) * hardnessMulti;

                                        if (blockProgress.containsKey(pos)) {
                                            blockProgress.remove(pos);
                                        }
                                        Double oldP = 0d;
                                        if (oldBlockProgress.containsKey(pos)) {
                                            oldP = oldBlockProgress.get(pos);
                                        }
                                        if (p.getGameMode() != GameMode.CREATIVE) {
                                            blockProgress.put(pos, progress + oldP);
                                        }

                                        Double prog = blockProgress.get(pos);
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                double barProg = Math.round(prog * 100.0) / 100.0;
                                                int progressBarProg = (int) (prog / 5);
                                                int progressBarProgRed = 20 - progressBarProg;
                                                String txt = "&2";
                                                for (int i = 0; i < progressBarProg; i++) {
                                                    txt = txt + "|";
                                                }
                                                txt = txt + "&c";
                                                for (int i = 0; i < progressBarProgRed; i++) {
                                                    txt = txt + "|";
                                                }
                                                txt = txt + " &7(" + barProg + "%)";
                                                checkSettings(txt, p.getPlayer(), bl);
                                                if (prog < 100D && prog > -1D) {
                                                } else {
                                                    bl.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, bl.getLocation(), 2);
                                                    MiningEvent e = new MiningEvent(p, bl);
                                                    Bukkit.getServer().getPluginManager().callEvent(e);
                                                    blockProgress.remove(pos);
                                                    if (oldBlockProgress.containsKey(pos)) {
                                                        oldBlockProgress.remove(pos);
                                                    }
                                                }
                                            }
                                        }.runTaskLater(ZlurpyMining.getInstance(), 1l);
                                    }
                                } else {
                                }
                            }
                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    sync.wait(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void checkSettings(String txt, Player player, Block block) {
        if (ZlurpyMining.getInstance().getConfig().getBoolean("settings.actionbar")) {
            Util.sendActionbar(player, Util.toColor(txt));
        }
    }
}