package zlurpymining.zlurpymining.Ores.CustomEvents;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/*
© Copyright TranspireDev 2021-2021. All Rights Reserved

This class and all its contents belongs to, and solely to Zlurpy and PlasmaMan916.
The copying of this class is prohibited.
 */

public class MiningEvent extends Event implements Cancellable {
    private Block block;
    private Player player;

    public MiningEvent(Player player, Block block ){
        this.block = block;
        this.player = player;
    }

    private static final HandlerList HANDLERS_LIST = new HandlerList();


    private boolean isCancelled;
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Block getBlock() {
        return block;
    }

    public Player getPlayer() {
        return player;
    }


}
