package dk.spirit55555.serverlogger.loggers;

import dk.spirit55555.serverlogger.ILogger;
import dk.spirit55555.serverlogger.ServerLogger;
import java.util.ArrayList;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDeathLogger implements ILogger, Listener {
	private static final String NAME = "playerdeath";
	private ServerLogger plugin;

	private final ArrayList<Document> deathLogs = new ArrayList<>();

	@Override
	public void init(ServerLogger plugin) {
		this.plugin = plugin;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public ArrayList<Document> getData() {
		return deathLogs;
	}

	@Override
	public void resetData() {
		deathLogs.clear();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void playerDeathEvent(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;

		//Let EntityDamageByEntityEvent handle these
		if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK
			|| event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
			|| event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK
			|| event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) { //FIXME: Test if this works...
			return;
		}

		Player player = (Player) event.getEntity();

		if (!(event.getFinalDamage() >= player.getHealth()))
			return;

		Document deathLog = new Document()
			.append("name", player.getName())
			.append("uuid", player.getUniqueId().toString())
			.append("world", player.getWorld().getName())
			.append("location", plugin.locationToString(player.getLocation()))
			.append("cause", event.getCause().name())
			.append("timestamp", plugin.getUnixTimestamp());

		deathLogs.add(deathLog);
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void playerDeathEvent(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();

		if (!(event.getFinalDamage() >= player.getHealth()))
			return;

		Document deathLog = new Document()
			.append("name", player.getName())
			.append("uuid", player.getUniqueId().toString())
			.append("world", player.getWorld().getName())
			.append("location", plugin.locationToString(player.getLocation()))
			.append("cause", event.getCause().name())
			.append("killer_entity", event.getDamager().getType().name())
			.append("timestamp", plugin.getUnixTimestamp());

		if (event.getDamager() instanceof Player) {
			Player killer = (Player) event.getDamager();

			deathLog.append("killer_name", killer.getName())
				.append("killer_uuid", killer.getUniqueId().toString())
				.append("killer_health", killer.getHealth())
				.append("killer_food", killer.getFoodLevel())
				.append("killer_item", killer.getInventory().getItemInMainHand().getType().name());
		}

		deathLogs.add(deathLog);
	}
}
