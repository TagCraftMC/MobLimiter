package us.corenetwork.moblimiter.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.moblimiter.Setting;
import us.corenetwork.moblimiter.Settings;
import us.corenetwork.moblimiter.Util;

public abstract class BaseCommand
{
	public Boolean needPlayer;
	public String permission;
	public String desc;

	public abstract void run(CommandSender sender, String[] args);

	public boolean execute(CommandSender sender, String[] args, boolean stripArgs)
	{
		if (!(sender instanceof Player) && needPlayer)
		{
			Util.Message("Sorry, but you need to execute this command as player.", sender);
			return true;
		}
		if (sender instanceof Player && !Util.hasPermission(sender, "moblimiter.command." + permission))
		{
			Util.Message(Settings.getString(Setting.MESSAGE_NO_PERMISSION), sender);
			return true;
		}
		
		if (stripArgs && args.length > 0 && !Util.isInteger(args[0]))
		{
			args = Arrays.copyOfRange(args, 1, args.length);
		}

		run(sender, args);
		return true;
	}

}