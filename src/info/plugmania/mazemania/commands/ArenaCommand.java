package info.plugmania.mazemania.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;

public class ArenaCommand {
	
	MazeMania plugin;
	public ArenaCommand(MazeMania instance) {
		plugin = instance;
	}
	
	private HashMap<Player, ItemStack[]> invs = new HashMap<Player, ItemStack[]>();
	
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
		plugin.arena.setPlaying(true);

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

		invs.put(player, player.getInventory().getContents());
		player.getInventory().clear();
		plugin.arena.waiting.add(player);
		plugin.arena.setWaiting(true);
		
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
			
			if(plugin.arena.waiting.isEmpty())
				plugin.arena.setWaiting(false);
			
			
			player.teleport(player.getWorld().getSpawnLocation());
			
			player.getInventory().clear();
			if(invs.containsKey(player))
				player.getInventory().setContents(invs.get(player));
			
			player.sendMessage(Util.formatMessage("You have left the maze lobby"));
			return true;
		} else if(plugin.arena.playing.contains(player)){
			if(plugin.arena.playing.isEmpty()){
				plugin.arena.setPlaying(false);
				Bukkit.broadcastMessage(Util.formatBroadcast("Maze game forfited, all players left!"));
			}
			
			plugin.arena.playing.remove(player);
			player.teleport(player.getWorld().getSpawnLocation());
			
			player.getInventory().clear();
			if(invs.containsKey(player))
				player.getInventory().setContents(invs.get(player));
			
			player.sendMessage(Util.formatMessage("You have left the maze"));
			return false;
		} else {
			player.teleport(player.getWorld().getSpawnLocation());
			player.sendMessage(Util.formatMessage("You are not in the maze"));
			return true;
		}
	}
	
	

}
