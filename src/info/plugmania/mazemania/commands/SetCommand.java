package info.plugmania.mazemania.commands;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import info.plugmania.mazemania.ConfigUtil;
import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;

public class SetCommand {

	public HashMap<Player, Location> selected = new HashMap<Player, Location>();
	
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
			sender.sendMessage(Util.formatMessage("Incorrect usage"));
			//print out arena sub commands, user typed, /maze set
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
			} else if(args[1].equalsIgnoreCase("lobby")){
				Location loc = player.getLocation();
				plugin.arena.setLobby(loc);
				player.sendMessage(Util.formatMessage("Arena lobby position set."));
				return true;
			} else if(args[1].equalsIgnoreCase("spawn")){
				Location loc = player.getLocation();
				plugin.arena.setSpawn(loc);
				player.sendMessage(Util.formatMessage("Arena spawn position set."));
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
			if(args[1].equalsIgnoreCase("chest")){
				Location loc = null;
				if(selected.containsKey(player))
					loc = selected.get(player);
				if(loc == null){
					player.sendMessage(Util.formatMessage("Select a block first with a magma cream."));
					return true;
				}
				
				player.sendMessage(Util.formatMessage("Position set. Debug - (Unimplemented)"));
				return true;
			}
			
		}
		
		return false;
	}
}
