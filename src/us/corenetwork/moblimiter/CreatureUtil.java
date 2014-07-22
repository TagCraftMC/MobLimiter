package us.corenetwork.moblimiter;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import us.corenetwork.moblimiter.CreatureGroupSettings;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.metadata.FixedMetadataValue;


import java.util.ArrayDeque;
import java.util.HashMap;


public class CreatureUtil
{

	public static Iterable<LivingEntity> getCreaturesInRange(Chunk start)
	{
		ArrayDeque<LivingEntity> creatures = new ArrayDeque<LivingEntity>();

		World world = start.getWorld();

		int viewDistance = Settings.getInt(Setting.VIEW_DISTANCE_CHUNKS);

		for (int x = -viewDistance; x <= viewDistance; x++)
		{
			for (int z = -viewDistance; z <= viewDistance; z++)
			{
				int newX = start.getX() + x;
				int newZ = start.getZ() + z;

				if (world.isChunkLoaded(newX, newZ))
				{
					Chunk chunk = world.getChunkAt(newX, newZ);
					for (Entity e : chunk.getEntities())
					{
						if (e instanceof LivingEntity)
							creatures.add((LivingEntity) e);
					}
				}
			}
		}

		return creatures;
	}

	public static void purgeCreatures(Chunk chunk)
	{
		HashMap<CreatureGroupSettings, Integer> perGroupCounts = new HashMap<CreatureGroupSettings, Integer>();
		HashMap<CreatureSettings, Integer> perCreatureCounts = new HashMap<CreatureSettings, Integer>();

		int count = 0;
		Entity[] entities = chunk.getEntities().clone();
		for (Entity e : entities)
		{
			if (e instanceof LivingEntity)
			{
				LivingEntity c = (LivingEntity) e;
				CreatureSettings creatureSettings = CreatureGroupSettings.getAnyCreatureSettings(c.getType());
				if (creatureSettings == null)
					continue;

				Integer creatureCount = perCreatureCounts.get(creatureSettings);
				if(creatureCount == null) creatureCount = 0;
				Integer groupCount = perGroupCounts.get(creatureSettings.getGroup());
				if(groupCount == null) groupCount = 0;

				if (groupCount >= creatureSettings.getGroup().getChunkLimit() || creatureCount >= creatureSettings.getChunkLimit())
				{
					e.remove();
					count++;
				}
				else
				{
					perCreatureCounts.put(creatureSettings, creatureCount + 1);
					perGroupCounts.put(creatureSettings.getGroup(), groupCount + 1);
				}
			}
		}
		MLLog.debug("purged " + count + " from chunk " + chunk.getX() + "," + chunk.getZ());
	}

	public static boolean isBreedingFood(EntityType type, Material food)
	{
		switch (type)
		{
			case SHEEP:
			case COW:
				return food == Material.WHEAT;
			case PIG:
				return food == Material.CARROT_ITEM;
			case CHICKEN:
				return food == Material.SEEDS || food == Material.PUMPKIN_SEEDS || food == Material.NETHER_STALK || food == Material.MELON_SEEDS;
			case HORSE:
				return food == Material.GOLDEN_APPLE || food == Material.GOLDEN_CARROT;
			case OCELOT:
				return food == Material.RAW_FISH;
			case WOLF:
				return food == Material.RAW_CHICKEN || food == Material.COOKED_CHICKEN || 
					food == Material.RAW_BEEF || food == Material.COOKED_BEEF || food == Material.ROTTEN_FLESH ||
					food == Material.PORK || food == Material.GRILLED_PORK;
			default:
				return false;
		}
	}

	public static LimitStatus getSpawnDistanceLimitStatus(EntityType type, Location loc, SpawnReason reason)
	{
		CreatureSettings creatureSettings = CreatureGroupSettings.getAnyCreatureSettings(type);
		if (creatureSettings == null)
			return LimitStatus.OK;
		
		if(!creatureSettings.isRangeLimited())
			return LimitStatus.OK; 
		
		if(reason == SpawnReason.NATURAL || reason == SpawnReason.CUSTOM || reason == SpawnReason.DEFAULT)
		{
			// find closest player
			double closestDist = 9999;
			double closestHeight = 9999;
			for(Player p : loc.getWorld().getPlayers())
			{
				closestDist = Math.min(closestDist, p.getLocation().distance(loc));
				closestHeight = Math.min(closestHeight, Math.abs(p.getLocation().getY() - loc.getY()));
			}
			
			if(closestHeight > creatureSettings.getSpawnHeightMax() || closestDist > creatureSettings.getSpawnDistanceMax())
			{
				MLLog.debug("Cancelling " + reason + " Spawn of " + type + " because of " + LimitStatus.TOO_FAR + " " + Math.round(closestDist));
				return LimitStatus.TOO_FAR;
			}
					
			if(closestDist < creatureSettings.getSpawnDistanceMin())
			{
				MLLog.debug("Cancelling " + reason + " Spawn of " + type + " because of " + LimitStatus.TOO_CLOSE + " " + Math.round(closestDist));
				return LimitStatus.TOO_CLOSE;
			}
		}
		
		return LimitStatus.OK;
	}

	public static void flagSpawnerMob(LivingEntity entity)
	{
		entity.setMetadata("ML-Spawner", new FixedMetadataValue(MobLimiter.instance, "true"));
	}

	public static LimitStatus getViewDistanceLimitStatus(EntityType type, Chunk chunk, SpawnReason reason)
	{
		CreatureGroupSettings groupSettings = CreatureGroupSettings.getGroupSettings(type);
		if (groupSettings == null)
			return LimitStatus.OK;

		CreatureSettings creatureSettings = CreatureGroupSettings.getAnyCreatureSettings(type);
		if (creatureSettings == null)
			return LimitStatus.OK;

		int oneCountViewDistance = 0;
		int allCountViewDistance = 0;

		Iterable<LivingEntity> viewDistanceCreatures = getCreaturesInRange(chunk);
		
		for (LivingEntity c : viewDistanceCreatures)
		{
			// compare spawner mobs against spawner limit and count only spawner mobs
			if(reason == SpawnReason.SPAWNER)
			{
				if(c.getType() == type && c.hasMetadata("ML-Spawner"))
				{
					oneCountViewDistance++;
					if (oneCountViewDistance >= creatureSettings.getSpawnerLimit())
					{
						MLLog.debug("Cancelling " + reason + " Spawn of " + type + " because of too many of type");
						return LimitStatus.TOO_MANY_ONE;
					}
				}
			}
			// ignore spawner mobs on other kinds of spawn counting
			else if (creatureSettings.IsSameGroup(c.getType()) && !c.hasMetadata("ML-Spawner"))
			{
				allCountViewDistance++;
				if (allCountViewDistance >= groupSettings.getViewDistanceLimit())
				{
					MLLog.debug("Cancelling " + reason + " Spawn of " + type + " because of too many in group");
					return LimitStatus.TOO_MANY_ALL;
				}

				if (c.getType() == type)
				{
					oneCountViewDistance++;
					if (oneCountViewDistance >= creatureSettings.getViewDistanceLimit())
					{
						MLLog.debug("Cancelling " + reason + " Spawn of " + type + " because of too many of type");
						return LimitStatus.TOO_MANY_ONE;
					}
				}
			}
		}

		return LimitStatus.OK;
	}

	public static enum LimitStatus
	{
		OK,
		TOO_MANY_ONE,
		TOO_MANY_ALL,
		TOO_FAR,
		TOO_CLOSE,
	}
}
