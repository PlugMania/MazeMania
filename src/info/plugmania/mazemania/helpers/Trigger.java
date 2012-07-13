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
					System.out.println("m=effect");
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

	@effect void poison(Player p,String args){
		p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4 * 20, 1));
	}

	@effect void lightning(Player p,String args){
		p.getLocation().getWorld().strikeLightningEffect(p.getLocation());
		p.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 5, 1));
	}

	@effect(argument="4") void fire(Player p,String args){
		p.setFireTicks(Integer.parseInt(args) * 20);
	}

	@effect(argument="2") void damage(Player p,String args){
		p.damage(Integer.parseInt(args));
	}

	@effect(argument="2") void heal(Player p,String args){
		p.setHealth(p.getHealth()+Integer.parseInt(args));
	}

	@effect void kill(Player p,String args){
		//a bit over the top?
	}


	@interface effect{
		  String argument() default "";
	}
}
