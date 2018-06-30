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

public class PlayerDamageEntityLogger implements ILogger, Listener {
	private static final String NAME = "playerdamageentity";
	private ServerLogger plugin;

	private final ArrayList<Document> damageLogs = new ArrayList<>();

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
		return damageLogs;
	}

	@Override
	public void resetData() {
		damageLogs.clear();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void playerDeathEvent(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;

		Player player = (Player) event.getDamager();

		Document deathLog = new Document()
			.append("name", player.getName())
			.append("uuid", player.getUniqueId().toString())
			.append("world", player.getWorld().getName())
			.append("location", plugin.locationToString(player.getLocation()))
			.append("damage", event.getFinalDamage())
			.append("cause", event.getCause().name())
			.append("damaged_entity", event.getEntity().getType().name())
			.append("timestamp", plugin.getUnixTimestamp());

		if (event.getEntity() instanceof Player) {
			Player damaged = (Player) event.getEntity();

			deathLog.append("damaged_name", damaged.getName())
				.append("damaged_uuid", damaged.getUniqueId().toString());
		}

		damageLogs.add(deathLog);
	}
}
