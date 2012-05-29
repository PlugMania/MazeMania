package info.plugmania.mazemania.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;

public class ArenaCommand {
	
	MazeMania plugin;
	public ArenaCommand(MazeMania instance) {
		plugin = instance;
	}
	
	public boolean startHandle(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)){
			Util.sendMessageNotPlayer(sender);
			return true;
		}
		Player player = (Player) sender;
		
		Location spawn = plugin.arena.getSpawn();
		if(spawn == null){
			player.sendMessage(Util.formatMessage("Spawn position not set."));
			return true;
		}
		
		plugin.arena.playing.clear();
		plugin.arena.playing.addAll(plugin.arena.waiting);
		plugin.arena.waiting.clear();

		for(Player p : plugin.arena.playing){
			p.teleport(spawn);
			p.sendMessage(Util.formatMessage("The maze arena has started!"));
		}
		return true;
	}
	
	public boolean joinHandle(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)){
			Util.sendMessageNotPlayer(sender);
			return true;
		}
		Player player = (Player) sender;
		
		Location lobby = plugin.arena.getLobby();
		if(lobby == null){
			player.sendMessage(Util.formatMessage("Lobby position not set."));
			return true;
		}

		plugin.arena.waiting.add(player);
		player.teleport(lobby);
		player.sendMessage(Util.formatMessage("The game will start shortly, you have been teleported to lobby"));
		return true;
	}
	
	public boolean leaveHandle(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)){
			Util.sendMessageNotPlayer(sender);
			return true;
		}
		Player player = (Player) sender;

		if(plugin.arena.waiting.contains(player)){
			plugin.arena.waiting.remove(player);
			player.sendMessage(Util.formatMessage("You have left the maze lobby"));
			return true;
		} else if(plugin.arena.playing.contains(player)){
			plugin.arena.playing.remove(player);
			player.sendMessage(Util.formatMessage("You have left the maze"));
			return false;
		} else {
			player.sendMessage(Util.formatMessage("You are not in the maze"));
			return true;
		}
	}
	
	

}
