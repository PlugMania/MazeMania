package info.plugmania.mazemania.helpers;

import info.plugmania.mazemania.ConfigUtil;
import info.plugmania.mazemania.MazeMania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Triggers {

	public HashMap<String, List<String>> triggers = new HashMap<String, List<String>>();

	public YamlConfiguration trigConf;

	MazeMania plugin;

	public Triggers(MazeMania instance) {
		plugin = instance;

		ConfigUtil.loadConfig("triggers");
		trigConf = ConfigUtil.getConfig("triggers");

		if (trigConf.isSet("triggers")) {
			ConfigurationSection trigSec = trigConf.getConfigurationSection("triggers");
			for (String s : trigSec.getKeys(false)) {
				List<String> events = trigConf.getStringList("triggers." + s + ".events");
				if (events == null) events = new ArrayList<String>();

				triggers.put(s, events);
			}
		}
	}

	public void handle(Location loc, Player player) {
		String uid = loc.getWorld().getName() + loc.getBlockX() + "" + loc.getBlockY() + "" + loc.getBlockZ();
		if (!triggers.containsKey(uid)) return;

		List<String> events = triggers.get(uid);
		for (String s : events) {
			String[] ar = s.split(":");
			if (ar[0].equalsIgnoreCase("lightning")) {
				lightningHandle(loc, player);
			} else if (ar[0].equalsIgnoreCase("fire")) {
				fireHandle(loc, player);
			} else if (ar[0].equalsIgnoreCase("poison")) {
				poisonHandle(loc, player);
			} else if (ar[0].equalsIgnoreCase("mob")) {
				mobHandle(loc, player);
			}
		}
	}

	private void lightningHandle(Location loc, Player player) {
		loc.getWorld().strikeLightningEffect(loc);
		player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 5, 1));
	}

	private void fireHandle(Location loc, Player player) {
		player.setFireTicks(4 * 20);
	}

	private void poisonHandle(Location loc, Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4 * 20, 1));
	}

	private void mobHandle(Location loc, Player player) {
		loc.getWorld().spawnCreature(loc, EntityType.ZOMBIE);
	}
}
