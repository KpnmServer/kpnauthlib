
package com.github.zyxgad.authlib.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
// import org.bukkit.event.player.PlayerBucketEntityEvent;
// import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
// import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;

import com.github.zyxgad.authlib.AuthLib;
import com.github.zyxgad.authlib.storage.OffUserStorage;
import com.github.zyxgad.authlib.util.ColorTextBuilder;


public final class PlayerStatusListener implements Listener{
	private final String NO_EVENT_MSG = new ColorTextBuilder()
		.red().add("[SERVER]")
		.add("请先登录").toString();
	private final String NO_CHAT_MSG = new ColorTextBuilder()
		.red().add("[SERVER]")
		.add("请先登录才能聊天").toString();
	private final String NO_COMMAND_MSG = new ColorTextBuilder()
		.red().add("[SERVER]")
		.add("请先登录才能执行其他命令").toString();

	public PlayerStatusListener(){}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event){
		// UUID uuid = event.getUniqueId();
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerLogin(PlayerLoginEvent event){
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		OffUserStorage.getInstance().onPlayerJoin(player);

		event.setJoinMessage(new ColorTextBuilder()
			.blue("[SERVER] ")
			.green(player.getName())
			.blue(" came to [world]")
			.toString());
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerLeave(PlayerQuitEvent event){
		Player player = event.getPlayer();

		OffUserStorage.getInstance().onPlayerQuit(player);

		event.setQuitMessage(new ColorTextBuilder()
			.blue("[SERVER] ")
			.green(player.getName())
			.blue(" flew out [world]")
			.toString());
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();

		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			player.sendRawMessage(NO_CHAT_MSG);
			event.setCancelled(true);
			return;
		}
		event.setFormat(new ColorTextBuilder()
			.add('[').green(player.getName())
			.add('/').yellow("PLAYER").add("]: ")
			.add("%2$s")
			.toString());
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			String cmd = event.getMessage();
			if(cmd.startsWith("/login ") || cmd.startsWith("/register ")){
				return;
			}
			player.sendRawMessage(NO_COMMAND_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerBedEnter(PlayerBedEnterEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	// @EventHandler(priority=EventPriority.LOWEST)
	// public void onPlayerBucketEntity(PlayerBucketEntityEvent event){
	// 	Player player = event.getPlayer();
	// 	if(!OffUserStorage.getInstance().isPlayerLogin(player)){
	// 		player.sendRawMessage(NO_EVENT_MSG);
	// 		event.setCancelled(true);
	// 	}
	// }

	// @EventHandler(priority=EventPriority.LOWEST)
	// public void onPlayerBucket(PlayerBucketEvent event){
	// 	Player player = event.getPlayer();
	// 	if(!OffUserStorage.getInstance().isPlayerLogin(player)){
	// 		player.sendRawMessage(NO_EVENT_MSG);
	// 		event.setCancelled(true);
	// 	}
	// }

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerDropItem(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerEditBook(PlayerEditBookEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerFish(PlayerFishEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	// @EventHandler(priority=EventPriority.LOWEST)
	// public void onPlayerHarvestBlock(PlayerHarvestBlockEvent event){
	// 	Player player = event.getPlayer();
	// 	if(!OffUserStorage.getInstance().isPlayerLogin(player)){
	//		player.sendRawMessage(NO_EVENT_MSG);
	// 		event.setCancelled(true);
	// 	}
	// }

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerItemConsume(PlayerItemConsumeEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerItemDamage(PlayerItemDamageEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerItemHeld(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerItemMend(PlayerItemMendEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerPickupItem(PlayerPickupItemEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerShearEntity(PlayerShearEntityEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerStatisticIncrement(PlayerStatisticIncrementEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerTakeLecternBook(PlayerTakeLecternBookEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerToggleFlight(PlayerToggleFlightEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerToggleSprint(PlayerToggleSprintEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerVelocity(PlayerVelocityEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerLeashEntity(PlayerLeashEntityEvent event){
		Player player = event.getPlayer();
		if(!OffUserStorage.getInstance().isPlayerLogin(player)){
			// player.sendRawMessage(NO_EVENT_MSG);
			event.setCancelled(true);
		}
	}
}
