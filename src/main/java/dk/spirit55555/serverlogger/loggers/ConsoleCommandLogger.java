package dk.spirit55555.serverlogger.loggers;

import dk.spirit55555.serverlogger.ILogger;
import dk.spirit55555.serverlogger.ServerLogger;
import java.util.ArrayList;
import org.bson.Document;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class ConsoleCommandLogger implements ILogger, Listener {
	private static final String NAME = "consolecommand";
	private ServerLogger plugin;

	private ArrayList<Document> consoleCommandLogs = new ArrayList<Document>();

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
		return consoleCommandLogs;
	}

	@Override
	public void resetData() {
		consoleCommandLogs.clear();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onConsoleCommand(ServerCommandEvent event) {
		//Check if command should be ignored
		if (plugin.getConfig().getStringList("consolecommand-ignore").contains(event.getCommand()))
			return;

		//Add a slash, for the sake of consistency with PlayerCommandLogger
		String command = "/" + event.getCommand();

		Document commandLog = new Document("command", command)
			.append("timestamp", plugin.getUnixTimestamp());

		consoleCommandLogs.add(commandLog);
	}
}
