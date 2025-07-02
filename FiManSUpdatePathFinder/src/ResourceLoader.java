import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ResourceLoader {

	public static String loadResource(String fileName) throws FileNotFoundException, ResourceFileReadException {
        InputStream stream = ResourceLoader.class.getClassLoader().getResourceAsStream(fileName);
        if (stream == null) {
        	throw new FileNotFoundException("Keine Ressourcendatei mit folgendem Namen gefunden: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
        	throw new ResourceFileReadException(fileName);
        }
    }
}
