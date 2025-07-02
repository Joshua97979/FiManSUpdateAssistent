//By Joshua Froitzheim 2023
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Config {

	private File configFile;
	private String pathToUpdates;
	private Color backgroundColor;
	private Color secondaryBackgroundColor;
	private Color foregroundColor;
	public Float fontSize;
	
	public String pathToFibUpdateFiles;
	public String pathToFibReleaseFiles;
	public String pathToAssFiles;
	public String pathToJamFiles;
	
	private String defaultBackgroundColor = "#EEEEEE";
	private String defaultSecondaryBackgroundColor = "#cccccc";
	private String defaultForegroundColor = "#000000";
	private Float defaultFontSize = 1.4f;
	public boolean updatesChckbxState;
	public boolean optionalUpdatesChckbxState;
	public boolean maintainScrollPosChckbxState;
	
	private Color secondaryForegroundColor;
	private String defaultSecondaryForegroundColor = "#000000";
	
	public Config(String pathToConfigFile) throws ConfigEntryNotFoundException, IllegalColorException, ConfigWriteException, ConfigDeletionException {
		
		configFile = new File(pathToConfigFile);
		
		if (configFile.exists() == false) {
			this.pathToUpdates = System.getProperty("user.home") + "/AppData/Roaming/FiManSUpdateAssistent/UpdatesData.xml";
			this.setBackgroundColor(defaultBackgroundColor);
			this.setSecondaryBackgroundColor(defaultSecondaryBackgroundColor);
			this.setForegroundColor(defaultForegroundColor);
			this.setSecondaryForegroundColor(defaultSecondaryForegroundColor);
			this.fontSize = defaultFontSize;
			this.updatesChckbxState = true;
			this.optionalUpdatesChckbxState = true;
			this.maintainScrollPosChckbxState = true;
			
			this.pathToFibUpdateFiles = "F:\\FiManS Team\\Releases\\A_FIB";
			this.pathToFibReleaseFiles = "F:\\FiManS Team\\Releases\\A_FIB";
			this.pathToAssFiles = "F:\\FiManS Team\\Releases\\A_ASS";
			this.pathToJamFiles = "F:\\FiManS Team\\Releases\\A_JUMP";
			
			saveToFile();
		}
		
		String content = "";
		
		try(BufferedReader br = new BufferedReader(new FileReader(configFile))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	content += line;
		    }
		    br.close();
		} catch (IOException e1 ) {return;}
		ArrayList<String> elements = new ArrayList<String>();
		
		//////////////////////pathToUpdates//////////////////////////////
		elements.add("pathToUpdates");
		String textBetween = getTextBetween("pathToUpdates", content);
		if (textBetween != null) pathToUpdates = textBetween;
		
		//////////////////////backgroundColor//////////////////////////////
		elements.add("backgroundColor");
		textBetween = getTextBetween("backgroundColor", content);
		if (textBetween != null) {
			try {
				setBackgroundColor(textBetween);
			} catch (IllegalColorException e1) {
				setBackgroundColor(defaultBackgroundColor);
				throw new IllegalColorException("Hintergrundfarbe", textBetween, pathToConfigFile);
			}
		}
		
		//////////////////////secondaryBackgroundColor//////////////////////////////
		elements.add("secondaryBackgroundColor");
		textBetween = getTextBetween("secondaryBackgroundColor", content);
		if (textBetween != null) {
			try {
				setSecondaryBackgroundColor(textBetween);
			} catch (IllegalColorException e1) {
				setSecondaryBackgroundColor(defaultBackgroundColor);
				throw new IllegalColorException("Sekundäre Hintergrundfarbe", textBetween, pathToConfigFile);
			}
		}
		
		//////////////////////secondaryForegroundColor//////////////////////////////
		elements.add("secondaryForegroundColor");
		textBetween = getTextBetween("secondaryForegroundColor", content);
		if (textBetween != null) {
			try {
				setSecondaryForegroundColor(textBetween);
			} catch (IllegalColorException e1) {
				setSecondaryForegroundColor(defaultBackgroundColor);
				throw new IllegalColorException("Sekundäre Schriftfarbe", textBetween, pathToConfigFile);
			}
		}
		
		
		
		//////////////////////foregroundColor//////////////////////////////
		elements.add("foregroundColor");
		textBetween = getTextBetween("foregroundColor", content);
		if (textBetween != null) {
			try {
				setForegroundColor(textBetween);
			} catch (IllegalColorException e1) {
				setForegroundColor(defaultForegroundColor);
				throw new IllegalColorException("Schriftfarbe", textBetween, pathToConfigFile);
			}
		}
		
		//////////////////////fontSize//////////////////////////////
		elements.add("fontSize");
		textBetween = getTextBetween("fontSize", content);
		if (textBetween != null) {
			try {
				fontSize = Float.parseFloat(textBetween);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Schriftgröße in Config ungültig");
				fontSize = defaultFontSize;
			}
		}
		
		//////////////////////updatesChckbxState//////////////////////////////
		elements.add("updatesChckbxState");
		textBetween = getTextBetween("updatesChckbxState", content);
		if (textBetween != null) {
			updatesChckbxState = Boolean.parseBoolean(textBetween);
		}
		
		//////////////////////optionalUpdatesChckbxState//////////////////////////////
		elements.add("optionalUpdatesChckbxState");
		textBetween = getTextBetween("optionalUpdatesChckbxState", content);
		if (textBetween != null) {
			optionalUpdatesChckbxState = Boolean.parseBoolean(textBetween);
		}
		
		//////////////////////maintainScrollPosChckbxState//////////////////////////////
		elements.add("maintainScrollPosChckbxState");
		textBetween = getTextBetween("maintainScrollPosChckbxState", content);
		if (textBetween != null) {
			maintainScrollPosChckbxState = Boolean.parseBoolean(textBetween);
		}
		
		//////////////////////pathToFibUpdateFiles//////////////////////////////
		elements.add("pathToFibUpdateFiles");
		textBetween = getTextBetween("pathToFibUpdateFiles", content);
		if (textBetween != null) {
			pathToFibUpdateFiles = textBetween;
		}
		
		//////////////////////pathToFibReleaseFiles//////////////////////////////
		elements.add("pathToFibReleaseFiles");
		textBetween = getTextBetween("pathToFibReleaseFiles", content);
		if (textBetween != null) {
			pathToFibReleaseFiles = textBetween;
		}
		
		//////////////////////pathToAssFiles//////////////////////////////
		elements.add("pathToAssFiles");
		textBetween = getTextBetween("pathToAssFiles", content);
		if (textBetween != null) {
			pathToAssFiles = textBetween;
		}
		
		//////////////////////pathToAssFiles//////////////////////////////
		elements.add("pathToJamFiles");
		textBetween = getTextBetween("pathToJamFiles", content);
		if (textBetween != null) {
			pathToJamFiles = textBetween;
		}
		
		
		
		for (String element : elements) {
			if (getTextBetween(element, content) == null) {
				throw new ConfigEntryNotFoundException(element, configFile.getAbsolutePath());
			}
		}
	}
	
	private String getTextBetween(String xmlTag, String xmlContent) {
		int begin = xmlContent.indexOf("<" + xmlTag + ">") + 2 + xmlTag.length();
		int end = xmlContent.indexOf("</" + xmlTag + ">");
		if (begin == -1 || end == -1) return null;
		return xmlContent.substring(begin, end);
	}
	
	public String getConfigFilePath() {
		return this.configFile.getAbsolutePath();
	}

	public void saveToFile() throws ConfigWriteException, ConfigDeletionException {
		if (configFile.exists() == false) {
			Object[] options = new Object[] {"Erstellen", "Abbruch"};
			int selectedValue = JOptionPane.showOptionDialog(null, "Das Programm hat keine Config Datei bei folgedem Pfad gefunden:\n" + configFile.getAbsolutePath() + "\n\nDatei erstellen?",
					"Option auswählen",JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, options, options[0]);
			
			if (selectedValue == 1) { //dont create
				System.exit(0);
			}
		}
		
		if (configFile.exists() == true) {
			if (configFile.delete()) { 
				System.out.println("Deleted the file: " + configFile.getName());
		    } else {
		    	throw new ConfigDeletionException(configFile.getAbsolutePath());
		    }
		}	
		
		try {
			boolean created = configFile.getParentFile().mkdirs();
			if (created) System.out.println("ParentFodler created");
			
			if (!configFile.createNewFile()) throw new ConfigWriteException(configFile.getAbsolutePath());
			FileWriter writer = new FileWriter(configFile);
			writer.write("<pathToUpdates>"+ this.pathToUpdates +"</pathToUpdates>\n");
			writer.write("<backgroundColor>"+ this.getBackgroundColorCode() +"</backgroundColor>\n");
			writer.write("<secondaryBackgroundColor>"+ this.getSecondaryBackgroundColorCode() +"</secondaryBackgroundColor>\n");
			writer.write("<foregroundColor>"+ this.getForegroundColorCode() +"</foregroundColor>\n");
			writer.write("<secondaryForegroundColor>"+ this.getSecondaryForegroundColorCode() +"</secondaryForegroundColor>\n");
			writer.write("<fontSize>"+ this.fontSize.toString() +"</fontSize>\n");
			writer.write("<updatesChckbxState>"+ this.updatesChckbxState +"</updatesChckbxState>\n");
			writer.write("<optionalUpdatesChckbxState>"+ this.optionalUpdatesChckbxState +"</optionalUpdatesChckbxState>\n");
			writer.write("<maintainScrollPosChckbxState>"+ this.maintainScrollPosChckbxState +"</maintainScrollPosChckbxState>\n");
			writer.write("<pathToFibUpdateFiles>"+ this.pathToFibUpdateFiles +"</pathToFibUpdateFiles>\n");
			writer.write("<pathToFibReleaseFiles>"+ this.pathToFibReleaseFiles +"</pathToFibReleaseFiles>\n");
			writer.write("<pathToAssFiles>"+ this.pathToAssFiles +"</pathToAssFiles>\n");
			writer.write("<pathToJamFiles>"+ this.pathToJamFiles +"</pathToJamFiles>\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new ConfigWriteException(configFile.getAbsolutePath());
		}
	}
	
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}
	
	public void setSecondaryBackgroundColor(Color color) {
		this.secondaryBackgroundColor = color;
	}
	
	public void setSecondaryForegroundColor(Color color) {
		this.secondaryForegroundColor = color;
	}

	public void setBackgroundColor(String colorCode) throws IllegalColorException {
		try { setBackgroundColor(Color.decode(colorCode)); } catch (Exception e) {
			throw new IllegalColorException("Hintergrundfarbe", colorCode);
		}
	}
	
	public void setSecondaryBackgroundColor(String colorCode) throws IllegalColorException {
		try { setSecondaryBackgroundColor(Color.decode(colorCode)); } catch (Exception e) {
			throw new IllegalColorException("Sekundäre Hintergrundfarbe", colorCode);
		}
	}
	
	private void setSecondaryForegroundColor(String colorCode) throws IllegalColorException {
		try { setSecondaryForegroundColor(Color.decode(colorCode)); } catch (Exception e) {
			throw new IllegalColorException("Sekundäre Schriftfarbe", colorCode);
		}
	}
	
	public void setForegroundColor(Color color) {
		this.foregroundColor = color;
	}

	public void setForegroundColor(String colorCode) throws IllegalColorException {
		try { setForegroundColor(Color.decode(colorCode)); } catch (Exception e) {
			throw new IllegalColorException("Hintergrundfarbe", colorCode);
		}
	}
	
	public static String colorToColorCode(Color color) {
		return ("#"+Integer.toHexString(color.getRGB()).substring(2));
	}

	public String getBackgroundColorCode() {
		return colorToColorCode(backgroundColor);
	}
	
	public String getSecondaryBackgroundColorCode() {
		return colorToColorCode(secondaryBackgroundColor);
	}

	public String getForegroundColorCode() {
		return colorToColorCode(foregroundColor);
	}
	
	public String getSecondaryForegroundColorCode() {
		return colorToColorCode(secondaryForegroundColor);
	}

	public Color getBackgroundColor() {
		return this.backgroundColor;
	}
	
	public Color getSecondaryBackgroundColor() {
		return this.secondaryBackgroundColor;
	}

	public Color getForegroundColor() {
		return this.foregroundColor;
	}
	
	public Color getSecondaryForegroundColor() {
		return this.secondaryForegroundColor;
	}

	public String getPathToUpdates() {
		return this.pathToUpdates;
	}

	public void setPathToUpdates(String pathToUpdates) throws FileNotFoundException {
		if (!new File(pathToUpdates).isFile()) {throw new FileNotFoundException("Keine Datei an angegebenem Pfad gefunden:\n" + pathToUpdates);}
		this.pathToUpdates = pathToUpdates;
	}
}
