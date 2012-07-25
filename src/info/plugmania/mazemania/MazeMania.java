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

package info.plugmania.mazemania;

import info.plugmania.mazemania.commands.MazeCommand;
import info.plugmania.mazemania.helpers.Arena;
import info.plugmania.mazemania.helpers.Econ;
import info.plugmania.mazemania.helpers.Reward;
import info.plugmania.mazemania.helpers.Triggers;
import info.plugmania.mazemania.helpers.TriggerManager;
import info.plugmania.mazemania.listeners.PlayerListener;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MazeMania extends JavaPlugin {
	public YamlConfiguration mainConf;

	public boolean debug = false;

	private final ConfigUtil configUtil;
	public final Util util;

	private String confVersion = "1.4.0";

	public Arena arena;
	public Triggers triggers;
	public Reward reward;
	public Econ econ = null;

	public MazeCommand mazeCommand;
	public TriggerManager TriggerManager;

	public MazeMania() {
		this.configUtil = new ConfigUtil(this);
		this.util = new Util(this);
		this.TriggerManager= new TriggerManager(this);
	}

	public void onDisable() {
		Util.log(Util.pdfFile.getName() + " has been disabled");
	}

	public void onEnable() {
		Util.pdfFile = getDescription();
		Util.log("----------- " + Util.pdfFile.getName() + " has been enabled" + " -----------");
		Util.log(Util.pdfFile.getName() + " Version " + Util.pdfFile.getVersion());
		Util.log(Util.pdfFile.getName() + " By " + (String) Util.pdfFile.getAuthors().get(0));

		ConfigUtil.loadConfig("config", "config");
		this.mainConf = ConfigUtil.getConfig("config");

		if (!this.mainConf.getString("version", this.confVersion).equalsIgnoreCase(this.confVersion)) {
			File file = new File(getDataFolder() + File.separator + "config" + ".yml");
			file.delete();
			Util.log("!!Regenerating config, invalid version!!");
			ConfigUtil.loadConfig("config", "config");
			this.mainConf = ConfigUtil.getConfig("config");
		}

		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			Util.log("Vault detected.");
			this.econ = new Econ(this);
		} else {
			Util.log("Vault was not detected");
		}

		arena = new Arena(this);
		triggers = new Triggers(this);
		reward = new Reward(this);

		debug = mainConf.getBoolean("debug", false);

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(this), this);

		registerCommands();

		Util.log("Succefully loaded");
	}

	private void registerCommands() {
		mazeCommand = new MazeCommand(this);
		getCommand("maze").setExecutor(mazeCommand);
	}

	public boolean hasPermission(String name, String perm) {
		Player player = Bukkit.getPlayer(name);
		return hasPermission(player, perm);
	}

	public String basePerm = "mazemania";

	public boolean hasPermission(Player player, String perm) {
		if ((player.hasPermission(basePerm + "." + perm)) || (player.hasPermission(this.basePerm + ".*"))) {
			Util.debug("Has permission for player: " + player.getName() + " and perm: " + this.basePerm + "." + perm);
			return true;
		}
		return false;
	}
}