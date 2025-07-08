package Exceptions;
@SuppressWarnings("serial")
public class StateAmountException extends Exception{
	public StateAmountException(String update) {
		super("StateAmountException\nFehler beim Setzen der Checkboxen!\nAnzahl Checkboxen unterschiedlich zwischen Update aus Liste und übergebenden (geladenen) Update:\n\n" + update);
	}
}
