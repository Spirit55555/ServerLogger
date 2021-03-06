package dk.spirit55555.serverlogger.loggers;

import dk.spirit55555.serverlogger.ILogger;
import dk.spirit55555.serverlogger.ServerLogger;
import java.util.ArrayList;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitLogger implements ILogger, Listener {
	private static final String NAME = "playerquit";
	private ServerLogger plugin;

	private final ArrayList<Document> quitLogs = new ArrayList<>();

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
		return quitLogs;
	}

	@Override
	public void resetData() {
		quitLogs.clear();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		Document join = new Document()
			.append("name", player.getName())
			.append("uuid", player.getUniqueId().toString())
			.append("world", player.getWorld().getName())
			.append("location", plugin.locationToString(player.getLocation()))
			.append("timestamp", plugin.getUnixTimestamp());

		quitLogs.add(join);
	}
}
