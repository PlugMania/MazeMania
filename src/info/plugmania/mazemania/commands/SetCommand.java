package info.plugmania.mazemania.commands;

import java.util.HashMap;

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
		
		if(args.length <= 1){
			sender.sendMessage(Util.formatMessage("---------------------- MazeMania Setup ----------------------"));
			sender.sendMessage(Util.formatMessage("Setup Commands:"));
			sender.sendMessage(Util.formatMessage("/maze set [pos1|pos2]  - Set the arena boundaries"));
			sender.sendMessage(Util.formatMessage("/maze set spawn        - Set the arena spawn (random spawns based on this)"));
			sender.sendMessage(Util.formatMessage("/maze set exit         - Set the arena exit, the location you find to win"));
			sender.sendMessage(Util.formatMessage("Items needed to win at exit are configurable!"));
		}
		
		if(args.length > 1){
			if(args[1].equalsIgnoreCase("pos1")){
				Location loc = player.getLocation();
				plugin.arena.setPos1(loc);
				player.sendMessage(Util.formatMessage("Arena position set."));
				return true;
			} else if(args[1].equalsIgnoreCase("pos2")){
				Location loc = player.getLocation();
				plugin.arena.setPos2(loc);
				player.sendMessage(Util.formatMessage("Arena position set."));
				return true;
			}  else if(args[1].equalsIgnoreCase("spawn")){
				Location loc = player.getLocation();
				plugin.arena.setSpawn(loc);
				player.sendMessage(Util.formatMessage("Arena spawn position set."));
				return true;
			} else if(args[1].equalsIgnoreCase("exit")){
				Location loc = player.getLocation();
				plugin.arena.setExit(loc);
				player.sendMessage(Util.formatMessage("Arena exit position set."));
				return true;
			}
			
		}
		
		return false;
	}
	
	public boolean addHandle(CommandSender sender, String[] args){
		if(!(sender instanceof Player)){
			Util.sendMessageNotPlayer(sender);
			return true;
		}
		Player player = (Player) sender;
		
		if(args.length <= 1){
			sender.sendMessage(Util.formatMessage("Incorrect usage"));
			//print out arena sub commands, user typed, /maze add
		}
		
		if(args.length > 1){
//			if(args[1].equalsIgnoreCase("")){
//				
//			}
			
		}
		
		return false;
	}
}
