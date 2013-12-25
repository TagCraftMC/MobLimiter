package us.corenetwork.moblimiter;

import java.util.HashMap;

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

import us.corenetwork.moblimiter.CreatureSettingsStorage.CreatureGroupSettings;
import us.corenetwork.moblimiter.CreatureUtil.LimitStatus;

public class MobLimiterListener implements Listener {

	public static HashMap<String, Long> lastDisplayedBreedingSpam = new HashMap<String, Long>();

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkUnload(ChunkUnloadEvent event) {
		CreatureUtil.purgeCreatures(event.getChunk());
	}

	@EventHandler(ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		//Prevent horse breeding
		if (Settings.getBoolean(Setting.NO_HORSE_BREED) && event.getEntityType() == EntityType.HORSE && event.getSpawnReason() == SpawnReason.BREEDING) {
			event.setCancelled(true);
			return;
		}

		if (CreatureUtil.getViewDistanceLimitStatus(event.getEntityType(), event.getLocation().getChunk()) != LimitStatus.OK) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityPortal(EntityPortalEvent event) {
		if (CreatureUtil.getViewDistanceLimitStatus(event.getEntityType(), event.getTo().getChunk()) != LimitStatus.OK) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
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
		if (Settings.getBoolean(Setting.NO_HORSE_BREED) && ent.getType() == EntityType.HORSE) {
			Util.Message(Settings.getString(Setting.MESSAGE_NO_HORSE_BREEDING), player);
			event.setCancelled(true);
			player.updateInventory();
			return;
		}

		LimitStatus status = CreatureUtil.getViewDistanceLimitStatus(ent.getType(), ent.getLocation().getChunk());
		if (status == LimitStatus.OK)
			return;

		CreatureGroupSettings groupSettings = CreatureSettingsStorage.typeGroups.get(ent.getType());
		CreatureSettings creatureSettings = groupSettings.creatureSettings.get(ent.getType());

		Long lastDisplayedSpam = lastDisplayedBreedingSpam.get(player.getName());
		if (lastDisplayedSpam == null)
			lastDisplayedSpam = 0L;

		long diff = System.currentTimeMillis() - lastDisplayedSpam;
		if (diff > Settings.getInt(Setting.VIEW_DISTANCE_CHUNKS) * 1000) {
			String message;
			if (status == LimitStatus.TOO_MANY_ONE) {
				message = Settings.getString(Setting.MESSAGE_BREED_LIMIT_ONE_MOB);

				message = message.replace("<MobNamePlural>", creatureSettings.getPluralName());
				message = message.replace("<MobTypeLimit>", Integer.toString(creatureSettings.getViewDistanceLimit()));
			} else {
				message = Settings.getString(Setting.MESSAGE_BREED_LIMIT_ALL_MOBS);

				message = message.replace("<MobGroupNamePlural>", groupSettings.groupPlural);
				message = message.replace("<MobGroupLimit>", Integer.toString(groupSettings.globalViewDistanceLimit));
			}

			message = message.replace("<MobName>", creatureSettings.getSingularName());
			Util.Message(message, player);

			lastDisplayedBreedingSpam.put(player.getName(), System.currentTimeMillis());
		}

		event.setCancelled(true);
		player.updateInventory();
	}


}
