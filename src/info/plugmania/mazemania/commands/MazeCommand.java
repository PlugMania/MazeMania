package info.plugmania.mazemania.commands;

import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;

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
		if(command.getName().equalsIgnoreCase("maze")){
			Player player = null;
			if(sender instanceof Player)
				player = (Player) sender;
			
			if(args.length == 0){
				sender.sendMessage(Util.formatMessage("---------------- MazeMania Help -----------------"));
				sender.sendMessage(Util.formatMessage("Player Commands:"));
				sender.sendMessage(Util.formatMessage("/maze join  - Join the MazeMania game"));
				sender.sendMessage(Util.formatMessage("/maze leave - Leave the MazeMania game"));
				sender.sendMessage(Util.formatMessage("/maze about - Show MazeMania credits and info"));
			}
			
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("set")){
					if(!(sender instanceof Player)){
						Util.sendMessageNotPlayer(sender);
						return true;
					}
					if(!plugin.hasPermission(player, "admin")){
						Util.sendMessageNoPerms(player);
						return true;
					}
					return setCommand.handle(sender, args);
					
				} else if(args[0].equalsIgnoreCase("join")){
					if(!(sender instanceof Player)){
						Util.sendMessageNotPlayer(sender);
						return true;
					}
					if(!plugin.hasPermission(player, "use")){
						Util.sendMessageNoPerms(player);
						return true;
					}
					return arenaCommand.joinHandle(player);
					
				} else if(args[0].equalsIgnoreCase("leave")){
					if(!(sender instanceof Player)){
						Util.sendMessageNotPlayer(sender);
						return true;
					}
					if(!plugin.hasPermission(player, "use")){
						Util.sendMessageNoPerms(player);
						return true;
					}
					return arenaCommand.leaveHandle(player);
					
				} else if(args[0].equalsIgnoreCase("trigger")){
					if(!plugin.hasPermission(player, "admin")){
						Util.sendMessageNoPerms(player);
						return true;
					}
					return setCommand.triggerHandle(sender, args);
					
				} else if(args[0].equalsIgnoreCase("about") || args[0].equalsIgnoreCase("info")){
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
