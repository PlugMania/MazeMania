package info.plugmania.mazemania.helpers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import info.plugmania.mazemania.ConfigUtil;
import info.plugmania.mazemania.MazeMania;

public class Arena {

	private boolean isWaiting = false;
	private boolean isPlaying = false;
	
	public List<Player> playing = new ArrayList<Player>();
	public List<Player> waiting = new ArrayList<Player>();
	
	private Location higherPos;
	private Location lowerPos;
	
	MazeMania plugin;
	public Arena(MazeMania instance) {
		plugin = instance;
		
		updatePosLocs();
	}
	
	public boolean isWaiting(){
		return isWaiting;
	}
	
	public boolean isPlaying(){
		return isPlaying;
	}
	
	public void setWaiting(boolean val){
		if(val && !isWaiting){
			isWaiting = true;
		} else {
			isWaiting = false;
		}
	}
	
	public void setPlaying(boolean val){
		if(val){
			isWaiting = false;
			isPlaying = true;
		} else {
			isPlaying = false;
		}
	}
	
	private void updatePosLocs(){
		String pos1 = plugin.mainConf.getString("arena.pos1");
		if(pos1 == null) return;
		String[] pos1Ar = pos1.split(":");
		World pos1w = Bukkit.getWorld(pos1Ar[0]);
		Location pos1Loc;
		
		if(pos1Ar.length != 4) return;
		if(pos1w == null) return;
		pos1Loc = new Location(pos1w, Integer.parseInt(pos1Ar[1]), Integer.parseInt(pos1Ar[2]), Integer.parseInt(pos1Ar[3]));
		
		String pos2 = plugin.mainConf.getString("arena.pos2");
		if(pos2 == null) return;
		String[] pos2Ar = pos1.split(":");
		World pos2w = Bukkit.getWorld(pos1Ar[0]);
		Location pos2Loc;
		
		if(pos2Ar.length != 4) return;
		if(pos2w == null) return;
		pos2Loc = new Location(pos2w, Integer.parseInt(pos2Ar[1]), Integer.parseInt(pos2Ar[2]), Integer.parseInt(pos2Ar[3]));
		
		int plx = 0, ply = 0, plz = 0, phx = 0, phy = 0, phz = 0;
		if(pos1Loc.getBlockX() > pos2Loc.getBlockX()){
			phx = pos1Loc.getBlockX();
			plx = pos2Loc.getBlockX();
		}
		if(pos1Loc.getBlockY() > pos2Loc.getBlockY()){
			phy = pos1Loc.getBlockY();
			ply = pos2Loc.getBlockY();
		}
		if(pos1Loc.getBlockZ() > pos2Loc.getBlockZ()){
			phz = pos1Loc.getBlockZ();
			plz = pos2Loc.getBlockZ();
		}
		
		Location higher = new Location(pos1Loc.getWorld(), phx, phy, phz);
		Location lower = new Location(pos1Loc.getWorld(), plx, ply, plz);
		higherPos = higher;
		lowerPos = lower;
	}
	
	public void setPos1(Location loc){
		int blockX = loc.getBlockX();
		int blockY = loc.getBlockY();
		int blockZ = loc.getBlockZ();
		plugin.mainConf.set("arena.pos1", loc.getWorld().getName() + ":" + blockX + ":" + blockY + ":" + blockZ);
		ConfigUtil.saveConfig(plugin.mainConf, "config");
		updatePosLocs();
	}
	
	public void setPos2(Location loc){
		int blockX = loc.getBlockX();
		int blockY = loc.getBlockY();
		int blockZ = loc.getBlockZ();
		plugin.mainConf.set("arena.pos2", loc.getWorld().getName() + ":" + blockX + ":" + blockY + ":" + blockZ);
		ConfigUtil.saveConfig(plugin.mainConf, "config");
		updatePosLocs();
	}
	
	public void setLobby(Location loc){
		int blockX = loc.getBlockX();
		int blockY = loc.getBlockY();
		int blockZ = loc.getBlockZ();
		plugin.mainConf.set("arena.lobby", loc.getWorld().getName() + ":" + blockX + ":" + blockY + ":" + blockZ);
		ConfigUtil.saveConfig(plugin.mainConf, "config");
	}
	
	public void setSpawn(Location loc){
		int blockX = loc.getBlockX();
		int blockY = loc.getBlockY();
		int blockZ = loc.getBlockZ();
		plugin.mainConf.set("arena.spawn", loc.getWorld().getName() + ":" + blockX + ":" + blockY + ":" + blockZ);
		ConfigUtil.saveConfig(plugin.mainConf, "config");
	}
	
	public Location getLowerPos(){
		return lowerPos;
	}
	
	public Location getHigherPos(){
		return higherPos;
	}
	
	public Location getLobby(){
		String lobby = plugin.mainConf.getString("arena.lobby");
		String[] lobbyAr = lobby.split(":");
		World lobbyW = Bukkit.getWorld(lobbyAr[0]);
		
		if(lobbyAr.length != 4) return null;
		if(lobbyAr == null) return null;
		return new Location(lobbyW, Integer.parseInt(lobbyAr[1]), Integer.parseInt(lobbyAr[2]), Integer.parseInt(lobbyAr[3]));
	}
	
	public Location getSpawn(){
		String spawn = plugin.mainConf.getString("arena.spawn");
		String[] spawnAr = spawn.split(":");
		World spawnW = Bukkit.getWorld(spawnAr[0]);
		
		if(spawnAr.length != 4) return null;
		if(spawnAr == null) return null;
		return new Location(spawnW, Integer.parseInt(spawnAr[1]), Integer.parseInt(spawnAr[2]), Integer.parseInt(spawnAr[3]));
	}
}
