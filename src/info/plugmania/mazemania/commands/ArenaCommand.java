package info.plugmania.mazemania.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import sun.swing.plaf.synth.Paint9Painter.PaintType;

import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;
import info.plugmania.mazemania.helpers.PlayerStore;

public class ArenaCommand {
	
	MazeMania plugin;
	public ArenaCommand(MazeMania instance) {
		plugin = instance;
	}
	
	public int scheduleId;
	public boolean scheduleActive;
	
	public boolean joinHandle(Player player) {
		if(plugin.arena.gameActive){
			player.sendMessage(Util.formatMessage("The MazeMania game is currently in play, please wait."));
			return true;
		}
		
		if(plugin.arena.waiting.isEmpty()){
			startMatch();
		}
		
		plugin.arena.waiting.add(player);
		player.sendMessage(Util.formatMessage("You have been added to the MazeMania game waiting list, the game will begin shortly!"));
		
		return true;
	}
	
	public boolean leaveHandle(Player player) {
		if(plugin.arena.playing.contains(player)){
			leaveMatch(player);
			plugin.arena.playing.remove(player);
			player.sendMessage(Util.formatMessage("You have left the MazeMania game."));
			Bukkit.broadcastMessage(Util.formatBroadcast(player.getName() + " has left the maze!"));
			if(plugin.arena.playing.isEmpty()){
				plugin.arena.gameActive = false;
				Bukkit.broadcastMessage(Util.formatBroadcast("The MazeMania game was forfeited, all players left!"));
			}
			return true;
		} else if(plugin.arena.waiting.contains(player)) {
			if(plugin.arena.waiting.isEmpty() && scheduleActive){
				Bukkit.getScheduler().cancelTask(scheduleId);
				Bukkit.broadcastMessage(Util.formatBroadcast("MazeMania game cancelled, all waiting players left"));
			}
			plugin.arena.waiting.remove(player);
			player.sendMessage(Util.formatMessage("You are no longer on the MazeMania waiting list."));
			return true;
		} else {
			player.sendMessage(Util.formatMessage("You are not in the MazeMania game."));
			return true;
		}
	}
	
	public void leaveMatch(Player player){
		if(plugin.arena.playing.contains(player)){
			
			player.getInventory().clear();
			
			Location back = null;
			if(plugin.arena.store.containsKey(player)){
				PlayerStore ps = plugin.arena.store.get(player);
				
				player.getInventory().setContents(ps.inv.getContents());
				back = ps.previousLoc;
				player.setGameMode(ps.gm);
				player.setFoodLevel(ps.hunger);
				player.setHealth(ps.health);
			}
			
			if(back == null){
				player.sendMessage(Util.formatMessage("Your previous location was not found."));
				player.teleport(player.getWorld().getSpawnLocation());
			} else {
				player.teleport(back);
			}
		}
	}
	
	private void startMatch(){
		int tOut = plugin.mainConf.getInt("waitingDelay", 20);
		if(tOut < 1) tOut = 1;
		
		int minP = plugin.mainConf.getInt("minimumPlayers", 2);
		if(minP < 2) minP = 2;
		if(plugin.debug) minP = 1;
		final int minPlayers = minP;
		
		Bukkit.broadcastMessage(Util.formatBroadcast("The MazeMania game will start in " + tOut + " seconds!"));
		
		scheduleActive = true;
		scheduleId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				Location spawn = plugin.arena.getSpawn();
				if(spawn == null){
					Bukkit.broadcastMessage(Util.formatBroadcast("MazeMania spawn location not set!"));
					return;
				}
				if(plugin.arena.waiting.size() < minPlayers){
					Bukkit.broadcastMessage(Util.formatBroadcast("Not enough players to start MazeMania!"));
					Bukkit.broadcastMessage(Util.formatBroadcast(plugin.arena.waiting.size() + " players waiting, " + minPlayers + " minimum."));
					startMatch();
					return;
				}
				plugin.arena.playing.clear();
				plugin.arena.store.clear();
				plugin.arena.gameActive = true;
				scheduleActive = false;
				for(Player p: plugin.arena.waiting){
					
					plugin.arena.store.put(p, new PlayerStore());
					
					Inventory inv = Bukkit.createInventory(null, p.getInventory().getSize());
					inv.setContents(p.getInventory().getContents());
					
					PlayerStore ps = plugin.arena.store.get(p);
					ps.inv = inv;
					ps.gm = p.getGameMode();
					ps.previousLoc = p.getLocation();
					ps.health = p.getHealth();
					ps.hunger = p.getFoodLevel();
					
					p.getInventory().clear();
					
					plugin.arena.playing.add(p);
					
					p.setHealth(p.getMaxHealth());
					p.setFoodLevel(p.getMaxHealth());
					p.setGameMode(GameMode.SURVIVAL);
					
					p.teleport(spawn);
				}
				plugin.arena.waiting.clear();
				Bukkit.broadcastMessage(Util.formatBroadcast("The MazeMania game has begun!"));
			}
			
		}, tOut * 20L);
	}
	
	

}
