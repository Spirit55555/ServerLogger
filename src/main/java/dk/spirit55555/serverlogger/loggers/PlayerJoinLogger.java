package dk.spirit55555.serverlogger.loggers;

import dk.spirit55555.serverlogger.ServerLogger;
import java.util.ArrayList;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import dk.spirit55555.serverlogger.ILogger;

public class PlayerJoinLogger implements ILogger, Listener {
	private static final String NAME = "playerjoin";
	private ServerLogger plugin;

	private final ArrayList<Document> joinLogs = new ArrayList<>();

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
		return joinLogs;
	}

	@Override
	public void resetData() {
		joinLogs.clear();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		new BukkitRunnable() {
			int timestamp = plugin.getUnixTimestamp();

			@Override
			public void run() {
				Document join = new Document()
					.append("name", player.getName())
					.append("uuid", player.getUniqueId().toString())
					.append("ip", player.getAddress().getAddress().getHostAddress())
					.append("local", player.spigot().getLocale())
					.append("world", player.getWorld().getName())
					.append("location", plugin.locationToString(player.getLocation()))
					.append("timestamp", plugin.getUnixTimestamp());

				joinLogs.add(join);
			}
		}.runTaskLater(plugin, (5 * 20));

	}
}
