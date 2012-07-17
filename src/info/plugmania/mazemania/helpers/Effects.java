package info.plugmania.mazemania.helpers;

import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Effects {
	
	public Effects(){
		
	}
	MazeMania plugin;
public Effects(MazeMania instance){
		plugin=instance;
	}
	public void apply(Player p,String arguments,String effect){
		try {
			for(Method m : this.getClass().getMethods()) {
				if(m.getName().equalsIgnoreCase(effect)) {
					for(Annotation a:m.getAnnotations()) {
						if(a instanceof effect){
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
		//a bit over the top? hell, no!
		p.damage(2000); //only 1000 hearts or so ;)
	}
	
	@effect(argument="5") public void money(Player p,String args){
plugin.reward.rewardPlayerMoney(p,Double.valueOf(args));
	}


	@Retention(RetentionPolicy.RUNTIME) @interface effect{
		  String argument() default "";
	}
}
