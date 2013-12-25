package us.corenetwork.moblimiter;

import java.util.ArrayDeque;
import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import us.corenetwork.moblimiter.CreatureSettingsStorage.CreatureGroup;
import us.corenetwork.moblimiter.CreatureSettingsStorage.CreatureGroupSettings;

public class CreatureUtil {

	public static Iterable<Creature> getCreaturesInRange(Chunk start) {
		ArrayDeque<Creature> creatures = new ArrayDeque<Creature>();

		World world = start.getWorld();

		int viewDistance = Settings.getInt(Setting.VIEW_DISTANCE_CHUNKS);

		for (int x = -viewDistance; x <= viewDistance; x++) {
			for (int z = -viewDistance; z <= viewDistance; z++) {
				int newX = start.getX() + x;
				int newZ = start.getZ() + z;

				if (world.isChunkLoaded(newX, newZ)) {
					Chunk chunk = world.getChunkAt(newX, newZ);
					for (Entity e : chunk.getEntities()) {
						if (e instanceof Creature)
							creatures.add((Creature) e);
					}
				}
			}
		}

		return creatures;
	}

	public static void purgeCreatures(Chunk chunk) {
		for (CreatureGroup group : CreatureGroup.values()) {
			CreatureGroupSettings settings = CreatureSettingsStorage.getGroupSettings(group);

			HashMap<CreatureSettings, Integer> perCreatureCounts = new HashMap<CreatureSettings, Integer>();
			int allCount = 0;

			for (CreatureSettings setting : settings.creatureSettings.values()) {
				perCreatureCounts.put(setting, 0);
			}

			Entity[] entities = chunk.getEntities().clone();
			for (Entity e : entities) {
				if (e instanceof Creature) {
					Creature c = (Creature) e;
					CreatureSettings creatureSettings = settings.creatureSettings.get(c.getType());
					if (creatureSettings == null)
						continue;
					int creatureCount = perCreatureCounts.get(creatureSettings);

					if (allCount >= settings.globalChunkLimit || creatureCount >= creatureSettings.getChunkLimit()) {
						e.remove();
					} else {
						allCount++;
						perCreatureCounts.put(creatureSettings, creatureCount + 1);
					}
				}
			}
		}
	}

	public static boolean isBreedingFood(EntityType type, Material food) {
		switch (type) {
			case SHEEP:
			case COW:
				return food == Material.WHEAT;
			case PIG:
				return food == Material.CARROT_ITEM;
			case CHICKEN:
				return food == Material.SEEDS || food == Material.PUMPKIN_SEEDS || food == Material.NETHER_STALK || food == Material.MELON_SEEDS;
			case HORSE:
				return food == Material.GOLDEN_APPLE || food == Material.GOLDEN_CARROT;
			default:
				return false;
		}
	}


	public static LimitStatus getViewDistanceLimitStatus(EntityType type, Chunk chunk) {
		CreatureGroupSettings groupSettings = CreatureSettingsStorage.typeGroups.get(type);
		if (groupSettings == null)
			return LimitStatus.OK;

		CreatureSettings creatureSettings = groupSettings.creatureSettings.get(type);

		int oneCountViewDistance = 0;
		int allCountViewDistance = 0;

		Iterable<Creature> viewDistanceCreatures = getCreaturesInRange(chunk);
		for (Creature c : viewDistanceCreatures) {
			if (CreatureSettingsStorage.typeGroups.get(c.getType()) == groupSettings) {
				allCountViewDistance++;
				if (allCountViewDistance >= groupSettings.globalViewDistanceLimit)
					return LimitStatus.TOO_MANY_ALL;

				if (c.getType() == type) {
					oneCountViewDistance++;
					if (oneCountViewDistance >= creatureSettings.getViewDistanceLimit())
						return LimitStatus.TOO_MANY_ONE;

				}
			}
		}

		return LimitStatus.OK;
	}

	public static enum LimitStatus {
		OK,
		TOO_MANY_ONE,
		TOO_MANY_ALL,
	}
}
