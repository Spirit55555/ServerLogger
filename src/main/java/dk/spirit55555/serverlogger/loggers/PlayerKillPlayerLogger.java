package dk.spirit55555.serverlogger.loggers;

import dk.spirit55555.serverlogger.ILogger;
import dk.spirit55555.serverlogger.ServerLogger;
import java.util.ArrayList;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKillPlayerLogger implements ILogger, Listener {
	private static final String NAME = "playerkillplayer";
	private ServerLogger plugin;

	private final ArrayList<Document> killLogs = new ArrayList<>();

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
		return killLogs;
	}

	@Override
	public void resetData() {
		killLogs.clear();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void playerDeathEvent(PlayerDeathEvent event) {
		if (event.getEntity().getKiller() == null)
			return;

		Player killer = event.getEntity().getKiller();
		Player killed = event.getEntity();

		Document killLog = new Document()
			.append("name", killer.getName())
			.append("uuid", killer.getUniqueId().toString())
			.append("health", killer.getHealth())
			.append("food", killer.getFoodLevel())
			.append("item", killer.getInventory().getItemInMainHand().getType().name())
			.append("world", killer.getWorld().getName())
			.append("location", plugin.locationToString(killer.getLocation()))
			.append("cause", killed.getLastDamageCause().getCause().name())
			.append("killed_name", killed.getName())
			.append("killed_uuid", killed.getUniqueId())
			.append("timestamp", plugin.getUnixTimestamp());

		killLogs.add(killLog);
	}
}
