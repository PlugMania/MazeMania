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

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import info.plugmania.mazemania.ConfigUtil;
import info.plugmania.mazemania.MazeMania;

public class TriggerManager {
	MazeMania plugin;
	public TriggerManager(MazeMania instance) {
		plugin = instance;
	}
	
	private ArrayList<Trigger> triggerList=new ArrayList<Trigger>();
	
	public void addTrigger(Trigger t){
		triggerList.add(t);
		plugin.arena.dbConf.createSection("triggers");
		plugin.arena.dbConf.set("triggers", saveTriggers(plugin.arena.dbConf.getConfigurationSection("triggers")));
		ConfigUtil.saveConfig(plugin.arena.dbConf, "db");
	}
	
	public Trigger getTrigger(Material block){
		for(Trigger t:triggerList){
			if(t.blockID==block.getId()){
				return t;
			}
		}
		return null;
	}
	
	public void applyTrigger(Material block,MazeMania instance,Player p){
		try{
			for(Trigger t:triggerList){
				if(t.blockID==block.getId()){
					t.apply(p, instance);
				}
			}
			}catch(Exception ex){
				
			}
	}
	
	public void removeTrigger(Material block){
		try{
		for(Trigger t:triggerList){
			if(t.blockID==block.getId()){
				triggerList.remove(t);
			}
		}
		}catch(Exception ex){
			
		}
	}
	
	public boolean isTrigger(Material block){
		for(Trigger t:triggerList){
			if(t.blockID==block.getId()){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Trigger> getTriggers(){
		return triggerList;
	}
	
	public ConfigurationSection saveTriggers(ConfigurationSection csec){
		for (Trigger t:triggerList){
			csec.createSection(String.valueOf(t.blockID));
			csec.set(String.valueOf(t.blockID), t.asConfigSection(csec.getConfigurationSection(String.valueOf(t.blockID))));
		}
		return csec;
	}
	
	public void loadTriggers(ConfigurationSection csec){
		for(String s:csec.getKeys(false)){
			triggerList.add(new Trigger(csec.getConfigurationSection(s)));
		}
	}
}
