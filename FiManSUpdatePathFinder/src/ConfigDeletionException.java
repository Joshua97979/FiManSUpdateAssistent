@SuppressWarnings("serial")
public class ConfigDeletionException extends Exception{
	public ConfigDeletionException(String configFilePath) {
		super("ConfigDeletionException\nFehler beim L�schen der Config Datei:\n" + configFilePath + "\n(Datei wird eventuell noch von einem anderen Prozess verwendet?)");
	}
}
