package us.corenetwork.moblimiter;

import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;

import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

public class CreatureGroupSettings
{
	private static HashMap<EntityType, CreatureGroupSettings> typeGroups;
	private static HashMap<String, CreatureGroupSettings> groups;

	private HashMap<EntityType, CreatureSettings> creatures;
	private int chunkLimit;
	private int viewDistanceLimit;
	private String name;
	private boolean warning;
	private int spawnDistanceMax;
	private int spawnDistanceMin;
	private int spawnHeightMax;
	
	public static void init()
	{
		typeGroups = new HashMap<EntityType, CreatureGroupSettings>();
		groups = new HashMap<String, CreatureGroupSettings>();

		MemorySection configSection = (MemorySection) IO.config.get("Groups");
		
		for (Entry<String, Object> e : configSection.getValues(false).entrySet())
		{
			if (!(e.getValue() instanceof MemorySection))
				continue;
			new CreatureGroupSettings(e.getKey(), (MemorySection)e.getValue());

		}
	}

	protected CreatureGroupSettings(String name, MemorySection configSection)
	{
		chunkLimit = configSection.getInt("ChunkLimit", 9001);
		viewDistanceLimit = configSection.getInt("ViewDistanceLimit", 9001);
		this.name = configSection.getString("Name", name);
	    warning = configSection.getBoolean("WarnOverLimit", false);
	    spawnDistanceMax = configSection.getInt("SpawnDistanceMax", 1000);
	    spawnDistanceMin = configSection.getInt("SpawnDistanceMin", -1);
	    spawnHeightMax = configSection.getInt("SpawnHeightMax", 1000);
		creatures = new HashMap<EntityType, CreatureSettings>();

		for (Entry<String, Object> e : configSection.getValues(false).entrySet())
		{
			if (!(e.getValue() instanceof MemorySection))
				continue;

			EntityType type;
			try
			{
				type = EntityType.valueOf(e.getKey());
			}
			catch (IllegalArgumentException err)
			{
				MLLog.warning("Invalid config! " + e.getKey() + " is not valid entity type!");
				continue;

			}

			creatures.put(type, new CreatureSettings(type, this, (MemorySection) e.getValue()));
			typeGroups.put(type, this);
		}

		groups.put(name, this);
	}

	public static Set<Entry<String, CreatureGroupSettings>> getGroups()
	{
		return groups.entrySet();
	}

	public static CreatureGroupSettings getGroupSettings(String group)
	{
		return groups.get(group);
	}

	public static CreatureGroupSettings getGroupSettings(EntityType type)
	{
		return typeGroups.get(type);
	}

	public static CreatureSettings getAnyCreatureSettings(EntityType type)
	{
		if(typeGroups.containsKey(type))
			return typeGroups.get(type).creatures.get(type);
		return null;
	}

	public CreatureSettings getCreatureSettings(EntityType e)
	{
		return creatures.get(e);
	}

	public Collection<CreatureSettings> getCreatures()
	{
		return creatures.values();
	}

	public int getChunkLimit()
	{
		return chunkLimit;
	}

	public int getViewDistanceLimit()
	{
		return viewDistanceLimit;
	}

	public String getName()
	{
		return name;
	}
	
	public boolean warnOverLimit()
	{
		return warning;
	}

	public int getSpawnDistanceMax()
	{
		return spawnDistanceMax;
	}

	public int getSpawnDistanceMin()
	{
		return spawnDistanceMin;
	}

	public int getSpawnHeightMax()
	{
		return spawnHeightMax;
	}

}
