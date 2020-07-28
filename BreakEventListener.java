import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BreakEventListener implements Listener {
    RandomDrops plugin;
    RandomDropsConfigManager Data;
    public BreakEventListener(RandomDrops Plugin){
        plugin = Plugin;
        Data = plugin.getData();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent Event) {
        if (Event.getPlayer().hasPermission("RandomDrops.ExtraDrops")) {
            this.Data.getConfig().getConfigurationSection("Blocks").getKeys(false).forEach(Block -> {
                if (Event.getBlock().getType().equals(Material.matchMaterial(this.Data.getConfig().getString("Blocks." + Block + ".Material")))) {
                    Random rand = new Random();
                    double RandomNum = rand.nextInt(1000000000);
                    if (this.Data.getConfig().getBoolean("Blocks." + Block + ".PerItemDropChance")) {
                        int i;
                        if(this.Data.getConfig().getInt("Blocks." + Block + ".Inputs") != 1) {
                            i = rand.nextInt(1000000000) % this.Data.getConfig().getInt("Blocks." + Block + ".Inputs");
                        }
                        else{
                            i = 0;
                        }
                        RandomNum = RandomNum % 100;
                        List DropList = this.Data.getConfig().getList("Blocks." + Block + ".PossibleDrops");
                        ArrayList<String> MaterialList = new ArrayList<>();
                        MaterialList.addAll(DropList);

                        String MaterialBlock = MaterialList.get(i);
                        String[] ItemMetaArray = MaterialBlock.split("!!", 30);
                        Double Chance = Double.parseDouble(ItemMetaArray[ItemMetaArray.length - 2]);

                        double Rando = Math.random();
                        //STOP HERE IF IT DOES NOT GO PAST CHANCE
                        if (Rando <= Chance) {

                            //Creating lore List and Setting values...
                            int LoreCount = Integer.parseInt(ItemMetaArray[1]);
                            List<String> LoreList = new ArrayList<String>();
                            for (int j = 0; j < LoreCount; j++) {
                                if (ItemMetaArray[j + 3].equals("%EMPTY%")) {
                                    ItemMetaArray[j + 3] = "";
                                }
                                LoreList.add(ChatColor.translateAlternateColorCodes('&', ItemMetaArray[j + 3]));
                            }
                            //End of setting lore to list
                            //Giving random quantity between 1 and QUANTITY
                            int Quantity = rand.nextInt(1000000000) % Integer.parseInt(ItemMetaArray[ItemMetaArray.length - 1]);
                            if (Quantity == 0) {
                                Quantity = Quantity + 1;
                            }
                            Event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&7You just found " + Quantity +
                                    " " + ItemMetaArray[2] + " &7in a " + Block + " block."));
                            //Setting ItemStack with metadata collected..
                            ItemStack Item = new ItemStack(Material.matchMaterial(ItemMetaArray[0]), Quantity);
                            ItemMeta Meta = Item.getItemMeta();
                            Meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemMetaArray[2]));
                            Meta.setLore(LoreList);

                            if (!ItemMetaArray[LoreCount + 3].equals("NONE")) {
                                Meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                            }
                            Item.setItemMeta(Meta);

                            //DROPPING ITEM IN THE WORLD
                            World AffectWorld = Event.getPlayer().getWorld();
                            AffectWorld.dropItem(Event.getBlock().getLocation(), Item);
                            return;
                        }
                    } else {
                        RandomNum = Math.random();
                        if (RandomNum <= this.Data.getConfig().getInt("Blocks." + Block + ".DropChance")) {

                            int i = rand.nextInt(1000000000) % this.Data.getConfig().getInt("Blocks." + Block + ".Inputs");

                            World AffectWorld = Event.getPlayer().getWorld();

                            List DropList = this.Data.getConfig().getList("Blocks." + Block + ".PossibleDrops");
                            ArrayList<String> MaterialList = new ArrayList<>();
                            MaterialList.addAll(DropList);
                            if(i == 0){
                                i = 1;
                            }
                            String StringArray[] = MaterialList.get(i).split(":", 2);
                            Material BlockMaterial = Material.matchMaterial(StringArray[0]);

                            int Quantity = rand.nextInt(1000000000)%Integer.parseInt(StringArray[1]);
                            if(Quantity == 0){
                                Quantity = Quantity + 1;
                            }
                            AffectWorld.dropItem(Event.getBlock().getLocation(), new ItemStack(BlockMaterial, Quantity));
                            return;
                        } else {
                            return;
                        }
                    }
                }
            });
        }
        else{
            return;
        }
    }
}
