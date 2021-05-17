package zlurpymining.zlurpymining.Ores.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class Ore {

    private String name;
    private long regenerationTime;
    private Material indentifier;
    private ItemStack placeableItem;
    private double hardness;
    private ArrayList<String> commands;
    private int commandCount;

    public Ore(String name, Double harndness, ItemStack placeableItem, Material indentifier, ArrayList<String> commands, int commandCount, long regenerationTime) {
        this.name = name;
        this.regenerationTime = regenerationTime;
        this.indentifier = indentifier;
        this.placeableItem = placeableItem;
        this.hardness = harndness;
        this.commands = commands;
        this.commandCount = commandCount;
    }

    public ItemStack getPlaceableItem() {
        return placeableItem;
    }

    public Material getIndentifier() {
        return indentifier;
    }

    public long getRegenerationTime() {
        return regenerationTime;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> pickCommands() {
        ArrayList<String> pickedCommands = new ArrayList<>();
        while(pickedCommands.size() < this.commandCount){
            Random random = new Random();
            random.setSeed(System.currentTimeMillis());
            int num = random.nextInt(this.commands.size());
            String pick = this.commands.get(num);
            if(!pickedCommands.contains(pick)){
                pickedCommands.add(pick);
            }
        }
        return pickedCommands;
    }

    public void setHardness(double hardness) {
        this.hardness = hardness;
    }

    public double getHardness() {
        return hardness;
    }

}
