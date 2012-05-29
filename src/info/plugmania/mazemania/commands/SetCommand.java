package info.plugmania.mazemania.commands;

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
				plugin.arena.setPos1(loc);
				player.sendMessage(Util.formatMessage("Arena position set."));
			} else if(args[1].equalsIgnoreCase("pos2")){
				Location loc = player.getLocation();
				plugin.arena.setPos2(loc);
				player.sendMessage(Util.formatMessage("Arena position set."));
			} else if(args[1].equalsIgnoreCase("lobby")){
				Location loc = player.getLocation();
				plugin.arena.setLobby(loc);
				player.sendMessage(Util.formatMessage("Arena lobby position set."));
			} else if(args[1].equalsIgnoreCase("spawn")){
				Location loc = player.getLocation();
				plugin.arena.setSpawn(loc);
				player.sendMessage(Util.formatMessage("Arena spawn position set."));
			}
			
		}
		
		return false;
	}
}
