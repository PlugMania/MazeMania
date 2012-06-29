package info.plugmania.mazemania.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import info.plugmania.mazemania.ConfigUtil;
import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;

public class SetCommand {

	MazeMania plugin;

	public SetCommand(MazeMania instance) {
		plugin = instance;
	}

	public boolean handle(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Util.sendMessageNotPlayer(sender);
			return true;
		}
		Player player = (Player) sender;

		if (args.length <= 1) {
			sender.sendMessage(Util.formatMessage("--------------- MazeMania Setup ----------------"));
			sender.sendMessage(Util.formatMessage("Setup Commands:"));
			sender.sendMessage(Util.formatMessage("/maze set [pos1|pos2]  - Set the arena boundaries"));
			sender.sendMessage(Util.formatMessage("/maze set spawn        - Set the arena spawn (random spawns based on this)"));
			sender.sendMessage(Util.formatMessage("/maze set exit         - Set the arena exit, the location you find to win"));
			sender.sendMessage(Util.formatMessage("Items needed to win at exit are configurable!"));
		}

		if (args.length > 1) {
			if (args[1].equalsIgnoreCase("pos1")) {
				Location loc = player.getLocation();
				plugin.arena.setPos1(loc);
				player.sendMessage(Util.formatMessage("Arena position set."));
				return true;
			} else if (args[1].equalsIgnoreCase("pos2")) {
				Location loc = player.getLocation();
				plugin.arena.setPos2(loc);
				player.sendMessage(Util.formatMessage("Arena position set."));
				return true;
			} else if (args[1].equalsIgnoreCase("spawn")) {
				Location loc = player.getLocation();
				plugin.arena.setSpawn(loc);
				player.sendMessage(Util.formatMessage("Arena spawn position set."));
				return true;
			} else if (args[1].equalsIgnoreCase("exit")) {
				Location loc = player.getLocation();
				plugin.arena.setExit(loc);
				player.sendMessage(Util.formatMessage("Arena exit position set."));
				return true;
			}

		}

		return false;
	}

	public boolean triggerHandle(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Util.sendMessageNotPlayer(sender);
			return true;
		}
		Player player = (Player) sender;

		if (args.length <= 1) {
			sender.sendMessage(Util.formatMessage("-------------- MazeMania Triggers ---------------"));
			sender.sendMessage(Util.formatMessage("Event Types:"));
			sender.sendMessage(Util.formatMessage("Fire, Lightning, Mob, Poison"));
			sender.sendMessage(Util.formatMessage("Setup Commands:"));
			sender.sendMessage(Util.formatMessage("Commands are relative to the block you are standing on"));
			sender.sendMessage(Util.formatMessage("/maze trigger add [type]    - Add the type of effect to block"));
			sender.sendMessage(Util.formatMessage("/maze trigger remove [type] - Remove type of effect from block"));
			sender.sendMessage(Util.formatMessage("/maze trigger clear         - Clear all effects on block"));
		}

		if (args.length > 1) {
			if (!plugin.arena.isInArena(player.getLocation())) {
				player.sendMessage(Util.formatMessage("You must be inside the arena to edit a trigger"));
				return true;
			}
			Location pLoc = player.getLocation().getBlock().getLocation();
			//UID for location, not needed for retrieval since it loops though any keys
			String basePath = pLoc.getWorld().getName() + pLoc.getBlockX() + "" + pLoc.getBlockY() + "" + pLoc.getBlockZ();
			if (args[1].equalsIgnoreCase("add")) {
				if (args.length > 2) {

					if (!plugin.triggers.trigConf.isSet("triggers." + basePath)) {
						plugin.triggers.trigConf.set("triggers." + basePath + ".loc", pLoc.getWorld().getName() + ":" + pLoc.getBlockX() + ":" + pLoc.getBlockY() + ":" + pLoc.getBlockZ());
					}
					List<String> events = plugin.triggers.trigConf.getStringList("triggers." + basePath + ".events");
					if (events == null) events = new ArrayList<String>();

					if (args[2].equalsIgnoreCase("fire")) {
						events.add("fire");
					} else if (args[2].equalsIgnoreCase("lightning")) {
						events.add("lightning");
					} else if (args[2].equalsIgnoreCase("poison")) {
						events.add("poison");
					} else if (args[2].equalsIgnoreCase("mob")) {
						events.add("mob");
					}
					//save to config
					plugin.triggers.trigConf.set("triggers." + basePath + ".events", events);
					ConfigUtil.saveConfig(plugin.triggers.trigConf, "triggers");
					//load into current memory db
					plugin.triggers.triggers.put(basePath, events);

					sender.sendMessage(Util.formatMessage("Trigger added."));
					return true;
				}
			} else if (args[1].equalsIgnoreCase("clear")) {
				if (plugin.triggers.trigConf.get("triggers." + basePath) == null) {
					sender.sendMessage(Util.formatMessage("No events are attatched to this block."));
					return true;
				}

				plugin.triggers.trigConf.set("triggers." + basePath, null);
				ConfigUtil.saveConfig(plugin.triggers.trigConf, "triggers");

				plugin.triggers.triggers.remove(basePath);

				sender.sendMessage(Util.formatMessage("Events attatched to this block have been cleared."));
				return true;
			}
			return false;
		}

		return false;
	}
}
