package us.corenetwork.moblimiter;


import org.bukkit.DyeColor;
import org.bukkit.Material;

public enum Setting
{

	VIEW_DISTANCE_CHUNKS("ViewDistanceChunks", 10),
	NO_HORSE_BREED("NoHorseBreed", true),
	BREEDING_SPAM_DELAY_SECONDS("BreedingSpamDelaySeconds", 5),

	MESSAGE_NO_PERMISSION("Messages.NoPermission", "No permission!"),
	MESSAGE_MOB_COUNT_LINE("Messages.MobCountLine", "<MobName>: <ChunkCount>&f/<ChunkLimit> in Chunk, <ViewDistanceCount>&f/<ViewDistanceLimit> in View distance"),
	MESSAGE_TOO_MANY("Messages.TooManyMobs", "&cYou have too many mobs for the server to handle. Please, if you can, consider killing some or moving them further to keep the server healthy. Many thanks, we appreciate it."),
	MESSAGE_BREED_LIMIT_ONE_MOB("Messages.BreedLimitOneMob", "You cannot breed this <MobName> because there is more than <MobTypeLimit> <MobNamePlural> in <Radius> block radius around you."),
	MESSAGE_BREED_LIMIT_ALL_MOBS("Messages.BreedLimitAllMobs", "You cannot breed this <MobName> because there are too more than <MobGroupLimit> <MobGroupNamePlural> in <Radius> block radius around you."),
	MESSAGE_NO_HORSE_BREEDING("Messages.NoHorseBreeding", "Sorry, you are only allowed to breed horses using golden apple."),


	GRID_NONE_ID("Grid.None.Id", Material.GLASS.getId()),
	GRID_NONE_DATA("Grid.None.Data", 0),

	GRID_LOW_ID("Grid.Low.Id", Material.WOOL.getId()),
	GRID_LOW_DATA("Grid.Low.Data", (int) DyeColor.LIME.getWoolData()),

	GRID_MEDIUM_ID("Grid.Medium.Id", Material.WOOL.getId()),
	GRID_MEDIUM_DATA("Grid.Medium.Data", (int) DyeColor.YELLOW.getWoolData()),

	GRID_HIGH_ID("Grid.High.Id", Material.WOOL.getId()),
	GRID_HIGH_DATA("Grid.High.Data", (int) DyeColor.ORANGE.getWoolData()),

	GRID_EXCEED_ID("Grid.Exceed.Id", Material.WOOL.getId()),
	GRID_EXCEED_DATA("Grid.Exceed.Data", (int) DyeColor.RED.getWoolData()),

	GRID_DURATION("Grid.Duration", 30 * 20);

	private String name;
	private Object def;

	private Setting(String Name, Object Def)
	{
		name = Name;
		def = Def;
	}

	public String getString()
	{
		return name;
	}

	public Object getDefault()
	{
		return def;
	}
}
