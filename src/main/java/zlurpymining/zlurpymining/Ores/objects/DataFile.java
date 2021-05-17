package zlurpymining.zlurpymining.Ores.Objects;

import org.bukkit.configuration.file.FileConfiguration;
import zlurpymining.zlurpymining.OresHandler;
import zlurpymining.zlurpymining.ZlurpyMining;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataFile {
    
    private ZlurpyMining main;
    private File file;
    protected FileConfiguration config;
    public DataFile(ZlurpyMining main, String name){
            this.main = main;
            if (name.equalsIgnoreCase(name + "Locs.yml")){
                this.file = new File(main.getDataFolder(), name + "Locs.yml");
            }
            if (!file.exists()){
                try{
                    file.createNewFile();
                }catch(IOException ev){
                    ev.printStackTrace();
                }
            }else{
                OresHandler.getOreLocs().put(name, new ArrayList<String>());
                for (String string : this.config.getStringList("locs")){
                    System.out.println("loading " + string);
                    OresHandler.getOreLocs().get(name).add(string.split(":")[0]);
                    main.getAllRegenLocs().put(string.split(":")[0], OresHandler.getOreTypes().get(string.split(":")[1]));
                    OresHandler.getLocToOre().put(string.split(":")[0], OresHandler.getOreTypes().get(string.split(":")[1]));

                }
            }
    }
}
