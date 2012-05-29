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
		Location spawn = plugin.arena.getSpawn();
		if(spawn == null){
			sender.sendMessage(Util.formatMessage("Spawn position not set."));
			return true;
		}
		
		plugin.arena.playing.clear();
		plugin.arena.playing.addAll(plugin.arena.waiting);
		plugin.arena.waiting.clear();

		for(Player p : plugin.arena.playing){
			p.teleport(spawn);
			p.sendMessage(Util.formatMessage("The maze arena has started!"));
		}
		return false;
	}
	
	

}
