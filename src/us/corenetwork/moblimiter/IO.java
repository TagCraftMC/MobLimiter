package us.corenetwork.moblimiter;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class IO
{
	public static YamlConfiguration config;

	public static void LoadSettings()
	{
		try
		{
			config = new YamlConfiguration();

			if (!new File(MobLimiter.instance.getDataFolder(), "config.yml").exists())
				config.save(new File(MobLimiter.instance.getDataFolder(), "config.yml"));

			config.load(new File(MobLimiter.instance.getDataFolder(), "config.yml"));
			for (Setting s : Setting.values())
			{
				if (config.get(s.getString()) == null && s.getDefault() != null)
					config.set(s.getString(), s.getDefault());
			}

			saveConfig();

		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvalidConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveConfig()
	{
		try
		{
			config.save(new File(MobLimiter.instance.getDataFolder(), "config.yml"));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
