package us.corenetwork.moblimiter.commands;

import us.corenetwork.moblimiter.CreatureSettingsStorage.CreatureGroup;

public class VillagersCommand extends CountCommand {
	
	public VillagersCommand()
	{
		super(CreatureGroup.VILLAGES);
		permission = "villagers";
	}
}
