package us.corenetwork.moblimiter;

import org.bukkit.Bukkit;

public class MLLog
{
	public static void debug(String text)
	{
		if(Settings.getBoolean(Setting.DEBUG_LOGGING))
			info(text);
	}

	public static void info(String text)
	{
		Bukkit.getLogger().info("[MobLimiter] " + text);
	}

	public static void warning(String text)
	{
		Bukkit.getLogger().warning("[MobLimiter] " + text);
	}

	public static void severe(String text)
	{
		Bukkit.getLogger().severe("[MobLimiter] " + text);
	}
}
