package dk.spirit55555.serverlogger;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import dk.spirit55555.serverlogger.loggers.ChatLogger;
import dk.spirit55555.serverlogger.loggers.ConsoleCommandLogger;
import dk.spirit55555.serverlogger.loggers.OnlinePlayersLogger;
import dk.spirit55555.serverlogger.loggers.PlayerCommandLogger;
import dk.spirit55555.serverlogger.loggers.PlayerDamageEntityLogger;
import dk.spirit55555.serverlogger.loggers.PlayerDeathLogger;
import dk.spirit55555.serverlogger.loggers.PlayerJoinLogger;
import dk.spirit55555.serverlogger.loggers.PlayerKillPlayerLogger;
import dk.spirit55555.serverlogger.loggers.PlayerQuitLogger;
import java.util.ArrayList;
import java.util.HashSet;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerLogger extends JavaPlugin {
	private final HashSet<ILogger> loggers = new HashSet<>();
	private MongoClient mongo;
	private MongoDatabase mongoDB;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		if (!getConfig().getBoolean("enabled", false)) {
			getLogger().severe(ChatColor.DARK_RED + "Plugin disabled, please enable it in config.yml");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		if (getConfig().getString("server-name").isEmpty()) {
			getLogger().severe(ChatColor.DARK_RED + "server-name can not be empty, please change it in config.yml");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		mongo = new MongoClient(new MongoClientURI(getConfig().getString("mongodb", "locahost:27017")));
		mongoDB = mongo.getDatabase(getConfig().getString("db-name", "serverlogs"));

		if (getConfig().getBoolean("loggers.chat"))
			addLogger(new ChatLogger());

		if (getConfig().getBoolean("loggers.consolecommand"))
			addLogger(new ConsoleCommandLogger());

		if (getConfig().getBoolean("loggers.onlineplayers"))
			addLogger(new OnlinePlayersLogger());

		if (getConfig().getBoolean("loggers.playercommand"))
			addLogger(new PlayerCommandLogger());

		if (getConfig().getBoolean("loggers.playerdamageentity"))
			addLogger(new PlayerDamageEntityLogger());

		if (getConfig().getBoolean("loggers.playerdeath"))
			addLogger(new PlayerDeathLogger());

		if (getConfig().getBoolean("loggers.playerjoin"))
			addLogger(new PlayerJoinLogger());

		if (getConfig().getBoolean("loggers.playerkillplayer"))
			addLogger(new PlayerKillPlayerLogger());

		if (getConfig().getBoolean("loggers.playerquit"))
			addLogger(new PlayerQuitLogger());

		runLoggers();
	}

	public int getUnixTimestamp() {
		return (int) (System.currentTimeMillis() / 1000L);
	}

	public String locationToString(Location location) {
		return location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ();
	}

	public String getServername() {
		String name = getConfig().getString("server-name", "");

		if (!name.isEmpty())
			name = name + "_";

		return name;
	}

	public void addLogger(ILogger logger) {
		logger.init(this);
		loggers.add(logger);
	}

	private void runLoggers() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (ILogger logger : loggers) {
					String name = logger.getName();
					ArrayList<Document> documents = logger.getData();

					if (!documents.isEmpty()) {
						mongoDB.getCollection(getServername() + name).insertMany(documents);
						logger.resetData();
					}
				}
			}
		}.runTaskTimer(this, (getConfig().getInt("timer", 15) * 20), (getConfig().getInt("timer", 15) * 20));
	}

	public String removeChatColors(String text) {
		return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', text));
	}
}
