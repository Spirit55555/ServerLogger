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
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBlockBreakLogger implements ILogger, Listener {
	private static final String NAME = "blockbreak";
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
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();

		Document blockLog = new Document()
			.append("item", player.getInventory().getItemInMainHand().getType().name())
			.append("name", player.getName())
			.append("uuid", player.getUniqueId().toString())
			.append("world", player.getWorld().getName())
			.append("location", plugin.locationToString(player.getLocation()))
			.append("block_type", block.getType().name())
			.append("block_location", plugin.locationToString(block.getLocation()))
			.append("block_drops_count", block.getDrops().size())
			.append("block_xp_drop", event.getExpToDrop())
			.append("timestamp", plugin.getUnixTimestamp());

		blockLogs.add(blockLog);
	}
}
