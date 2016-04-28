package dk.spirit55555.serverlogger;

import java.util.ArrayList;
import org.bson.Document;

public interface ILogger {
	public void init(ServerLogger plugin);

	public String getName();

	public ArrayList<Document> getData();

	public void resetData();
}