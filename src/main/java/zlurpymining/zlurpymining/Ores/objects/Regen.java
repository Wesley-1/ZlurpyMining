package zlurpymining.zlurpymining.Ores.Objects;

import org.bukkit.Location;
import org.bukkit.block.Block;
import zlurpymining.zlurpymining.OresHandler;
import zlurpymining.zlurpymining.ZlurpyMining;


public class Regen {

    private long end;
    private long start;
    private Block block;
    private Location loc;
    private Ore ore;
    private boolean regenerated = false;

    public Regen(Block block, Ore ore, long start, long end) {

        this.start = start;
        this.end = end;
        this.block = block;
        this.loc = block.getLocation();
        this.ore = ore;
    }

    public Ore getOre() {
        return ore;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public Block getBlock() {
        return block;
    }

    public void regen() {
        if (regenerated) {
            return;
        }
        regenerated = true;
        this.block.setType(this.ore.getIndentifier());
        OresHandler.addOre(this.block.getLocation(), this.ore);
        Location flareLoc = this.getBlock().getLocation();
        OresHandler.addOre(flareLoc, ore);
        ZlurpyMining.getInstance().getCurrentRegens().remove(this);

    }
}