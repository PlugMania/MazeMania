package info.plugmania.mazemania.listeners;


import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;
import info.plugmania.mazemania.helpers.PlayerStore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class PlayerListener implements Listener {

	MazeMania plugin;
	public PlayerListener(MazeMania instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		if(event.isCancelled()) return;
		if(event.getMessage().startsWith("/maze")) return;
		if(!plugin.arena.playing.contains(event.getPlayer())) return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event){
		if(event.isCancelled()) return;
		if(plugin.arena.isInArena(event.getEntity().getLocation()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		if(!plugin.arena.playing.contains(player)) return;
		
		plugin.arena.playing.remove(player);
		Bukkit.broadcastMessage(Util.formatBroadcast(player.getName() + " has died in the maze!"));
		
		if(plugin.arena.playing.isEmpty()){
			plugin.arena.gameActive = false;
			Bukkit.broadcastMessage(Util.formatBroadcast("The MazeMania game was forfeited, all players left!"));
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		if(!plugin.arena.store.containsKey(player)) return;
		
		player.getInventory().clear();
		
		Location back = null;
		PlayerStore ps = plugin.arena.store.get(player);
			
		player.getInventory().setContents(ps.inv.getContents());
		back = ps.previousLoc;
		player.setGameMode(ps.gm);
		player.setFoodLevel(ps.hunger);
		player.setHealth(ps.health);
		
		if(back == null){
			player.sendMessage(Util.formatMessage("Your previous location was not found."));
			event.setRespawnLocation(player.getWorld().getSpawnLocation());
		} else {
			event.setRespawnLocation(back);
		}
		
		plugin.arena.store.remove(player);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(plugin.arena.playing.contains(player)){
			plugin.mazeCommand.arenaCommand.leaveMatch(player);
			Bukkit.broadcastMessage(Util.formatBroadcast(player.getName() + " has left the maze!"));
			
			plugin.arena.playing.remove(player);
			
			if(plugin.arena.playing.isEmpty()){
				plugin.arena.gameActive = false;
				Bukkit.broadcastMessage(Util.formatBroadcast("The MazeMania game was forfeited, all players left!"));
			}
		}
		if(plugin.arena.waiting.contains(player)){
			plugin.arena.waiting.remove(player);
		}
		if(plugin.arena.waiting.isEmpty() && plugin.mazeCommand.arenaCommand.scheduleActive){
			Bukkit.getScheduler().cancelTask(plugin.mazeCommand.arenaCommand.scheduleId);
			Bukkit.broadcastMessage(Util.formatBroadcast("MazeMania game cancelled, all waiting players left"));
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(plugin.arena.store.containsKey(player)){
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
			} else {
				player.teleport(back);
			}
			plugin.arena.store.remove(player);
		}
		if(plugin.arena.isInArena(player.getLocation())){
			player.teleport(player.getWorld().getSpawnLocation());
		}
	}
	
	@EventHandler
	public void onChestInteract(PlayerInteractEvent event){
		if(event.isCancelled()) return;
		Player player = event.getPlayer();
		if(!plugin.arena.playing.contains(player)) return;
		
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			Block b = event.getClickedBlock();
			if(b.getType().equals(Material.CHEST)){
				event.setCancelled(true);
				
				PlayerStore ps = plugin.arena.store.get(player);
				if(ps.chests.containsKey(b.getLocation())){
					player.openInventory(ps.chests.get(b.getLocation()));
				} else {
					Chest chest = (Chest) b.getState();
					Inventory inv = Bukkit.createInventory(null, chest.getInventory().getSize());
					inv.setContents(chest.getInventory().getContents());
					ps.chests.put(b.getLocation(), inv);
					player.openInventory(ps.chests.get(b.getLocation()));
				}
			}
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event){
		if(event.isCancelled()) return;
		Player player = event.getPlayer();
		if(plugin.arena.playing.contains(player))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		if(event.isCancelled()) return;
		if(event.getFrom().getBlockX() != event.getTo().getBlockX()
				|| event.getFrom().getBlockY() != event.getTo().getBlockY()
				|| event.getFrom().getBlockZ() != event.getTo().getBlockZ()){
			if(!plugin.arena.playing.contains(event.getPlayer())) return;
			if(event.getTo().getBlockX() == plugin.arena.getExit().getBlockX()
					&& event.getTo().getBlockY() == plugin.arena.getExit().getBlockY()
					&& event.getTo().getBlockZ() == plugin.arena.getExit().getBlockZ()){
				Player player = event.getPlayer();
				Inventory inv = player.getInventory();
				
				Material item = Material.getMaterial(plugin.mainConf.getString("itemToCollect", "GOLD_NUGGET"));
				if(item == null) item = Material.GOLD_NUGGET;
				int amount = plugin.mainConf.getInt("itemAmountToCollect", 10);
				if(amount < 1) amount = 1;
				if(inv.contains(item, amount)){
					
					Bukkit.broadcastMessage(Util.formatBroadcast(player.getName() + " has won the maze!"));
					for(Player p : plugin.arena.playing){
						plugin.mazeCommand.arenaCommand.leaveMatch(p);
						plugin.arena.store.remove(p);
						p.sendMessage(Util.formatMessage("Thank you for playing MazeMania."));
					}
					plugin.arena.playing.clear();
					plugin.arena.gameActive = false;
					
				} else {
					player.sendMessage(Util.formatMessage("You found the exit but have not collected enough items!"));
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(event.isCancelled()) return;
		if(!plugin.arena.playing.contains(event.getPlayer())) return;
		if(!plugin.arena.isInArena(event.getBlock().getLocation())) return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlaceBreak(BlockPlaceEvent event){
		if(event.isCancelled()) return;
		if(!plugin.arena.playing.contains(event.getPlayer())) return;
		if(!plugin.arena.isInArena(event.getBlock().getLocation())) return;
		event.setCancelled(true);
	}
	
}
