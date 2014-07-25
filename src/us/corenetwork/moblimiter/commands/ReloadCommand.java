package us.corenetwork.moblimiter.commands;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;

import us.corenetwork.moblimiter.CreatureGroupSettings;
import us.corenetwork.moblimiter.IO;
import us.corenetwork.moblimiter.MobLimiter;
import us.corenetwork.moblimiter.OldMobKiller;
import us.corenetwork.moblimiter.Util;

public class ReloadCommand extends BaseCommand {
	
	public ReloadCommand()
	{
		desc = "Reload config";
		needPlayer = false;
		permission = "reload";
	}


	public void run(CommandSender sender, String[] args) {	
		
		IO.LoadSettings();
		
		CreatureGroupSettings.init();
		OldMobKiller.loadConfig();
		
		//Remove existing count commands
		Iterator<Entry<String, BaseCommand>> commandIterator = MobLimiter.commands.entrySet().iterator();
		while (commandIterator.hasNext())
		{
			Entry<String, BaseCommand> commandEntry = commandIterator.next();
			if (commandEntry.getValue() instanceof CountCommand)
				commandIterator.remove();
		}
		
		for (Map.Entry<String, CreatureGroupSettings> g : CreatureGroupSettings.getGroups())
		{
			MobLimiter.commands.put(g.getKey().toLowerCase(), new CountCommand(g.getKey()));
		}

		
		Util.Message("Moblimiter config reloaded", sender);
	}
	

}
