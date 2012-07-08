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
			if(t.blockID==block){
				return t;
			}
		}
		return null;
	}
	
	public void removeTrigger(Material block){
		for(Trigger t:triggerList){
			if(t.blockID==block){
				triggerList.remove(t);
			}
		}
	}
	
	public boolean isATrigger(Material block){
		for(Trigger t:triggerList){
			if(t.blockID==block){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Trigger> getTriggers(){
		return triggerList;
	}
}
