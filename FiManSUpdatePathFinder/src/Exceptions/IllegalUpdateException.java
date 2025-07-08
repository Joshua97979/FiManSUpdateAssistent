package Exceptions;

import src.Update;

@SuppressWarnings("serial")
public class IllegalUpdateException extends Exception{
	public IllegalUpdateException(Update update, String filePath) {
		super("IllegalUpdateException\nDas Update \"" + update.toString() + "\" ist kein gültiges Update\nFehlerbehebung: korrigieren Sie das betroffene Update in der XML-Datei:\n" + filePath);
	}
}