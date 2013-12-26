package us.corenetwork.moblimiter;

import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map.Entry;

public class CreatureSettingsStorage
{

	public static HashMap<EntityType, CreatureGroupSettings> typeGroups;
	private static HashMap<CreatureGroup, CreatureGroupSettings> groups;

	public static void init()
	{
		typeGroups = new HashMap<EntityType, CreatureGroupSettings>();
		groups = new HashMap<CreatureGroup, CreatureGroupSettings>();

		for (CreatureGroup group : CreatureGroup.values())
			initGroup(group);
	}

	public static CreatureGroupSettings getGroupSettings(CreatureGroup group)
	{
		return groups.get(group);
	}

	private static void initGroup(CreatureGroup group)
	{
		MemorySection configSection = (MemorySection) IO.config.get(group.configNode);

		if (configSection == null)
		{
			MLLog.severe("Config for " + group.toString() + " missing!");
			return;
		}

		CreatureGroupSettings groupSettings = new CreatureGroupSettings();
		groupSettings.globalChunkLimit = configSection.getInt("GlobalChunkLimit", 9001);
		groupSettings.globalViewDistanceLimit = configSection.getInt("GlobalViewDistanceLimit", 9001);

		String plural = configSection.getString("GroupPlural");
		if (plural == null)
			plural = "I don't know how to config!";
		groupSettings.groupPlural = plural;

		for (Entry<String, Object> e : configSection.getValues(false).entrySet())
		{
			if (!(e.getValue() instanceof MemorySection))
				continue;

			EntityType type;
			try
			{
				type = EntityType.valueOf(e.getKey());
			} catch (IllegalArgumentException err)
			{
				MLLog.warning("Invalid config! " + e.getKey() + " is not valid entity type!");
				continue;

			}

			CreatureSettings creatureSettings = new CreatureSettings(type, (MemorySection) e.getValue());

			groupSettings.creatureSettings.put(type, creatureSettings);
			typeGroups.put(type, groupSettings);
		}

		groups.put(group, groupSettings);
	}

	public static class CreatureGroupSettings
	{
		public HashMap<EntityType, CreatureSettings> creatureSettings = new HashMap<EntityType, CreatureSettings>();
		public int globalChunkLimit;
		public int globalViewDistanceLimit;
		public String groupPlural;
	}


	public static enum CreatureGroup
	{
		ANIMALS("Animals"),
		VILLAGES("Villages");

		private String configNode;

		private CreatureGroup(String configNode)
		{
			this.configNode = configNode;
		}
	}
}
