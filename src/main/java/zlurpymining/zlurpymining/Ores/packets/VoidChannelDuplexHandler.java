package zlurpymining.zlurpymining.Ores.Packets;

import io.netty.channel.*;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import zlurpymining.zlurpymining.OresHandler;
import zlurpymining.zlurpymining.Ores.Objects.Ore;
import zlurpymining.zlurpymining.Ores.Tasks.ProgressTask;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
© Copyright TranspireDev 2021-2021. All Rights Reserved

This class and all its contents belongs to, and solely to Zlurpy and PlasmaMan916.
The copying of this class is prohibited.
 */

public class VoidChannelDuplexHandler extends ChannelDuplexHandler {
    private Player player;
    private ChannelHandlerContext context = null;
    private ChannelPromise promise = null;

    public ChannelHandlerContext getContext() {
        return context;
    }

    public ChannelPromise getPromise() {
        return promise;
    }

    public VoidChannelDuplexHandler(Player player) {
        this.player = player;
    }

    private static Map<BlockPosition, Boolean> blockBreaking = new ConcurrentHashMap<>();

    public static boolean getBlockBreak(BlockPosition pos) {
        if (blockBreaking.containsKey(pos)) {
            return blockBreaking.get(pos);
        } else {
            return false;
        }
    }

    private boolean isOre(Block block) {
        Location loc = block.getLocation();
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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof PacketPlayInBlockDig) {
            PacketPlayInBlockDig packet = (PacketPlayInBlockDig) msg;
            BlockPosition a;
            Field fa = packet.getClass().getDeclaredField("a");
            fa.setAccessible(true);
            a = (BlockPosition) fa.get(packet);
            PacketPlayInBlockDig.EnumPlayerDigType c;
            Field fc = packet.getClass().getDeclaredField("c");
            fc.setAccessible(true);
            c = (PacketPlayInBlockDig.EnumPlayerDigType) fc.get(packet);
            Block block = player.getWorld().getBlockAt(a.getX(), a.getY(), a.getZ());

            if (blockBreaking.containsKey(a)) {
                blockBreaking.remove(a);
            }
            if (c == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                blockBreaking.put(a, true);
            } else if (c == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK || c == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                blockBreaking.put(a, false);
                ProgressTask.copyOldData(a);
            }

            if (isOre(block) && c == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
                return;
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        this.context = ctx;
        this.promise = promise;

        if (msg instanceof PacketPlayOutBlockBreak) {
            PacketPlayOutBlockBreak packet = (PacketPlayOutBlockBreak) msg;
            BlockPosition c;
            Field ca = packet.getClass().getDeclaredField("c");
            ca.setAccessible(true);
            c = (BlockPosition) ca.get(packet);
            Block block = player.getWorld().getBlockAt(c.getX(), c.getY(), c.getZ());
            IBlockData d;
            Field da = packet.getClass().getDeclaredField("d");
            da.setAccessible(true);
            d = (IBlockData) da.get(packet);
            PacketPlayInBlockDig.EnumPlayerDigType a;
            Field fa = packet.getClass().getDeclaredField("a");
            fa.setAccessible(true);
            a = (PacketPlayInBlockDig.EnumPlayerDigType) fa.get(packet);
            boolean e;
            Field ea = packet.getClass().getDeclaredField("e");
            ea.setAccessible(true);
            e = ea.getBoolean(packet);

            if (isOre(block)) {
                e = true;
            }
        }

        if (msg instanceof PacketPlayOutBlockBreakAnimation) {
            PacketPlayOutBlockBreakAnimation packet = (PacketPlayOutBlockBreakAnimation) msg;

            int a;
            Field fa = packet.getClass().getDeclaredField("a");
            fa.setAccessible(true);
            a = fa.getInt(packet);

            BlockPosition b;
            Field fb = packet.getClass().getDeclaredField("b");
            fb.setAccessible(true);
            b = (BlockPosition) fb.get(packet);

            int c;
            Field fc = packet.getClass().getDeclaredField("c");
            fc.setAccessible(true);
            c = fc.getInt(packet);

        }

        super.write(ctx, msg, promise);

    }

    public VoidChannelDuplexHandler startListen() {//Use the reflection for getting the channel I sent you
        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), this);
        return this;
    }

    public VoidChannelDuplexHandler stopListening() {//Use the reflection for getting the channel I sent you
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        if (channel == null) return this;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
        return this;
    }
}