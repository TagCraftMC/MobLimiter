package us.corenetwork.moblimiter;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import us.corenetwork.moblimiter.commands.BaseCommand;
import us.corenetwork.moblimiter.commands.CountCommand;
import us.corenetwork.moblimiter.commands.HelpCommand;

public class MobLimiter extends JavaPlugin
{

	public static MobLimiter instance;

	public static HashMap<String, BaseCommand> commands = new HashMap<String, BaseCommand>();

	public final WorkerPool pool = new WorkerPool();

	@Override
	public void onEnable()
	{
		instance = this;

		IO.LoadSettings();

		CreatureGroupSettings.init();
		OldMobKiller.init();
		
		commands.put("help", new HelpCommand());
		
		for (Map.Entry<String, CreatureGroupSettings> g : CreatureGroupSettings.getGroups())
		{
			commands.put(g.getKey().toLowerCase(), new CountCommand(g.getKey()));
		}

		pool.start();
	}

	@Override
	public void onDisable()
	{
		for (Chunk c : getServer().getWorlds().get(0).getLoadedChunks())
		{
			CreatureUtil.purgeCreatures(c);
		}
		pool.interrupt();
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if (args.length < 1 || Util.isInteger(args[0]))
			return commands.get("help").execute(sender, args, true);

		BaseCommand cmd = commands.get(args[0]);
		if (cmd != null)
			return cmd.execute(sender, args, true);
		else
			return commands.get("help").execute(sender, args, true);
	}
}
