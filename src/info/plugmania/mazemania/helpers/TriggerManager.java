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

import info.plugmania.mazemania.MazeMania;

public class TriggerManager {
	MazeMania plugin;
	public TriggerManager(MazeMania instance) {
		plugin = instance;
	}
	
	private ArrayList<Trigger> triggerList=new ArrayList<Trigger>();
	
	public void addTrigger(Trigger t){
		triggerList.add(t);
	}
	
	public Trigger getTrigger(Material block){
		for(Trigger t:triggerList){
			if(t.blockID==block.getId()){
				return t;
			}
		}
		return null;
	}
	
	public void removeTrigger(Material block){
		for(Trigger t:triggerList){
			if(t.blockID==block.getId()){
				triggerList.remove(t);
			}
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
}
