package Exceptions;

import src.Update;

@SuppressWarnings("serial")
public class UpdateNotFoundException extends Exception{
	
	public Update update;
	
	public UpdateNotFoundException(String filePath, Update update) {
		super("UpdateNotFoundException\nUpdate: " + update.toString() + " nicht gefunden in XML-Datei: " + filePath + "\nFehlerbehebung: Pflegen Sie das Update in die XML-Datei ein. (Eventuell ein FaultyUpdate?)");
		this.update = update;
	}
}
