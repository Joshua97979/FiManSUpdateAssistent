package src;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class XMLUpdateWriter {

	private File file;
	String content = "";
	
	public XMLUpdateWriter(String filename) throws FileNotFoundException {
		/*All of this comes after the updates are initialized.
		 * So it's pretty safe to assume that everything is fine with the UpdateXML file.
		 * (Unless the user modifies the UpdateXML file between displaying the "new updates found"-message and pressing the add button.)*/
		
		this.file = new File(filename);
		if (!file.exists()) throw new FileNotFoundException();
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	content += line + "\n";
		    }
		    br.close();
		} catch (IOException e1 ) {return;}
		    
	}
	
	public void addUpdateToXML(Update update) {
		ArrayList<Update> updates = new ArrayList<Update>();
		updates.add(update);
		this.addUpdatesToXML(updates);
	}

	public void addUpdatesToXML(ArrayList<Update> newUpdates) {
		//Intended only for ASS and JAM updates
		String newContent = content;
		
		for (int i = 0; i < newUpdates.size(); i++) {
			String newUpdateStr = getUpdateAsXMLString(newUpdates.get(i));
			String tmpContent = "";
			if (newUpdates.get(i).getType().equals("ASS")) {
				tmpContent = newContent.substring(0, newContent.indexOf("</assUpdates>"));
				tmpContent += newUpdateStr;
				tmpContent += "\t" + newContent.substring(newContent.indexOf("</assUpdates>"));
			} else if (newUpdates.get(i).getType().equals("JAM")) {
				tmpContent = newContent.substring(0, newContent.indexOf("</jamUpdates>"));
				tmpContent += newUpdateStr;
				tmpContent += "\t" + newContent.substring(newContent.indexOf("</jamUpdates>"));
			} else {
				return;
			}
			newContent = tmpContent;
		}
		
		if (file.delete()) { 
			System.out.println("Deleted the file: " + file.getName());
	    } else {
	    	System.out.println("Failed to delete the file.");
	    	return;
	    }
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(newContent);
			writer.flush();
			writer.close();
			System.out.println("Created the file: " + file.getName());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fehler bei erstellung der UpdateXML-Datei (Das System kann den angegebenen Pfad nicht finden)");
		}
	}
	
	private static String getUpdateAsXMLString(Update update) {
		if (update == null) return "";
		
		String returnStr = "\t<update>\n"
				+ "\t\t\t<type>" + update.getType() + "</type>\n"
				+ "\t\t\t<version>" + update.getVersion() + "</version>\n"
				+ "\t\t\t<releasedate>" + update.getReleasedate() + "</releasedate>\n";
		if (update.hasLink()) {
			returnStr += "\t\t\t<link>" + update.getLink() + "</link>\n";
		}
		if (update.hasAssDependency()) {
			returnStr += "\t\t\t<dependency>\n"
				+ "\t\t\t\t<dependentType>" + update.getAssDependency().getType() + "</dependentType>\n"
				+ "\t\t\t\t<dependentVersion>" + update.getAssDependency().getVersion() + "</dependentVersion>\n"
				+ "\t\t\t\t<dependentReleasedate>" + update.getAssDependency().getReleasedate() + "</dependentReleasedate>\n"
				+ "\t\t\t</dependency>\n";
		}
		if (update.hasJamDependency()) {
			
			//Veda still hasn't corrected the typo in Update 6310:
			if (update.getJamDependency().getVersion() == 6310 && update.getJamDependency().getReleasedate().equals("28.04.2013")) {
				update.setJamDependency(new Update("JAM", 6310, "28.04.2023"));
			}
			
			returnStr += "\t\t\t<dependency>\n"
				+ "\t\t\t\t<dependentType>" + update.getJamDependency().getType() + "</dependentType>\n"
				+ "\t\t\t\t<dependentVersion>" + update.getJamDependency().getVersion() + "</dependentVersion>\n"
				+ "\t\t\t\t<dependentReleasedate>" + update.getJamDependency().getReleasedate() + "</dependentReleasedate>\n"
				+ "\t\t\t</dependency>\n";
		}
		returnStr += "\t\t</update>\n";
		return returnStr;
	}
}
