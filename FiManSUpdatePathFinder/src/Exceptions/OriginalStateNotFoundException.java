package Exceptions;
@SuppressWarnings("serial")
public class OriginalStateNotFoundException extends Exception{
	public OriginalStateNotFoundException(String update) {
		super("OriginalStateNotFoundException\nFehler beim Setzen der Checkboxen!\nIn übergebenden (geladenen) Updates fehlt:\n\n" + update);
	}
}
