package es.programahermes.pitazzo;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
  implements Listener, CommandExecutor
{
  Plugin plugin = this;

  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    loadConfiguration();
  }

  public void onDisable()
  {
  }

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    Player player = (Player)sender;
    if (cmd.getName().equalsIgnoreCase("Bakers"))
      player.sendMessage(ChatColor.GREEN + 
        "This server is running Bakers 1.0 by Pitazzo ");
    return false;
  }

  public void loadConfiguration()
  {
    this.plugin.getConfig().options().copyDefaults(true);
    this.plugin.saveConfig();
  }

  @EventHandler(priority=EventPriority.NORMAL)
  public void onPlayerInteractEvent2(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    Inventory inv = p.getInventory();
    ItemStack item = new ItemStack(Material.getMaterial(this.plugin.getConfig()
      .getInt("Item")));
    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
      Block block = e.getClickedBlock();
      if ((block.getType().equals(Material.BURNING_FURNACE)) && 
        (!p.hasPermission("bakers.bypass")))
      {
        if (inv.contains(this.plugin.getConfig().getInt("Item"))) {
          if (this.plugin.getConfig().getBoolean("Consume")) {
            inv.removeItem(new ItemStack[] { item });
            if (this.plugin.getConfig().getString(
              "Messages.Consum") != null)
            {
              p.sendMessage(ChatColor.BLUE + 
                this.plugin.getConfig().getString(
                "Messages.Consum"));
            }
            p.playSound(p.getLocation(), Sound.FIZZ, 
              0.5F, 0.0F);
          }
        }
        else
        {
          p.damage(3.0D);
          if (this.plugin.getConfig().getString("Messages.Damage1") != null)
          {
            p.sendMessage(ChatColor.BLUE + 
              this.plugin.getConfig().getString(
              "Messages.Damage1"));
          }
          if (this.plugin.getConfig().getString("Messages.Damage2") != null)
          {
            p.sendMessage(ChatColor.BLUE + 
              this.plugin.getConfig().getString(
              "Messages.Damage2"));
          }
          if (this.plugin.getConfig().getString(
            "Messages.PaperUseSuggestion") != null)
          {
            p.sendMessage(ChatColor.BLUE + 
              this.plugin.getConfig().getString(
              "Messages.PaperUseSuggestion"));
          }
          e.setCancelled(true);
        }
      }
    }
  }
}