package us.corenetwork.moblimiter.commands;

import us.corenetwork.moblimiter.CreatureSettingsStorage.CreatureGroup;
import us.corenetwork.moblimiter.MobLimiter;

public class VillagersCommand extends CountCommand
{

	public VillagersCommand(MobLimiter plugin)
	{
		super(plugin, CreatureGroup.VILLAGES);
		permission = "villagers";
	}
}
