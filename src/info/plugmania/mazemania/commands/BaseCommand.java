package info.plugmania.mazemania.commands;


import info.plugmania.mazemania.Lang;
import info.plugmania.mazemania.MazeMania;
import info.plugmania.mazemania.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class BaseCommand implements CommandExecutor {

	private SetCommand setCommand;
	private ArenaCommand arenaCommand;
	
	MazeMania plugin;
	public BaseCommand(MazeMania instance) {
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
				sender.sendMessage(Util.formatMessage("---------------------- " + Util.pdfFile.getName() + " ----------------------"));
				sender.sendMessage(Util.formatMessage(plugin.getName() + Lang._("devBy") + Util.pdfFile.getAuthors().get(0)));
				sender.sendMessage(Util.formatMessage("To view more information visit http://plugmania.github.com/ (<-- You can click it!)"));
			}
			
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("set")){
					return setCommand.handle(sender, args);
				} else if(args[0].equalsIgnoreCase("start")){
					return arenaCommand.startHandle(sender, args);
				}
			}
			
			return true;
		}
		return false;
	}

}
