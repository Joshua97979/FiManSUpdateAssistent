package Exceptions;
@SuppressWarnings("serial")
public class ConfigEntryNotFoundException extends Exception{
	public ConfigEntryNotFoundException(String entry, String configFilePath) {
		super("ConfigEntryNotFoundException\nWichtiger Eintrag in Konfigurationsdatei nicht gefunden: " + entry + "\nunter: " + configFilePath + "\nFehlerbehebung: Löschen Sie die Konfigurationsdatei und starten Sie das Programm erneut.");
	}
}
