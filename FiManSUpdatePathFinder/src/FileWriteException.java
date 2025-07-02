@SuppressWarnings("serial")
public class FileWriteException extends Exception{
	public FileWriteException(String filePath) {
		super("FileWriteException\nFehler beim Schreiben in Datei:\n" + filePath);
	}
}
