package Exceptions;
@SuppressWarnings("serial")
public class PassedStateNotFoundException extends Exception{
	public PassedStateNotFoundException(String update) {
		super("PassedStateNotFoundException\nFehler beim Setzen der Checkboxen!\nIn �bergebenden (geladenen) Updates sind unbekannte Updates:\n\n" + update);
	}
}
