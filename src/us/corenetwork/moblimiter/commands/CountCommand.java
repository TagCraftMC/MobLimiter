package us.corenetwork.moblimiter.commands;

import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import us.corenetwork.moblimiter.CreatureSettings;
import us.corenetwork.moblimiter.CreatureSettingsStorage;
import us.corenetwork.moblimiter.Setting;
import us.corenetwork.moblimiter.Settings;
import us.corenetwork.moblimiter.CreatureSettingsStorage.CreatureGroup;
import us.corenetwork.moblimiter.CreatureSettingsStorage.CreatureGroupSettings;
import us.corenetwork.moblimiter.CreatureUtil;
import us.corenetwork.moblimiter.Util;

public abstract class CountCommand extends BaseCommand {
	private CreatureGroup group;
	
	public CountCommand(CreatureGroup group)
	{
		needPlayer = true;
		this.group = group;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		Chunk playerChunk = player.getLocation().getChunk();
		CreatureGroupSettings groupSettings = CreatureSettingsStorage.getGroupSettings(group); 
		
		HashMap<CreatureSettings, Integer> perCreatureCountsChunk = new HashMap<CreatureSettings, Integer>(); 
		int allCountChunk = 0;

		HashMap<CreatureSettings, Integer> perCreatureCountsViewDistance = new HashMap<CreatureSettings, Integer>(); 
		int allCountViewDistance = 0;
		
		boolean tooMany = false;
		
		for (CreatureSettings settings : groupSettings.creatureSettings.values())
		{
			perCreatureCountsChunk.put(settings, 0);
			perCreatureCountsViewDistance.put(settings, 0);
		}
		
		Iterable<Creature> viewDistanceCreatures = CreatureUtil.getCreaturesInRange(playerChunk);
		Entity[] chunkCreatures = playerChunk.getEntities();

		for (Entity e : chunkCreatures)
		{
			if (e instanceof Creature)
			{
				CreatureSettings creatureSettings = groupSettings.creatureSettings.get(e.getType());
				if (creatureSettings == null)
					continue;
				
				int curCount = perCreatureCountsChunk.get(creatureSettings);
				
				allCountChunk++;
				perCreatureCountsChunk.put(creatureSettings, curCount + 1);				
			}
		}
		
		for (Creature c : viewDistanceCreatures)
		{
			CreatureSettings creatureSettings = groupSettings.creatureSettings.get(c.getType());
			if (creatureSettings == null)
				continue;
				
			int curCount = perCreatureCountsViewDistance.get(creatureSettings);
			
			allCountViewDistance++;
			perCreatureCountsViewDistance.put(creatureSettings, curCount + 1);
			
			if (!tooMany)
			{
				tooMany = curCount > creatureSettings.getViewDistanceLimit() || allCountChunk > groupSettings.globalViewDistanceLimit;
			}
		}
		
		messageCount(player, groupSettings.groupPlural, allCountChunk, groupSettings.globalChunkLimit, allCountViewDistance, groupSettings.globalViewDistanceLimit);
		
		for (CreatureSettings settings : groupSettings.creatureSettings.values())
		{
			messageCount(player, settings.getPluralName(), perCreatureCountsChunk.get(settings), settings.getChunkLimit(), perCreatureCountsViewDistance.get(settings), settings.getViewDistanceLimit());
		}
		
		if (tooMany)
		{
			Util.Message(Settings.getString(Setting.MESSAGE_TOO_MANY), sender);
		}
	}

	
	private void messageCount(Player player, String creature, int chunkCount, int chunkMax, int vdCount, int vdMax)
	{
		char chunkColor = Util.getPercentageColor((double) chunkCount / chunkMax);
		char vdColor = Util.getPercentageColor((double) vdCount / vdMax);
		
		String message= Settings.getString(Setting.MESSAGE_MOB_COUNT_LINE);
			
		message = message.replace("<MobName>", creature);
		message = message.replace("<ChunkCount>", "&" + chunkColor + chunkCount);
		message = message.replace("<ChunkLimit>", Integer.toString(chunkMax));
		message = message.replace("<ViewDistanceCount>", "&" + vdColor + vdCount);
		message = message.replace("<ViewDistanceLimit>", Integer.toString(vdMax));

		Util.Message(message, player);
		
		//"<MobName>: <ChunkCount>/<ChunkLimit> in Chunk, <ViewDistanceCount>/<ViewDistanceLimit> in View distance"),
		//"&cYou have too many mobs for the server to handle. Please, if you can, consider killing some or moving them further to keep the server healthy. Many thanks, we appreciate it.");

	}
}
