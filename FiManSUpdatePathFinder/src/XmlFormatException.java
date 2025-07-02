@SuppressWarnings("serial")
public class XmlFormatException extends Exception{
	public XmlFormatException(String filePath, String exceptionText) {
		super("XmlFormatException\nFehler beim Lesen der Update-XML.\n\n" + exceptionText + "\n\nFehlerbehebung: korrigieren Sie die XML-Datei:\n" + filePath);
	}
}
