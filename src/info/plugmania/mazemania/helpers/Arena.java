/*
    MazeMania; a minecraft bukkit plugin for managing a maze as an arena.
    Copyright (C) 2012 Plugmania (Sorroko,korikisulda) and contributors.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package info.plugmania.mazemania.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import info.plugmania.mazemania.ConfigUtil;
import info.plugmania.mazemania.MazeMania;

public class Arena {
	public List<Player> playing = new ArrayList<Player>();
	public List<Player> waiting = new ArrayList<Player>();

	public boolean gameActive = false;

	public HashMap<Player, PlayerStore> store = new HashMap<Player, PlayerStore>();

	private Location higherPos;
	private Location lowerPos;

	public YamlConfiguration dbConf;

	MazeMania plugin;

	public Arena(MazeMania instance) {
		plugin = instance;

		ConfigUtil.loadConfig("db");
		dbConf = ConfigUtil.getConfig("db");

		updatePosLocs();
		
		if(dbConf.isSet("triggers")) plugin.TriggerManager.loadTriggers(dbConf.getConfigurationSection("triggers"));
	}

	private void updatePosLocs() {
		String pos1 = dbConf.getString("arena.pos1");
		if (pos1 == null) return;
		String[] pos1Ar = pos1.split(":");
		World pos1w = Bukkit.getWorld(pos1Ar[0]);
		Location pos1Loc;

		if (pos1Ar.length != 4) return;
		if (pos1w == null) return;
		pos1Loc = new Location(pos1w, Integer.parseInt(pos1Ar[1]), Integer.parseInt(pos1Ar[2]), Integer.parseInt(pos1Ar[3]));

		String pos2 = dbConf.getString("arena.pos2");
		if (pos2 == null) return;
		String[] pos2Ar = pos2.split(":");
		World pos2w = Bukkit.getWorld(pos1Ar[0]);
		Location pos2Loc;

		if (pos2Ar.length != 4) return;
		if (pos2w == null) return;
		pos2Loc = new Location(pos2w, Integer.parseInt(pos2Ar[1]), Integer.parseInt(pos2Ar[2]), Integer.parseInt(pos2Ar[3]));

		int plx = 0, ply = 0, plz = 0, phx = 0, phy = 0, phz = 0;
		if (pos1Loc.getBlockX() > pos2Loc.getBlockX()) {
			phx = pos1Loc.getBlockX();
			plx = pos2Loc.getBlockX();
		} else {
			phx = pos2Loc.getBlockX();
			plx = pos1Loc.getBlockX();
		}
		if (pos1Loc.getBlockY() > pos2Loc.getBlockY()) {
			phy = pos1Loc.getBlockY();
			ply = pos2Loc.getBlockY();
		} else {
			phy = pos2Loc.getBlockY();
			ply = pos1Loc.getBlockY();
		}
		if (pos1Loc.getBlockZ() > pos2Loc.getBlockZ()) {
			phz = pos1Loc.getBlockZ();
			plz = pos2Loc.getBlockZ();
		} else {
			phz = pos2Loc.getBlockZ();
			plz = pos1Loc.getBlockZ();
		}


		Location higher = new Location(pos1Loc.getWorld(), phx, phy, phz);
		Location lower = new Location(pos1Loc.getWorld(), plx, ply, plz);
		higherPos = higher;
		lowerPos = lower;
	}

	public void setPos1(Location loc) {
		int blockX = loc.getBlockX();
		int blockY = loc.getBlockY();
		int blockZ = loc.getBlockZ();
		dbConf.set("arena.pos1", loc.getWorld().getName() + ":" + blockX + ":" + blockY + ":" + blockZ);
		ConfigUtil.saveConfig(dbConf, "db");
		updatePosLocs();
	}

	public void setPos2(Location loc) {
		int blockX = loc.getBlockX();
		int blockY = loc.getBlockY();
		int blockZ = loc.getBlockZ();
		dbConf.set("arena.pos2", loc.getWorld().getName() + ":" + blockX + ":" + blockY + ":" + blockZ);
		ConfigUtil.saveConfig(dbConf, "db");
		updatePosLocs();
	}

	public void setSpawn(Location loc) {
		int blockX = loc.getBlockX();
		int blockY = loc.getBlockY();
		int blockZ = loc.getBlockZ();
		dbConf.set("arena.spawn", loc.getWorld().getName() + ":" + blockX + ":" + blockY + ":" + blockZ);
		ConfigUtil.saveConfig(dbConf, "db");
	}

	public void setExit(Location loc) {
		int blockX = loc.getBlockX();
		int blockY = loc.getBlockY();
		int blockZ = loc.getBlockZ();
		dbConf.set("arena.exit", loc.getWorld().getName() + ":" + blockX + ":" + blockY + ":" + blockZ);
		ConfigUtil.saveConfig(dbConf, "db");
	}

	public boolean isInArena(Location loc) {
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		if (lowerPos == null || higherPos == null) return false;
		if (x >= lowerPos.getBlockX() && x <= higherPos.getBlockX()
				&& y >= lowerPos.getBlockY() && y <= higherPos.getBlockY()
				&& z >= lowerPos.getBlockZ() && z <= higherPos.getBlockZ()) {
			return true;
		}
		return false;
	}

	public Location getLowerPos() {
		return lowerPos;
	}

	public Location getHigherPos() {
		return higherPos;
	}

	public Location getRandomSpawn() {
		Location s = getSpawn();
		int d = 10;
		double dist;
		long deg;
		double x, z;
		Block b;
		boolean finish = false;
		;
		do {
			dist = Math.random() * d;
			deg = Math.round(Math.random() * 360);
			x = s.getBlockX() + (dist - d) * Math.cos(deg);
			z = s.getBlockZ() + (dist - d) * Math.cos(deg);
			b = s.getWorld().getBlockAt((int) x, s.getBlockY(), (int) z);
			if (b.getType().equals(Material.AIR)
					&& b.getRelative(BlockFace.UP).getType().equals(Material.AIR)
					&& isInArena(b.getLocation())) {
				finish = true;
			}
		} while (!finish);

		return b.getLocation().add(0.5, 0, 0.5);
	}

	public Location getRandomLocation(Location s, int d) {
		double dist;
		long deg;
		double x, z;
		Block b;
		boolean finish = false;
		;
		do {
			dist = Math.random() * d;
			deg = Math.round(Math.random() * 360);
			x = s.getBlockX() + (dist - d) * Math.cos(deg);
			z = s.getBlockZ() + (dist - d) * Math.cos(deg);
			b = s.getWorld().getBlockAt((int) x, s.getBlockY(), (int) z);
			if (b.getType().equals(Material.AIR)
					&& b.getRelative(BlockFace.UP).getType().equals(Material.AIR)
					&& isInArena(b.getLocation())) {
				finish = true;
			}
		} while (!finish);

		return b.getLocation().add(0.5, 0, 0.5);
	}

	public Location getSpawn() {
		String spawn = dbConf.getString("arena.spawn");
		String[] spawnAr = spawn.split(":");
		World spawnW = Bukkit.getWorld(spawnAr[0]);

		if (spawnAr.length != 4) return null;
		return new Location(spawnW, Integer.parseInt(spawnAr[1]), Integer.parseInt(spawnAr[2]), Integer.parseInt(spawnAr[3]));
	}

	public Location getExit() {
		try{
		String exit = dbConf.getString("arena.exit");
		String[] exitAr = exit.split(":");
		if (exitAr.length != 4) return new Location(plugin.getServer().getWorlds().get(0),0,0,0);
		World exitW = Bukkit.getWorld(exitAr[0]);
		return new Location(exitW, Integer.parseInt(exitAr[1]), Integer.parseInt(exitAr[2]), Integer.parseInt(exitAr[3]));
		}catch(Exception ex){
			return new Location(plugin.getServer().getWorlds().get(0),0,0,0);
	}
		
}
}