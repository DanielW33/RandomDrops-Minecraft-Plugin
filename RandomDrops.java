import org.bukkit.plugin.java.JavaPlugin;

public class RandomDrops extends JavaPlugin {
    RandomDropsConfigManager data;
    @Override
    public void onEnable(){
        this.data = new RandomDropsConfigManager(this);
        this.data.getConfig().options().copyDefaults(true);
        this.data.saveDefaultConfig();

        RunHelperCommands cmds = new RunHelperCommands(this);

        this.getCommand("RandomDrops").setExecutor(cmds);
        this.getCommand("ExtraDrops").setExecutor(cmds);
        this.getServer().getPluginManager().registerEvents(new BreakEventListener(this), this);
    }

    public RandomDropsConfigManager getData(){
        return data;
    }
}
