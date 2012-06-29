package info.plugmania.mazemania.helpers;

import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

public class Econ {

	public Economy economy = null;

	MazeMania plugin;

	public Econ(MazeMania instance) {
		plugin = instance;

		RegisteredServiceProvider economyProvider = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			this.economy = ((Economy) economyProvider.getProvider());
			Util.log("Vault economy hooked.");
		} else {
			Util.log("Vault economy, failed to hook.");
		}
	}
}