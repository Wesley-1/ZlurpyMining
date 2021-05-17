package zlurpymining.zlurpymining.Ores.Files;


import org.bukkit.configuration.file.FileConfiguration;
import zlurpymining.zlurpymining.ZlurpyMining;
import zlurpymining.zlurpymining.Ores.objects.AbstractFile;

public class OresYml extends AbstractFile {
    public OresYml(ZlurpyMining main) {
        super(main, "ores.yml");
    }

    public FileConfiguration getConfig(){
        return this.config;
    }
}
