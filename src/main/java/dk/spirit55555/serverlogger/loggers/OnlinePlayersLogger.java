package dk.spirit55555.serverlogger.loggers;

import dk.spirit55555.serverlogger.ILogger;
import dk.spirit55555.serverlogger.ServerLogger;
import java.util.ArrayList;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;

public class OnlinePlayersLogger implements ILogger {
	private static final String NAME = "onlineplayers";

	private final ArrayList<Document> onlinePlayersLogs = new ArrayList<>();

	@Override
	public void init(ServerLogger plugin) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document onlinePlayersLog = new Document()
					.append("players", plugin.getServer().getOnlinePlayers().size())
					.append("max_players", plugin.getServer().getMaxPlayers())
					.append("timestamp", plugin.getUnixTimestamp());

				onlinePlayersLogs.add(onlinePlayersLog);
			}
		}.runTaskTimer(plugin, 0, (plugin.getConfig().getInt("onlineplayers.interval", 10) * 20));
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public ArrayList<Document> getData() {
		return onlinePlayersLogs;
	}

	@Override
	public void resetData() {
		onlinePlayersLogs.clear();
	}
}
