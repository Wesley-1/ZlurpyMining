package zlurpymining.zlurpymining.Ores.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zlurpymining.zlurpymining.OresHandler;
import zlurpymining.zlurpymining.Ores.Util;
import zlurpymining.zlurpymining.Ores.objects.Ore;

public class OresCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("ores")){
        switch (args.length) {
            case 1:
                if (Util.checkSender(sender)) {
                    Player player = (Player) sender;
                    if (Util.checkPermission(player, "ores.help")) {
                        player.sendMessage(Util.toColor("&b/ores give <player> <type>"));
                        player.sendMessage(Util.toColor("&b" + OresHandler.getOreTypes()));
                    }

                }
                break;
            case 4:
                if ((args[0].equalsIgnoreCase("ores")) && args[1].equalsIgnoreCase("give")){
                    Player target = Bukkit.getPlayer(args[2]);
                    if (target != null){
                        String type = args[3];
                        if (OresHandler.getOreTypes().containsKey(type)){
                            Ore ore = OresHandler.getOreTypes().get(type);
                            target.getInventory().addItem(ore.getPlaceableItem());
                        }else{
                            target.sendMessage(Util.toColor("&cInvalid Ore"));
                        }
                    }else{
                        target.sendMessage(Util.toColor("&cInvalid Player"));
                    }
                }else{

                }

                break;
            default:
                break;
        }



        }

        return false;
    }
}
