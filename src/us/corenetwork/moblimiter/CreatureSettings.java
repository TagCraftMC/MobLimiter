package us.corenetwork.moblimiter;

import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;

public class CreatureSettings
{
	private EntityType type;
	private CreatureGroupSettings group;
	private int chunkLimit;
	private int viewDistanceLimit;
	private int spawnerLimit;
	private int spawnDistanceMax;
	private int spawnDistanceMin;
	private int spawnHeightMax;
	private String name;
	private boolean listed;

	public CreatureSettings(EntityType type, CreatureGroupSettings group, MemorySection section)
	{
		this.type = type;
		this.group = group;

		listed = true;
		chunkLimit = section.getInt("ChunkLimit", group.getChunkLimit());
		spawnerLimit = section.getInt("SpawnerLimit", chunkLimit);
		viewDistanceLimit = section.getInt("ViewDistanceLimit", group.getViewDistanceLimit());
	    spawnDistanceMax = section.getInt("SpawnDistanceMax", group.getSpawnDistanceMax());
	    spawnDistanceMin = section.getInt("SpawnDistanceMin", group.getSpawnDistanceMin());
	    spawnHeightMax = section.getInt("SpawnHeightMax", group.getSpawnHeightMax());
		name = section.getString("Name");
		if(name == null) {
			listed = false;
			type.getName();
		}
	}

	public EntityType getType()
	{
		return type;
	}

	public int getChunkLimit()
	{
		return chunkLimit;
	}

	public int getSpawnerLimit()
	{
		return spawnerLimit;
	}

	public int getViewDistanceLimit()
	{
		return viewDistanceLimit;
	}

	public String getName()
	{
		return name;
	}
	
	public boolean isListed()
	{
		return listed;
	}
	
	public CreatureGroupSettings getGroup()
	{
		return group;
	}
	
	public boolean IsSameGroup(EntityType type)
	{
		return group.getCreatureSettings(type) != null;
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
	
	public boolean isRangeLimited()
	{
		return spawnDistanceMax < 1000 || spawnDistanceMin > 0 || spawnHeightMax < 1000;
	}
}
