package us.corenetwork.moblimiter;


public enum Setting {	
	
	VIEW_DISTANCE_CHUNKS("ViewDistanceChunks", 10),
	NO_HORSE_BREED("NoHorseBreed", true),
	
	MESSAGE_NO_PERMISSION("Messages.NoPermission", "No permission!"),
	MESSAGE_MOB_COUNT_LINE("Messages.MobCountLine", "<MobName>: <ChunkCount>&f/<ChunkLimit> in Chunk, <ViewDistanceCount>&f/<ViewDistanceLimit> in View distance"),
	MESSAGE_TOO_MANY("Messages.TooManyMobs", "&cYou have too many mobs for the server to handle. Please, if you can, consider killing some or moving them further to keep the server healthy. Many thanks, we appreciate it."),
	MESSAGE_BREED_LIMIT_ONE_MOB("Messages.BreedLimitOneMob", "You cannot breed this <MobName> because there is more than <MobTypeLimit> <MobNamePlural> in <Radius> block radius around you."),
	MESSAGE_BREED_LIMIT_ALL_MOBS("Messages.BreedLimitAllMobs", "You cannot breed this <MobName> because there are too more than <MobGroupLimit> <MobGroupNamePlural> in <Radius> block radius around you."),
	MESSAGE_NO_HORSE_BREEDING("Messages.NoHorseBreeding", "Sorry, you are not allowed to breed horses. Find another one in the wild.");

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
