package us.corenetwork.moblimiter.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.plugin.Plugin;
import us.corenetwork.moblimiter.MobLimiter;
import us.corenetwork.moblimiter.Setting;
import us.corenetwork.moblimiter.Settings;
import us.corenetwork.moblimiter.Util;

public abstract class BaseCommand {
	public Boolean needPlayer;
	public String permission;
	public MobLimiter plugin;

	public BaseCommand(MobLimiter plugin) {
		this.plugin = plugin;
	}

	public abstract void run(CommandSender sender, String[] args);

	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player) && needPlayer) {
			Util.Message("Sorry, but you need to execute this command as player.", sender);
			return true;
		}
		if (sender instanceof Player && !Util.hasPermission(sender, "moblimiter.command." + permission)) {
			Util.Message(Settings.getString(Setting.MESSAGE_NO_PERMISSION), sender);
			return true;
		}

		run(sender, args);
		return true;
	}

}