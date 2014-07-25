package us.corenetwork.moblimiter.commands;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import us.corenetwork.moblimiter.*;

import java.util.HashMap;
import java.util.Map;

public class CountCommand extends BaseCommand
{
	private String group;

	static private HashMap<Player, Cleanup> cleanups = new HashMap<Player, Cleanup>();

	public CountCommand(String group)
	{
		needPlayer = true;
		this.group = group;
		permission = group.toLowerCase();
		desc = "Count " + group;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;

		boolean visualize = args.length > 0 && args[0].equals("show");
		boolean keep = args.length > 1 && args[1].equals("keep");
		boolean clear = args.length > 0 && args[0].equals("hide");

		if (visualize && !clear)
		{
			visualize(player, !keep);
		}
		else if (!clear)
		{
			showCount(player);
		}
		else if (clear)
		{
			cleanup(player);
		}
	}

	private void visualize(Player player, boolean autocleanup)
	{
		cleanup(player);
		Cleanup cleanup;

		CreatureGroupSettings group = CreatureGroupSettings.getGroupSettings(this.group);

		int cx = player.getLocation().getChunk().getX();
		int cz = player.getLocation().getChunk().getZ();

		int drawY = player.getLocation().getBlockY() + 10;

		HashMap<Chunk, Float> limits = new HashMap();

		for (int x = cx - 6; x < cx + 6; x++)
		{
			for (int z = cz - 6; z < cz + 6; z++)
			{
				Chunk chunk = player.getLocation().getWorld().getChunkAt(x, z);
				Entity creatures[] = chunk.getEntities();

				HashMap<EntityType, Integer> count = new HashMap<EntityType, Integer>();

				for (Entity creature : creatures)
				{
					Integer value = count.get(creature.getType());
					if (value == null)
					{
						value = 0;
					}
					value++;
					count.put(creature.getType(), value);
				}

				float max = 0;

				int sum = 0;
				for (Map.Entry<EntityType, Integer> e : count.entrySet())
				{
					CreatureSettings settings = group.getCreatureSettings(e.getKey());
					if (settings != null)
					{
						float full = (float) e.getValue() / (float) settings.getChunkLimit();
						if (full > max)
						{
							max = full;
						}
						sum += e.getValue();
					}
				}

				float full = (float) sum / group.getChunkLimit();

				if (full > max)
				{
					max = full;
				}

				limits.put(chunk, max);

				int id = Settings.getInt(Setting.GRID_NONE_ID);
				int color = Settings.getInt(Setting.GRID_NONE_DATA);
				VisualizeLayout layout = VisualizeLayout.LAYOUT_NONE;

				if (max > 0)
				{
					id = Settings.getInt(Setting.GRID_LOW_ID);
					color = Settings.getInt(Setting.GRID_LOW_DATA);
					layout = VisualizeLayout.LAYOUT_LOW;
				}
				if (max >= 0.8)
				{
					id = Settings.getInt(Setting.GRID_MEDIUM_ID);
					color = Settings.getInt(Setting.GRID_MEDIUM_DATA);
					layout = VisualizeLayout.LAYOUT_MEDIUM;
				}
				if (max >= 0.9)
				{
					id = Settings.getInt(Setting.GRID_HIGH_ID);
					color = Settings.getInt(Setting.GRID_HIGH_DATA);
					layout = VisualizeLayout.LAYOUT_HIGH;
				}
				if (max > 1)
				{
					id = Settings.getInt(Setting.GRID_EXCEED_ID);
					color = Settings.getInt(Setting.GRID_EXCEED_DATA);
					layout = VisualizeLayout.LAYOUT_EXCEED;
				}

				layout.draw(chunk, player, drawY, id, (byte) color);
			}
		}

		cleanup = new Cleanup(cx, cz, drawY, player.getWorld(), player);
		if (autocleanup)
		{
			cleanup.setTaskId(
					Bukkit.getScheduler().scheduleSyncDelayedTask(MobLimiter.instance, cleanup, Settings.getInt(Setting.GRID_DURATION)));
		}
		cleanups.put(player, cleanup);
	}

	private void cleanup(Player player)
	{
		Cleanup cleanup = cleanups.get(player);

		if (cleanup != null)
		{
			if (Bukkit.getScheduler().isQueued(cleanup.getTaskId()))
			{
				Bukkit.getScheduler().cancelTask(cleanup.getTaskId());
				cleanup.fired = true;
				cleanup.run();
			}
		}
	}

	private void showCount(Player player)
	{
		Chunk playerChunk = player.getLocation().getChunk();
		CreatureGroupSettings groupSettings = CreatureGroupSettings.getGroupSettings(group);

		HashMap<CreatureSettings, Integer> perCreatureCountsChunk = new HashMap<CreatureSettings, Integer>();
		int allCountChunk = 0;

		HashMap<CreatureSettings, Integer> perCreatureCountsViewDistance = new HashMap<CreatureSettings, Integer>();
		int allCountViewDistance = 0;

		boolean tooMany = false;

		for (CreatureSettings settings : groupSettings.getCreatures())
		{
			perCreatureCountsChunk.put(settings, 0);
			perCreatureCountsViewDistance.put(settings, 0);
		}

		Iterable<LivingEntity> viewDistanceCreatures = CreatureUtil.getCreaturesInRange(playerChunk);
		Entity[] chunkCreatures = playerChunk.getEntities();

		for (Entity e : chunkCreatures)
		{
			if (e instanceof LivingEntity)
			{
				CreatureSettings creatureSettings = groupSettings.getCreatureSettings(e.getType());
				if (creatureSettings == null)
					continue;

				int curCount = perCreatureCountsChunk.get(creatureSettings);

				allCountChunk++;
				perCreatureCountsChunk.put(creatureSettings, curCount + 1);
				
				if (!tooMany)
				{
					tooMany = curCount > creatureSettings.getChunkLimit() || allCountChunk > groupSettings.getChunkLimit();
				}
			}
		}

		for (LivingEntity c : viewDistanceCreatures)
		{
			CreatureSettings creatureSettings = groupSettings.getCreatureSettings(c.getType());
			if (creatureSettings == null)
				continue;

			int curCount = perCreatureCountsViewDistance.get(creatureSettings);

			allCountViewDistance++;
			perCreatureCountsViewDistance.put(creatureSettings, curCount + 1);

			if (!tooMany)
			{
				tooMany = curCount > creatureSettings.getViewDistanceLimit() || allCountViewDistance > groupSettings.getViewDistanceLimit();
			}
		}

		messageCount(player, groupSettings.getName(), allCountChunk, groupSettings.getChunkLimit(), allCountViewDistance, groupSettings.getViewDistanceLimit());

		for (CreatureSettings settings : groupSettings.getCreatures())
		{
			if(settings.isListed())
				messageCount(player, settings.getName(), perCreatureCountsChunk.get(settings), settings.getChunkLimit(), perCreatureCountsViewDistance.get(settings), settings.getViewDistanceLimit());
		}

		if (tooMany && groupSettings.warnOverLimit())
		{
			Util.Message(Settings.getString(Setting.MESSAGE_TOO_MANY), player);
		}
	}


	private void messageCount(Player player, String creature, int chunkCount, int chunkMax, int vdCount, int vdMax)
	{
		if(vdCount == 0) {
			return;
		}
		
		char chunkColor = Util.getPercentageColor((double) chunkCount / chunkMax);
		char vdColor = Util.getPercentageColor((double) vdCount / vdMax);

		String message = Settings.getString(Setting.MESSAGE_MOB_COUNT_LINE);

		message = message.replace("<MobName>", creature);
		message = message.replace("<ChunkCount>", "&" + chunkColor + chunkCount);
		message = message.replace("<ChunkLimit>", Integer.toString(chunkMax));
		message = message.replace("<ViewDistanceCount>", "&" + vdColor + vdCount);
		message = message.replace("<ViewDistanceLimit>", Integer.toString(vdMax));

		Util.Message(message, player);

		//"<MobName>: <ChunkCount>/<ChunkLimit> in Chunk, <ViewDistanceCount>/<ViewDistanceLimit> in View distance"),
		//"&cYou have too many mobs for the server to handle. Please, if you can, consider killing some or moving them further to keep the server healthy. Many thanks, we appreciate it.");

	}

	class Cleanup implements Runnable
	{
		private int cx, cz, height;
		private World world;
		private Player player;
		private int taskId = -1;
		boolean fired = false;

		Cleanup(int cx, int cz, int height, World world, Player player)
		{
			this.cx = cx;
			this.cz = cz;
			this.height = height;
			this.world = world;
			this.player = player;
		}

		@Override
		public void run()
		{
			if (!fired)
			{
				fired = true;
				MobLimiter.instance.pool.addTask(this);
			}
			else
			{
				for (int x = cx - 6; x < cx + 6; x++)
				{
					for (int z = cz - 6; z < cz + 6; z++)
					{
						if (!world.isChunkLoaded(x, z))
							continue;
						
						Chunk chunk = world.getChunkAt(x, z);
						for (int bx = 0; bx < 16; bx++)
						{
							for (int bz = 0; bz < 16; bz++)
							{
								Block block = chunk.getBlock(bx, height, bz);

								if (bx == 0 || bx == 15 || bz == 0 || bz == 15)
								{
									player.sendBlockChange(block.getLocation(), block.getTypeId(), block.getData());
								}
							}
						}
					}
				}
			}
		}

		public void setTaskId(int taskId)
		{
			this.taskId = taskId;
		}

		public int getTaskId()
		{
			return taskId;
		}
	}
}
