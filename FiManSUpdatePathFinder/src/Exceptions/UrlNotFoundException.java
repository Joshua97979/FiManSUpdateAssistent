package Exceptions;
@SuppressWarnings("serial")
public class UrlNotFoundException extends Exception{
	
	public UrlNotFoundException(String url) {
		super("UrlNotFoundException\nURL nicht gefunden: " + url);
	}
}
