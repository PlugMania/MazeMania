package info.plugmania.mazemania.helpers;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerStore {
	public Inventory inv = null;
	public ItemStack[] armour = null;
	
	public HashMap<Location, Inventory> chests = new HashMap<Location, Inventory>();
	
	public Location previousLoc = null;
	
	public GameMode gm;
	
	public int health;
	public int hunger;
}
