package dk.spirit55555.serverlogger.loggers;

import dk.spirit55555.serverlogger.ILogger;
import dk.spirit55555.serverlogger.ServerLogger;
import java.util.ArrayList;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandLogger implements ILogger, Listener {
	private static final String NAME = "playercommand";
	private ServerLogger plugin;

	private ArrayList<Document> playerCommandLogs = new ArrayList<Document>();

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
		return playerCommandLogs;
	}

	@Override
	public void resetData() {
		playerCommandLogs.clear();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String command = event.getMessage();

		Document commandLog = new Document("command_raw", command)
			.append("command", ChatColor.stripColor(command))
			.append("name", player.getName())
			.append("uuid", player.getUniqueId().toString())
			.append("timestamp", plugin.getUnixTimestamp());

		playerCommandLogs.add(commandLog);
	}
}
