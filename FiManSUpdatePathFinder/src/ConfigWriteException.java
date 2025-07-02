@SuppressWarnings("serial")
public class ConfigWriteException extends Exception{
	public ConfigWriteException(String configFilePath) {
		super("ConfigWriteException\nFehler bei Erstellung der Config Datei:\n" + configFilePath);
	}
}
