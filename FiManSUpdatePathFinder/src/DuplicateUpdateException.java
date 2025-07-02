import java.util.ArrayList;

@SuppressWarnings("serial")
public class DuplicateUpdateException extends Exception{
	
	public DuplicateUpdateException(Update update, String filePath) {
		super("DuplicateUpdateException\nDas Update \"" + update.toString() + "\" ist doppelt vorhanden!\nFehlerbehebung: Entfernen Sie das betroffene Update aus der XML-Datei:\n" + filePath);
	}
	
	public DuplicateUpdateException(ArrayList<Update> updates, String filePath) {
		super("DuplicateUpdateException\nDie folgenden Updates sind doppelt vorhanden: \"" + updates.toString() + "!\"\nFehlerbehebung: Entfernen Sie die betroffenen Updates aus der XML-Datei:\n" + filePath);
	}
	
	public static DuplicateUpdateException create(ArrayList<Update> updates, String filePath) {
        if (updates.size() == 1) {
            return new DuplicateUpdateException(updates.get(0), filePath);
        } else {
            return new DuplicateUpdateException(updates, filePath);
        }
    }
}