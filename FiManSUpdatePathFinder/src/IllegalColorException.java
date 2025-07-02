@SuppressWarnings("serial")
public class IllegalColorException extends Exception{
	public IllegalColorException(String name) {
		super("IllegalColorException\nWert für \"" + name + "\" ist keine gültige Farbe!");
	}
	public IllegalColorException(String name, String colorCode) {
		super("IllegalColorException\nDer Wert \"" + colorCode + "\" für \"" + name +  "\" ist keine gültige Farbe!");
	}
	
	public IllegalColorException(String name, String colorCode, String configFilePath) {
		super("IllegalColorException\nDer Wert \"" + colorCode + "\" für \"" + name +  "\" in der Configdatei ist keine gültige Farbe\nunter " + configFilePath + "\nFehlerbehebung: Löschen Sie die Konfigurationsdatei und starten Sie das Programm erneut.");
	}
}
