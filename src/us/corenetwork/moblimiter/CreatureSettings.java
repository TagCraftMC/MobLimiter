package us.corenetwork.moblimiter;

import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;

public class CreatureSettings {
	private MemorySection section;
	private EntityType type;

	public CreatureSettings(EntityType type, MemorySection section) {
		this.type = type;
		this.section = section;
	}

	public EntityType getType() {
		return type;
	}

	public int getChunkLimit() {
		Integer limit = section.getInt("ChunkLimit");
		if (limit == null)
			limit = 9001;

		return limit;
	}

	public int getViewDistanceLimit() {
		Integer limit = section.getInt("ViewDistanceLimit");
		if (limit == null)
			limit = 9001;

		return limit;
	}

	public String getPluralName() {
		String name = section.getString("NamePlural");
		if (name == null)
			name = "UNKNOWN ANIMALS";

		return name;
	}

	public String getSingularName() {
		String name = section.getString("NameSingular");
		if (name == null)
			name = "UNKNOWN ANIMALS";

		return name;
	}

}
