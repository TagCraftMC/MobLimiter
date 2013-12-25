package us.corenetwork.moblimiter.commands;

import us.corenetwork.moblimiter.CreatureSettingsStorage.CreatureGroup;
import us.corenetwork.moblimiter.MobLimiter;

public class AnimalsCommand extends CountCommand {

	public AnimalsCommand(MobLimiter plugin) {
		super(plugin, CreatureGroup.ANIMALS);
		permission = "animals";
	}


}
