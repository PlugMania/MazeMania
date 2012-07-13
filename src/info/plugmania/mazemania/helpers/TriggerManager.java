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
			if(t.blockID.getId()==block.getId()){
				return t;
			}
		}
		return null;
	}
	
	public void removeTrigger(Material block){
		for(Trigger t:triggerList){
			if(t.blockID.getId()==block.getId()){
				triggerList.remove(t);
			}
		}
	}
	
	public boolean isTrigger(Material block){
		for(Trigger t:triggerList){
			if(t.blockID.getId()==block.getId()){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Trigger> getTriggers(){
		return triggerList;
	}
}
