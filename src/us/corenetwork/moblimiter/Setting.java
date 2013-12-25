package us.corenetwork.moblimiter;


import org.bukkit.DyeColor;
import org.bukkit.Material;

public enum Setting {

	VIEW_DISTANCE_CHUNKS("ViewDistanceChunks", 10),
	NO_HORSE_BREED("NoHorseBreed", true),
	BREEDING_SPAM_DELAY_SECONDS("BreedingSpamDelaySeconds", 5),

	MESSAGE_NO_PERMISSION("Messages.NoPermission", "No permission!"),
	MESSAGE_MOB_COUNT_LINE("Messages.MobCountLine", "<MobName>: <ChunkCount>&f/<ChunkLimit> in Chunk, <ViewDistanceCount>&f/<ViewDistanceLimit> in View distance"),
	MESSAGE_TOO_MANY("Messages.TooManyMobs", "&cYou have too many mobs for the server to handle. Please, if you can, consider killing some or moving them further to keep the server healthy. Many thanks, we appreciate it."),
	MESSAGE_BREED_LIMIT_ONE_MOB("Messages.BreedLimitOneMob", "You cannot breed this <MobName> because there is more than <MobTypeLimit> <MobNamePlural> in <Radius> block radius around you."),
	MESSAGE_BREED_LIMIT_ALL_MOBS("Messages.BreedLimitAllMobs", "You cannot breed this <MobName> because there are too more than <MobGroupLimit> <MobGroupNamePlural> in <Radius> block radius around you."),
	MESSAGE_NO_HORSE_BREEDING("Messages.NoHorseBreeding", "Sorry, you are not allowed to breed horses. Find another one in the wild."),


	BLOCK_NOT_APPLICABLE_ID("Blocks.NotApplicable.Id", Material.WOOL.getId()),
	BLOCK_NOT_APPLICABLE_DATA("Blocks.NotApplicable.Data", (int) DyeColor.GRAY.getWoolData()),

	BLOCK_BELOW_80_ID("Blocks.Below80.Id", Material.WOOL.getId()),
	BLOCK_BELOW_80_DATA("Blocks.Below80.Data", (int) DyeColor.GREEN.getWoolData()),

	BLOCK_BELOW_90_ID("Blocks.Below90.Id", Material.WOOL.getId()),
	BLOCK_BELOW_90_DATA("Blocks.Below90.Data", (int) DyeColor.YELLOW.getWoolData()),

	BLOCK_BELOW_100_ID("Blocks.Below100.Id", Material.WOOL.getId()),
	BLOCK_BELOW_100_DATA("Blocks.Below100.Data", (int) DyeColor.ORANGE.getWoolData()),

	BLOCK_EXCEEDS_LIMIT_ID("Blocks.ExceedsLimit.Id", Material.WOOL.getId()),
	BLOCK_EXCEEDS_LIMIT_DATA("Blocks.ExceedsLimit.Data", (int) DyeColor.RED.getWoolData()),;

	private String name;
	private Object def;

	private Setting(String Name, Object Def) {
		name = Name;
		def = Def;
	}

	public String getString() {
		return name;
	}

	public Object getDefault() {
		return def;
	}
}
