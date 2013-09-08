package us.corenetwork.moblimiter.commands;

import us.corenetwork.moblimiter.CreatureSettingsStorage.CreatureGroup;

public class AnimalsCommand extends CountCommand {
	
	public AnimalsCommand()
	{
		super(CreatureGroup.ANIMALS);
		permission = "animals";
	}

	
}
