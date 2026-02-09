package src;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VerEntry {
	public int modNumber;
	public int version;
	public String component;
	public String library;
	public String sourceFile;
	public String action;
	
	public VerEntry(int modNumber, int version, String component, 
			String library, String sourceFile, String action) {
		this.modNumber = modNumber;
		this.version = version;
		this.component = component;
		this.library = library;
		this.sourceFile = sourceFile;
		this.action = action;
	}
	
	public String toString() {
		String versionStr = "";
		if (version != -1) versionStr = "" + version;
		
		String str = (component.substring(0, 9) + " | " + sourceFile.substring(0, 10) + " | " + action + " | Mod-Nr: " + modNumber + 
				" | Version: " + versionStr);
		
		if (library.trim().toUpperCase().equals("VSWIFC")) {
			str += " | VSWIFC";
		}
		return str;
	}
	
	private static int actionPriority(String action) {
        if (action == null) return 0;
        switch (action.toUpperCase().trim()) {
            case "CHG": return 3;
            case "ADD": return 2;
            case "CMP": return 1;
            default:    return 0;
        }
    }

    private static VerEntry chooseBetter(VerEntry a, VerEntry b) {
        int pa = actionPriority(a.action);
        int pb = actionPriority(b.action);
        if (pa != pb) return pa > pb ? a : b;
        if (a.version != b.version) return a.version > b.version ? a : b;
        if (a.modNumber != b.modNumber) return a.modNumber > b.modNumber ? a : b;
        return a; // deterministischer Fallback
    }

    public static List<VerEntry> filter(List<VerEntry> list) {
        Map<String, VerEntry> bestByComponent = list.stream()
            .collect(Collectors.toMap(
                e -> e.component,
                e -> e,
                (existing, incoming) -> chooseBetter(existing, incoming)
            ));

        // Optional: sortiert zurückgeben (z.B. nach component)
        return bestByComponent.values().stream()
                .sorted(Comparator.comparing(e -> e.component))
                .collect(Collectors.toList());
    }
}
