package info.plugmania.mazemania.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;
import info.plugmania.mazemania.helpers.PlayerStore;

public class ArenaCommand {

	MazeMania plugin;

	public ArenaCommand(MazeMania instance) {
		plugin = instance;
	}

	public int scheduleId;
	public int scheduleMobId;
	public boolean scheduleActive;

	public boolean joinHandle(Player player) {

		if(plugin.mainConf.getInt("maximumPlayers", 0) != 0 && plugin.arena.waiting.size() >= plugin.mainConf.getInt("maximumPlayers", 0)){
			player.sendMessage(Util.formatMessage("Only " + plugin.mainConf.getInt("maximumPlayers", 0) + " players allowed at a time."));
			return true;
		}

		if (plugin.arena.gameActive) {
			if (plugin.mainConf.getBoolean("joinWhenever")) {
				if(plugin.mainConf.getInt("maximumPlayers", 0) != 0 && plugin.arena.playing.size() >= plugin.mainConf.getInt("maximumPlayers", 0)){
					player.sendMessage(Util.formatMessage("Only " + plugin.mainConf.getInt("maximumPlayers", 0) + " players allowed at a time."));
					return true;
				}
				joinMatch(player);
				player.sendMessage(Util.formatMessage("You have joined the MazeMania game!"));
			} else {
				player.sendMessage(Util.formatMessage("The MazeMania game is currently in play, please wait."));
			}
			return true;
		}

		if (plugin.arena.waiting.isEmpty()) {
			startMatch();
		}

		plugin.arena.waiting.add(player);
		player.sendMessage(Util.formatMessage("You have been added to the MazeMania game waiting list, the game will begin shortly!"));

		return true;
	}

	public boolean leaveHandle(Player player) {
		if (plugin.arena.playing.contains(player)) {
			leaveMatch(player);
			plugin.arena.playing.remove(player);
			player.sendMessage(Util.formatMessage("You have left the MazeMania game."));
			Bukkit.broadcastMessage(Util.formatBroadcast(player.getName() + " has left the maze!"));
			if (plugin.arena.playing.isEmpty()) {
				plugin.arena.gameActive = false;
				Bukkit.broadcastMessage(Util.formatBroadcast("The MazeMania game was forfeited, all players left!"));
			}
			return true;
		} else if (plugin.arena.waiting.contains(player)) {
			plugin.arena.waiting.remove(player);
			player.sendMessage(Util.formatMessage("You are no longer on the MazeMania waiting list."));
			if (plugin.arena.waiting.isEmpty() && scheduleActive) {
				Bukkit.getScheduler().cancelTask(scheduleId);
				Bukkit.broadcastMessage(Util.formatBroadcast("MazeMania game cancelled, all waiting players left"));
			}
			return true;
		} else {
			player.sendMessage(Util.formatMessage("You are not in the MazeMania game."));
			return true;
		}
	}

	public void leaveMatch(Player player) {
		if (plugin.arena.playing.contains(player)) {

			player.getInventory().clear();
			player.setSneaking(false);

			Location back = null;
			if (plugin.arena.store.containsKey(player)) {
				PlayerStore ps = plugin.arena.store.get(player);

				player.getInventory().setContents(ps.inv.getContents());
				back = ps.previousLoc;
				player.setGameMode(ps.gm);
				player.setFoodLevel(ps.hunger);
				player.setHealth(ps.health);
				player.getInventory().setArmorContents(ps.armour);
			}

			if (back == null) {
				player.sendMessage(Util.formatMessage("Your previous location was not found."));
				player.teleport(player.getWorld().getSpawnLocation());
			} else {
				player.teleport(back);
			}
		}
	}

	private void startMatch() {
		int tOut = plugin.mainConf.getInt("waitingDelay", 20);
		if (tOut < 1) tOut = 1;

		int minP = plugin.mainConf.getInt("minimumPlayers", 2);
		if (minP < 2) minP = 2;
		if (plugin.debug) minP = 1;
		final int minPlayers = minP;

		Bukkit.broadcastMessage(Util.formatBroadcast("The MazeMania game will start in " + tOut + " seconds!"));

		scheduleActive = true;
		scheduleId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {
				Location spawn = plugin.arena.getSpawn();
				if (spawn == null) {
					Bukkit.broadcastMessage(Util.formatBroadcast("MazeMania spawn location not set!"));
					return;
				}
				if (plugin.arena.waiting.size() < minPlayers) {
					Bukkit.broadcastMessage(Util.formatBroadcast("Not enough players to start MazeMania!"));
					Bukkit.broadcastMessage(Util.formatBroadcast(plugin.arena.waiting.size() + " players waiting, " + minPlayers + " minimum."));
					startMatch();
					return;
				}

				for (Entity e : spawn.getWorld().getEntities()) {
					if (!e.getType().equals(EntityType.ZOMBIE)) continue;
					if (!plugin.arena.isInArena(e.getLocation())) continue;
					e.remove();
				}

				plugin.arena.playing.clear();
				plugin.arena.store.clear();
				plugin.arena.gameActive = true;
				scheduleActive = false;
				for (Player p : plugin.arena.waiting) {
					joinMatch(p);
				}
				plugin.arena.waiting.clear();
				Bukkit.broadcastMessage(Util.formatBroadcast("The MazeMania game has begun!"));
			}

		}, tOut * 20L);

		int mOut = plugin.mainConf.getInt("mobDelay", 7);
		if (mOut < 1) mOut = 1;
		scheduleMobId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {
				if (!plugin.arena.gameActive) {
					Bukkit.getScheduler().cancelTask(scheduleMobId);
					return;
				}
				for (Player p : plugin.arena.playing) {
					Location l = plugin.arena.getRandomLocation(p.getLocation(), 5);
					p.getWorld().spawnCreature(l, EntityType.ZOMBIE);
				}
			}

		}, mOut * 20L);
	}

	private void joinMatch(Player p) {
		plugin.arena.store.put(p, new PlayerStore());

		Inventory inv = Bukkit.createInventory(null, p.getInventory().getSize());
		inv.setContents(p.getInventory().getContents());

		PlayerStore ps = plugin.arena.store.get(p);
		ps.inv = inv;
		ps.gm = p.getGameMode();
		ps.previousLoc = p.getLocation();
		ps.health = p.getHealth();
		ps.hunger = p.getFoodLevel();
		ps.armour = p.getInventory().getArmorContents();

		p.getInventory().clear();
		p.getInventory().setArmorContents(null);

		plugin.arena.playing.add(p);

		p.setHealth(p.getMaxHealth());
		p.setFoodLevel(p.getMaxHealth());
		p.setGameMode(GameMode.SURVIVAL);
		p.setSneaking(true);

		if (plugin.mainConf.get("noDamageDelay") != null
				&& plugin.mainConf.getInt("noDamageDelay", 0) != 0) {
			p.setNoDamageTicks(plugin.mainConf.getInt("noDamageDelay") * 20);
		}

		if (plugin.mainConf.getBoolean("randomSpawn", true)) {
			p.teleport(plugin.arena.getRandomSpawn());
		} else {
			Location spawn = plugin.arena.getSpawn();
			if (spawn == null) {
				return;
			}
			p.teleport(spawn);
		}
	}
	

}
