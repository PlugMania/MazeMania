package info.plugmania.mazemania.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import info.plugmania.mazemania.ConfigUtil;
import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;

public class ArenaCommand {

	MazeMania plugin;
	public ArenaCommand(MazeMania instance) {
		plugin = instance;
	}

	public boolean handle(CommandSender sender, String[] args){
		if(!(sender instanceof Player)){
			Util.sendMessageNotPlayer(sender);
			return true;
		}
		Player player = (Player) sender;
		
		if(!plugin.hasPermission(player, "default")){
			Util.sendMessageNoPerms(sender);
			return true;
		}
		if(args.length <= 1){
			sender.sendMessage(Util.formatMessage("Incorrect usage"));
			//print out arena sub commands, user typed, /maze set
		}
		
		if(args.length > 1){
			if(args[1].equalsIgnoreCase("pos1")){
				Location loc = player.getLocation();
				int blockX = loc.getBlockX();
				int blockY = loc.getBlockY();
				int blockZ = loc.getBlockZ();
				plugin.mainConf.set("arena.pos1", blockX + ":" + blockY + ":" + blockZ);
				ConfigUtil.saveConfig(plugin.mainConf, "config");
				player.sendMessage(Util.formatMessage("Arena position set."));
			} else if(args[1].equalsIgnoreCase("pos2")){
				Location loc = player.getLocation();
				int blockX = loc.getBlockX();
				int blockY = loc.getBlockY();
				int blockZ = loc.getBlockZ();
				plugin.mainConf.set("arena.pos2", blockX + ":" + blockY + ":" + blockZ);
				ConfigUtil.saveConfig(plugin.mainConf, "config");
				player.sendMessage(Util.formatMessage("Arena position set."));
			}
			
		}
		
		return false;
	}
}
