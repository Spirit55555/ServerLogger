package dk.spirit55555.serverlogger.loggers;

import dk.spirit55555.serverlogger.ILogger;
import dk.spirit55555.serverlogger.ServerLogger;
import java.util.ArrayList;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatLogger implements ILogger, Listener {
	private static final String NAME = "chat";
	private ServerLogger plugin;

	private final ArrayList<Document> chatLogs = new ArrayList<>();

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
		return chatLogs;
	}

	@Override
	public void resetData() {
		chatLogs.clear();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();

		Document chat = new Document()
			.append("message_raw", event.getMessage())
			.append("message", plugin.removeChatColors(event.getMessage()))
			.append("name", player.getName())
			.append("uuid", player.getUniqueId().toString())
			.append("world", player.getWorld().getName())
			.append("location", plugin.locationToString(player.getLocation()))
			.append("timestamp", plugin.getUnixTimestamp());

		chatLogs.add(chat);
	}
}
