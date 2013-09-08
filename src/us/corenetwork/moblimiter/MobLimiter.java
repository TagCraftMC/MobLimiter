package us.corenetwork.moblimiter;

import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import us.corenetwork.moblimiter.commands.AnimalsCommand;
import us.corenetwork.moblimiter.commands.BaseCommand;
import us.corenetwork.moblimiter.commands.VillagersCommand;

public class MobLimiter extends JavaPlugin implements Listener {

	public static MobLimiter instance;
	
	public HashMap<String, BaseCommand> commands = new HashMap<String, BaseCommand>();
		
	@Override
	public void onEnable() {
		instance = this;
		
		IO.LoadSettings();
		
		CreatureSettingsStorage.init();
		
		this.getServer().getPluginManager().registerEvents(new MobLimiterListener(), this);
		
		commands.put("animals", new AnimalsCommand());
		commands.put("villagers", new VillagersCommand());
	}

	@Override
	public void onDisable() {
		for (Chunk c : getServer().getWorlds().get(0).getLoadedChunks()) {
			CreatureUtil.purgeCreatures(c);
		}
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		BaseCommand cmd = commands.get(command.getName());
		if (cmd != null)
		{
			cmd.execute(sender, args);
			return true;
		}
		
		return false;
	}
}
