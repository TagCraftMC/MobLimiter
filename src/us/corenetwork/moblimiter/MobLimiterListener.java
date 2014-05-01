package us.corenetwork.moblimiter;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import us.corenetwork.moblimiter.CreatureUtil.LimitStatus;

import java.util.HashMap;
import java.util.List;

public class MobLimiterListener implements Listener
{

	public static HashMap<String, Long> lastDisplayedBreedingSpam = new HashMap<String, Long>();
	private static List<String> worlds = (List<String>) Settings.getList(Setting.ENABLED_WORLDS); 

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkUnload(ChunkUnloadEvent event)
	{
		if(worlds.contains(event.getWorld().getName()))
		{
			CreatureUtil.purgeCreatures(event.getChunk());
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		// disable zombie pigmen from portals if enabled, they are sent as spawner egg events
		if(event.getSpawnReason() == SpawnReason.SPAWNER_EGG && event.getEntity().getType() == EntityType.PIG_ZOMBIE && Settings.getBoolean(Setting.NO_PIGMEN_PORTAL))
		{
			MLLog.debug("Cancelling Portal Spawn of PIG_ZOMBIE because of NO_PIGMEN_PORTAL option");
			event.setCancelled(true);
			return;
		}
		
		if(worlds.contains(event.getLocation().getWorld().getName()))
		{
			
			
			if (CreatureUtil.getSpawnDistanceLimitStatus(event.getEntityType(), event.getLocation(), event.getSpawnReason()) != LimitStatus.OK)
			{
				event.setCancelled(true);
				return;
			}
			
			if (CreatureUtil.getViewDistanceLimitStatus(event.getEntityType(), event.getLocation().getChunk(),  event.getSpawnReason()) != LimitStatus.OK)
			{
				event.setCancelled(true);
				return;
			}
		}
		
		if(event.getSpawnReason() == SpawnReason.SPAWNER)
		{
			CreatureUtil.flagSpawnerMob(event.getEntity());
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityPortal(EntityPortalEvent event)
	{
		if (worlds.contains(event.getTo().getWorld().getName()) && CreatureUtil.getViewDistanceLimitStatus(event.getEntityType(), event.getTo().getChunk(), null) != LimitStatus.OK)
		{
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event)
	{
		if(!worlds.contains(event.getPlayer().getLocation().getWorld().getName()))
			return;
		
		Entity ent = event.getRightClicked();

		if (ent == null)
			return;

		Player player = event.getPlayer();
		ItemStack hand = player.getItemInHand();

		if (hand == null)
			return;

		if (!CreatureUtil.isBreedingFood(ent.getType(), hand.getType()))
			return;

		//No horse breed
		if (Settings.getBoolean(Setting.NO_HORSE_BREED) && ent.getType() == EntityType.HORSE)
		{
			Util.Message(Settings.getString(Setting.MESSAGE_NO_HORSE_BREEDING), player);
			event.setCancelled(true);
			player.updateInventory();
			return;
		}

		LimitStatus status = CreatureUtil.getViewDistanceLimitStatus(ent.getType(), ent.getLocation().getChunk(), null);
		if (status == LimitStatus.OK)
			return;

		CreatureGroupSettings groupSettings = CreatureGroupSettings.getGroupSettings(ent.getType());
		CreatureSettings creatureSettings = groupSettings.getCreatureSettings(ent.getType());

		Long lastDisplayedSpam = lastDisplayedBreedingSpam.get(player.getName());
		if (lastDisplayedSpam == null)
			lastDisplayedSpam = 0L;

		long diff = System.currentTimeMillis() - lastDisplayedSpam;
		if (diff > Settings.getInt(Setting.BREEDING_SPAM_DELAY_SECONDS) * 1000)
		{
			String message;
			if (status == LimitStatus.TOO_MANY_ONE)
			{
				message = Settings.getString(Setting.MESSAGE_BREED_LIMIT_REACHED);

				message = message.replace("<MobName>", creatureSettings.getName());
				message = message.replace("<MobLimit>", Integer.toString(creatureSettings.getViewDistanceLimit()));
			}
			else
			{
				message = Settings.getString(Setting.MESSAGE_BREED_LIMIT_REACHED);

				message = message.replace("<MobName>", groupSettings.getName());
				message = message.replace("<MobLimit>", Integer.toString(groupSettings.getViewDistanceLimit()));
			}

			Util.Message(message, player);

			lastDisplayedBreedingSpam.put(player.getName(), System.currentTimeMillis());
		}

		event.setCancelled(true);
		player.updateInventory();
	}


}
