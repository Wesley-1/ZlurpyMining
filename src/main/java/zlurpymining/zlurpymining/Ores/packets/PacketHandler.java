package zlurpymining.zlurpymining.Ores.packets;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import zlurpymining.zlurpymining.Ores.objects.Regen;
import zlurpymining.zlurpymining.Ores.tasks.ProgressTask;
import zlurpymining.zlurpymining.ZlurpyMining;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
© Copyright TranspireDev 2021-2021. All Rights Reserved

This class and all its contents belongs to, and solely to Zlurpy and PlasmaMan916.
The copying of this class is prohibited.
 */

public class PacketHandler extends Thread{

    private Map<BlockPosition,Integer> randomInts = new HashMap<>();
        private Object syncro = new Object();
        @Override
        public void run() {
            super.run();
            synchronized (syncro){
                while(true) {
                    Map<BlockPosition, Double> blockProgress = ProgressTask.getBlockProgress();
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        for (BlockPosition pos : blockProgress.keySet()) {
                            double p = blockProgress.get(pos);
                            int prog = (int)p;
                            if(!randomInts.containsKey(pos)){
                                Random rand = new Random(System.currentTimeMillis());
                                randomInts.put(pos,rand.nextInt(1000));
                            }
                            PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(randomInts.get(pos), pos, (prog / 10));
                            ZlurpyMining.getPacketHandler().sendPacket(player, packet);
                        }
                    }
                    try {
                        syncro.wait(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        public void resetBlockAnimations(Player player){
        }
        public void resetBlockAnimations(){
            for (Regen regen : ZlurpyMining.getInstance().getCurrentRegens().keySet()) {
                regen.regen();
            }
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                resetBlockAnimations(player);
            }
        }
    }