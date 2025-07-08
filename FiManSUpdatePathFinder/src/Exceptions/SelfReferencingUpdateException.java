package Exceptions;

import src.Update;

@SuppressWarnings("serial")
public class SelfReferencingUpdateException extends Exception{
	
	Update update;
	
	public SelfReferencingUpdateException(String filePath, Update update) {
		super("SelfReferencingUpdateException\nUpdate: " + update.toString() + " verweist unter der Abhängigkeit auf sich selbst!\nFehlerbehebung: korrigieren Sie das betroffene Update in der XML-Datei:\n" + filePath);
		this.update = update;
	}
}
