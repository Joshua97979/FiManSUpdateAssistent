@SuppressWarnings("serial")
public class IllegalColorException extends Exception{
	public IllegalColorException(String name) {
		super("IllegalColorException\nWert f�r \"" + name + "\" ist keine g�ltige Farbe!");
	}
	public IllegalColorException(String name, String colorCode) {
		super("IllegalColorException\nDer Wert \"" + colorCode + "\" f�r \"" + name +  "\" ist keine g�ltige Farbe!");
	}
	
	public IllegalColorException(String name, String colorCode, String configFilePath) {
		super("IllegalColorException\nDer Wert \"" + colorCode + "\" f�r \"" + name +  "\" in der Configdatei ist keine g�ltige Farbe\nunter " + configFilePath + "\nFehlerbehebung: L�schen Sie die Konfigurationsdatei und starten Sie das Programm erneut.");
	}
}
