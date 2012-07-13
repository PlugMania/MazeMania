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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import info.plugmania.mazemania.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Trigger {
	public int blockID;
	public String effect;
	public String arguments;

	public Trigger(int ID, String e,String args){
		blockID=ID;
		effect=e;
		arguments=args;
	}


	public void apply(Player p){
		System.out.println("applying effect: '" + effect + " " + arguments + "'");
		try {
			for(Method m : this.getClass().getMethods()) {
				System.out.println("method loop " + m.getName());
				if(m.getName().equalsIgnoreCase(effect)) {
					System.out.println("m=effect " + m.getAnnotations().length);
					for(Annotation a:m.getAnnotations()) {
						System.out.println("annotation loop");
						if(a instanceof effect){
							System.out.println("matched.");
							if(arguments==null) arguments=((effect) a).argument();
							m.invoke(this, new Object[]{p,arguments});
						}
					}
				}
			}
		} catch(Exception e) {
			Util.debug("Error in Trigger reflection: " + e.getMessage());
			//e.printStackTrace();
		}
	}

	@effect public void poison(Player p,String args){
		p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4 * 20, 1));
	}

	@effect public void lightning(Player p,String args){
		p.getLocation().getWorld().strikeLightningEffect(p.getLocation());
		p.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 5, 1));
	}

	@effect(argument="4") public void fire(Player p,String args){
		p.setFireTicks(Integer.parseInt(args) * 20);
	}

	@effect(argument="2") public void damage(Player p,String args){
		p.damage(Integer.parseInt(args));
	}

	@effect(argument="2") public void heal(Player p,String args){
		p.setHealth(p.getHealth()+Integer.parseInt(args));
	}

	@effect public void kill(Player p,String args){
		//a bit over the top?
	}


	public @interface effect{
		  String argument() default "";
	}
}
