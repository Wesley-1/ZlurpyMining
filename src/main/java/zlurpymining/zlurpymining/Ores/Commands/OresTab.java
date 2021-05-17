package zlurpymining.zlurpymining.Ores.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import zlurpymining.zlurpymining.OresHandler;

import java.util.ArrayList;
import java.util.List;

public class OresTab implements TabCompleter {
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        List<String> tabComplete = new ArrayList<>();
        if(args.length == 1){
            if("ores".startsWith(args[0].toLowerCase()) || args[0].equals("")) {
                tabComplete.add("ores");
            }
            if (args[0].equals("")) {
                tabComplete.add("ores");
            }
        }else if(args.length == 2){
            if("give".startsWith(args[1].toLowerCase()) || args[1].equals("")) {
                tabComplete.add("give");
            }
        }else if(args.length == 3){
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.getName().startsWith(args[2]) || args[2].equals("")){
                    tabComplete.add(p.getName());
                }
            }
        }else if(args.length == 4){
            for(String s : OresHandler.getOreTypes().keySet()){
                if(s.toLowerCase().startsWith(args[3].toLowerCase()) || args[3].equals("")) {
                    tabComplete.add(s);
                }
            }
        }
        return tabComplete;
    }

}
