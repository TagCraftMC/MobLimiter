package us.corenetwork.moblimiter;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.corenetwork.moblimiter.commands.CountCommand;
import us.corenetwork.moblimiter.commands.BaseCommand;

import java.util.HashMap;
import java.util.Map;

public class MobLimiter extends JavaPlugin implements Listener
{

	public static MobLimiter instance;

	public HashMap<String, BaseCommand> commands = new HashMap<String, BaseCommand>();

	public final WorkerPool pool = new WorkerPool();

	@Override
	public void onEnable()
	{
		instance = this;

		IO.LoadSettings();

		CreatureGroupSettings.init();

		this.getServer().getPluginManager().registerEvents(new MobLimiterListener(), this);

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
		BaseCommand cmd = commands.get(command.getName());
		if (cmd != null)
		{
			cmd.execute(sender, args);
			return true;
		}

		return false;
	}
}
