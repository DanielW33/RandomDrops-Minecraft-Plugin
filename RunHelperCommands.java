import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RunHelperCommands implements CommandExecutor {
    RandomDrops plugin;
    RandomDropsConfigManager Data;

    public RunHelperCommands(RandomDrops Plugin){
        plugin = Plugin;
        Data = Plugin.getData();
    }
    @Override
    public boolean onCommand(CommandSender Sender, Command Cmd, String Label, String args[]){
        if(Label.equalsIgnoreCase("RandomDrops")){
            if(args.length < 1){
                Sender.sendMessage("Use command /RandomDrops reload");
                return true;
            }
            else if(args[0].equalsIgnoreCase("reload")){
                Sender.sendMessage(ChatColor.RED+ "Reloading RandomDrops!");
                Data.reloadConfig();
                return true;
            }
            else{
                Sender.sendMessage(ChatColor.GRAY + "Run Command &b/RandomDrops reload");
                return true;
            }
        }
        else if(Label.equalsIgnoreCase("ExtraDrops")){
            Sender.sendMessage(ChatColor.RED + "You are receiving extra drops on block break...");
            return true;
        }
        return false;
    }
}
