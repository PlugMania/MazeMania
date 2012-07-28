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

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerStore {
	public Inventory inv = null;
	public ItemStack[] armour = null;

	public HashMap<Location, Inventory> chests = new HashMap<Location, Inventory>();
public ItemStack[] openChest;
	public Location previousLoc = null;

	public GameMode gm;

	public int health;
	public int hunger;
}
