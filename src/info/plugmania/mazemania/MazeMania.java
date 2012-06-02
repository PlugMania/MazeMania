/*
 * TODO
 * Chests
 * Triggers, lightning (in essence a couple of effects)
 * Winning prizes/a way of winning
 * Mobs in arena
 * Leaderboard/Scores
 */

package info.plugmania.mazemania;

import info.plugmania.mazemania.commands.MazeCommand;
import info.plugmania.mazemania.helpers.Arena;
import info.plugmania.mazemania.listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class MazeMania extends JavaPlugin {
	public YamlConfiguration mainConf;
	
	public boolean debug = false;
	
	private final ConfigUtil configUtil;
	private final Util util;
	
	public Arena arena;
	
	public MazeMania() {	
        configUtil = new ConfigUtil(this);
        util = new Util(this);
    }
	
	@Override
	public void onDisable() {
		Util.log(Util.pdfFile.getName() + " has been disabled");
		
	}
	
	@Override
	public void onEnable() { //enable
		Util.pdfFile = getDescription();
		Util.log("----------- " + Util.pdfFile.getName()  + " has been enabled" + " -----------");
		Util.log(Util.pdfFile.getName() + " Version " + Util.pdfFile.getVersion());
		Util.log(Util.pdfFile.getName() + " By " + Util.pdfFile.getAuthors().get(0));
		
		ConfigUtil.loadConfig("config", "config");
		mainConf = ConfigUtil.getConfig("config");
		
        arena = new Arena(this); //load after config
		
		debug = mainConf.getBoolean("debug", false);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(this), this);
		
		registerCommands();
		
		Util.log("Succefully loaded");
	}
	public MazeCommand mazeCommand;
	private void registerCommands(){
		mazeCommand = new MazeCommand(this);
		getCommand("maze").setExecutor(mazeCommand);
	}

	private String basePerm = "mazemania";
	
	public boolean hasPermission(String name, String perm) {
		Player player = Bukkit.getPlayer(name);
		return hasPermission(player, perm);
	}
	public boolean hasPermission(Player player, String perm) {
		if(player.hasPermission(basePerm + "." + perm) || player.hasPermission(basePerm + ".*")){
			Util.debug("Has permission for player: " + player.getName() + " and perm: " + basePerm + "." + perm);
			return true;
		}
		return false;
	}
}

