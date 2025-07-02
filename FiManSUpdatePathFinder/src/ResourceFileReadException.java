@SuppressWarnings("serial")
public class ResourceFileReadException extends Exception{
	public ResourceFileReadException(String fileName) {
		super("ResourceFileReadException\nFehler beim Lesen der Ressourcendatei: " + fileName);
	}
}
