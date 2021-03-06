package dk.spirit55555.serverlogger.loggers;

import dk.spirit55555.serverlogger.ILogger;
import dk.spirit55555.serverlogger.ServerLogger;
import java.util.ArrayList;
import org.bson.Document;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerBlockPlaceLogger implements ILogger, Listener {
	private static final String NAME = "blockplace";
	private ServerLogger plugin;

	private final ArrayList<Document> blockLogs = new ArrayList<>();

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
		return blockLogs;
	}

	@Override
	public void resetData() {
		blockLogs.clear();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlockPlaced();
		Player player = event.getPlayer();

		Document blockLog = new Document()
			.append("name", player.getName())
			.append("uuid", player.getUniqueId().toString())
			.append("world", player.getWorld().getName())
			.append("location", plugin.locationToString(player.getLocation()))
			.append("block_type", block.getType().name())
			.append("block_location", plugin.locationToString(block.getLocation()))
			.append("timestamp", plugin.getUnixTimestamp());

		blockLogs.add(blockLog);
	}

}
