package info.plugmania.mazemania.commands;

import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;
import info.plugmania.mazemania.helpers.Trigger;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MazeCommand implements CommandExecutor {

	public SetCommand setCommand;
	public ArenaCommand arenaCommand;

	MazeMania plugin;

	public MazeCommand(MazeMania instance) {
		plugin = instance;

		setCommand = new SetCommand(plugin);
		arenaCommand = new ArenaCommand(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equalsIgnoreCase("maze")) {
			Player player = null;
			if (sender instanceof Player)
				player = (Player) sender;

			if (args.length == 0) {
				sender.sendMessage(Util.formatMessage("---------------- MazeMania Help -----------------"));
				sender.sendMessage(Util.formatMessage("Player Commands:"));
				sender.sendMessage(Util.formatMessage("/maze join  - Join the MazeMania game"));
				sender.sendMessage(Util.formatMessage("/maze leave - Leave the MazeMania game"));
				sender.sendMessage(Util.formatMessage("/maze about - Show MazeMania credits and info"));
			}

			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("set")) {
					if (!(sender instanceof Player)) {
						Util.sendMessageNotPlayer(sender);
						return true;
					}
					if (!plugin.util.hasPermMsg(player, "admin")) return true;
					return setCommand.handle(sender, args);

				} else if (args[0].equalsIgnoreCase("join")) {
					if (!(sender instanceof Player)) {
						Util.sendMessageNotPlayer(sender);
						return true;
					}
					if (!plugin.util.hasPermMsg(player, "use")) return true;
					return arenaCommand.joinHandle(player);

				} else if (args[0].equalsIgnoreCase("leave")) {
					if (!(sender instanceof Player)) {
						Util.sendMessageNotPlayer(sender);
						return true;
					}
					if (!plugin.util.hasPermMsg(player, "use")) return true;
					return arenaCommand.leaveHandle(player);

				} else if (args[0].equalsIgnoreCase("trigger")) {
					if (!plugin.util.hasPermMsg(player, "admin")) return true;
					return setCommand.triggerHandle(sender, args);
					
				} else if (args[0].equalsIgnoreCase("block")) {
					if (!plugin.util.hasPermMsg(player, "admin")) return true;
					if(args.length==1){
						sender.sendMessage("========================");
						sender.sendMessage("Block   Event   Args");
						for (Trigger t:plugin.TriggerManager.getTriggers()){
				
							sender.sendMessage(Material.getMaterial(t.blockID).name() + " " + t.effect + " " + t.arguments);
						}
						sender.sendMessage("=========================");
					}else if(args.length==2){
						plugin.TriggerManager.removeTrigger(Material.getMaterial(args[1]));
					}else if(args.length==3){
						plugin.TriggerManager.addTrigger(new Trigger(Material.getMaterial(args[1]), args[2], ""));
					}else if(args.length>=4){
						plugin.TriggerManager.addTrigger(new Trigger(Material.getMaterial(args[1]), args[2], plugin.util.join(args, " ", 3)));
					}else{
						
					}
				} else if (args[0].equalsIgnoreCase("about") || args[0].equalsIgnoreCase("info")) {
					sender.sendMessage(Util.formatMessage("---------------------- " + Util.pdfFile.getName() + " ----------------------"));
					sender.sendMessage(Util.formatMessage(plugin.getName() + " developed by " + Util.pdfFile.getAuthors().get(0)));
					sender.sendMessage(Util.formatMessage("To view more information visit http://plugmania.github.com/ (<-- You can click it!)"));
				}
			}

			return true;
		}
		return false;
	}

}
