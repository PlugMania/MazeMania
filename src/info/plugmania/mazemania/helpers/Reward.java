package info.plugmania.mazemania.helpers;

import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Reward {

	MazeMania plugin;

	public Reward(MazeMania instance) {
		plugin = instance;
	}

	public void rewardPlayer(Player p) {
		Util.debug("Rewards Econ: " + plugin.mainConf.getBoolean("economy_rewards", true) + " Item: " + plugin.mainConf.getBoolean("item_rewards", true));

		if (plugin.mainConf.getBoolean("economy_rewards", true)) {
			rewardPlayerMoney(p, plugin.mainConf.getDouble("economy_reward_amount", 20.0D));
		}
		if (plugin.mainConf.getBoolean("item_rewards", true)) {
			rewardPlayerItem(p);
		}
		p.sendMessage(Util.formatMessage("You have been rewarded"));
	}

	private void rewardPlayerItem(Player p) {
		List<String> items_list = plugin.mainConf.getStringList("item_reward_list");

		if (items_list == null) return;
		if (items_list.size() == 0) return;

		for (String i : items_list) {
			String[] iAr = i.split(":");
			if (iAr.length == 2) {
				Material mat = Material.getMaterial(iAr[0]);
				if (mat == null) {
					Util.log("Rewards, No such material: " + iAr[0]);
					continue;
				}
				ItemStack s = new ItemStack(mat, Integer.parseInt(iAr[1]));
				p.getInventory().addItem(s);
			}
		}
		p.updateInventory();
	}

	private void rewardPlayerMoney(Player p, double amount) {
		if (plugin.econ == null || plugin.econ.economy == null) {
			p.sendMessage(Util.formatMessage(ChatColor.RED + "Vault not detected, could not reward money"));
			return;
		}
		plugin.econ.economy.depositPlayer(p.getName(), amount);
	}
}