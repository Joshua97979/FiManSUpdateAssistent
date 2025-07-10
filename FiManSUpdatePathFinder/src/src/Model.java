package src;
//By Joshua Froitzheim 2023
//Depends on Java 1.8.0_261
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.xml.sax.SAXException;

import Exceptions.ConfigDeletionException;
import Exceptions.ConfigEntryNotFoundException;
import Exceptions.ConfigWriteException;
import Exceptions.DuplicateUpdateException;
import Exceptions.FileWriteException;
import Exceptions.IllegalColorException;
import Exceptions.IllegalUpdateException;
import Exceptions.PassedStateNotFoundException;
import Exceptions.ResourceFileReadException;
import Exceptions.SelfReferencingUpdateException;
import Exceptions.UpdateNotFoundException;
import Exceptions.UrlNotFoundException;
import Exceptions.XmlFormatException;
import gui.ASSReleaseDescriptionPanel;
import gui.ASSdescriptionPanel;
import gui.DescriptionPanel;
import gui.FIBReleaseDescriptionPanel;
import gui.FIBdescriptionPanel;
import gui.JAMdescriptionPanel;
import gui.OptionPanel;

public class Model {
	
	private static String version = "V1.3";
	
	private Config configFile;
	
	private Controller controller;
	private String[] types = {"ASS", "JAM", "FIB"};

	private ArrayList<Update> assUpdates;
	private ArrayList<Update> jamUpdates;
	private ArrayList<Update> fibUpdates;
	
	private String selectedType;
	private Update selectetTargetUpdate;
	
	private Update installedJam;
	private Update installedAss;
	private Update installedFib;
	
	private ArrayList<Update> selectableInsJamUpdates;
	private ArrayList<Update> selectableInsAssUpdates;
	private ArrayList<Update> selectableInsFibUpdates;
	
	private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	private ArrayList<Update> path;
	private ArrayList<Update> optionalPath;
	
	private static String LAST_USED_FOLDER_KEY;
	private static String LAST_PROGRAM_START_KEY;
	
	private String startingData;
	
	private Update[] ASSreleasess;
	
	Map<String, String> userInputs = new HashMap<>();
	Map<Update, ArrayList<Boolean>> boxStates = new HashMap<>();
	
	private static final String[] inputs = {"fibPgmBib", "fibDtaBib", "fibSrcBib", "assDtaBib", "assPgmBib", "aesPgmBib", "aesDtaBib", "optName", "imgClgName", "imgClgPath", "jdkVersion",
			   "osVersion", "tomCatDownCommand", "tomCatUpCommand", "tomCatVersion", "tomCatJobName", "fibInsDirPath", "tomCatInsDirPath", "fibModBib"};
	
	public static void main(String[] args) {
		new Model();
	}
	
	public Model(){
		LAST_USED_FOLDER_KEY = "lastUsedFolder";
		LAST_PROGRAM_START_KEY = "lastProgramStart";
		
		ASSreleasess = new Update[] {
				new Update("ASS", 5810, "25.06.2018"), 
				new Update("ASS", 5710, "09.06.2017"),
				new Update("ASS", 5620, "09.12.2016"),
				new Update("ASS", 5610, "10.06.2016"),
				new Update("ASS", 5520, "11.12.2015"),
				new Update("ASS", 5510, "12.06.2015"),
				new Update("ASS", 5420, "12.12.2014"),
				new Update("ASS", 5410, "13.06.2014"),
				new Update("ASS", 5320, "13.12.2013"),
				new Update("ASS", 5310, "14.06.2013"),
				new Update("ASS", 5220, "14.12.2012"),
				new Update("ASS", 5210, "15.06.2012"),
				new Update("ASS", 5120, "09.12.2011"),
				new Update("ASS", 5110, "10.06.2011"),
				new Update("ASS", 5020, "10.12.2010"),
				new Update("ASS", 5010, "11.06.2010"),
				new Update("ASS", 4920, "11.12.2009")};
		
		
		selectedType = null;
		selectetTargetUpdate = null;
		installedJam = null;
		installedAss = null;
		installedFib = null;
		
		for (int i = 0; i < inputs.length; i++) {
			if (inputs[i].equals("optName")) {
				userInputs.put(inputs[i], "OPTVEDA");
			} else if (inputs[i].equals("imgClgName")) {
				userInputs.put(inputs[i], "VEDAIMAGES");
			} else {
				userInputs.put(inputs[i], "");
			}
		}
		
		// In case of an error, unlike the update XML file, the config file must be deleted because changes to the config file cannot be saved anywhere, and the config file may be incomplete if a save attempt is made.
		try {
			
			String filePath = "";
			if (System.getProperty("os.name").startsWith("Windows")) {
				filePath = System.getProperty("user.home") + "/AppData/Roaming/FiManSUpdateAssistent/FiManSUpdateAssistentConfig.txt";
			} else {
				JOptionPane.showMessageDialog(null, "Aktuell wird nur Windows für dieses Programm unterstützst."); //controller is null at this point
			}
			
			configFile = new Config(filePath);
		} catch (ConfigEntryNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage()); //controller is null at this point
			System.exit(0);
		} catch (IllegalColorException e) {
			JOptionPane.showMessageDialog(null, e.getMessage()); //controller is null at this point
			System.exit(0);
		} catch (ConfigWriteException | ConfigDeletionException e) {
			JOptionPane.showMessageDialog(null, e.getMessage()); //controller is null at this point
			System.exit(0);
		}
		this.controller = new Controller(this);
		handleInitUpdates();
		controller.setTypes(types);
		
		if (configFile.updatesChckbxState) {
			LocalDate lastProgramStart = loadDateFromPreferences();
			
			if (lastProgramStart == null) { // Frist time
				saveValueToPreferences(LAST_PROGRAM_START_KEY, LocalDate.MIN.toString());
				lastProgramStart = loadDateFromPreferences();
			} 
			
			if (!lastProgramStart.equals(LocalDate.now())) {
				try {
					checkForNewUpdatesFromVEDA();
				} catch (UrlNotFoundException e) {
					controller.showMessageDialog(e.getMessage());
				}
				saveValueToPreferences(LAST_PROGRAM_START_KEY, LocalDate.now().toString());
			}
			
		}
		startingData = getDataToSave();
	}
	
	private LocalDate loadDateFromPreferences() {
		String lastProgramStartString = getPreferencesValue(LAST_PROGRAM_START_KEY);
		if (lastProgramStartString == null) return null;
		return LocalDate.parse(lastProgramStartString);
	}
	
	public void checkForVedaUpdatesNow() {
		try {
			if (!checkForNewUpdatesFromVEDA()) controller.showMessageDialog("Keine neuen Updates bei VEDA gefunden.");
		} catch (UrlNotFoundException e) {
			controller.showMessageDialog(e.getMessage());
		}
	}

	private boolean checkForNewUpdatesFromVEDA() throws UrlNotFoundException {
		UpdateChecker updateChecker = new UpdateChecker(jamUpdates, assUpdates);
		ArrayList<Update> newUpdates = updateChecker.getNewUpdates(); //Dependencies are all new objects!
		if (newUpdates.size() <= 0) return false;
		
		int selectedValue = UpdateChecker.showNewUpdates(newUpdates, configFile.getPathToUpdates());
		if (selectedValue != 0) return true;
		addNewUpdates(newUpdates);
		return true;
	}
	
	private void catchFaultyUpdates(ArrayList<Update> newUpdates) {
		//Veda made a mistake and uploaded an update without the correct description, name, etc.
		//This function corrects this issue during the import into the UpdatesData.xml after a new update is found on the website.
		Map<Update, Update> faultyUpdates = null; //faulty, valid
		try {
			XmlUpdateReader xur = new XmlUpdateReader(configFile.getPathToUpdates(), this, false);
			faultyUpdates = xur.getFaultyUpdates(); //Dependencies are all new objects!
		} catch (SAXException | FileNotFoundException e) {
			e.printStackTrace();
		}
		if (faultyUpdates == null) return;
		
		for (Map.Entry<Update, Update> entry : faultyUpdates.entrySet()) {
			Update faultyUpdate = entry.getKey();
			Update validUpdate = entry.getValue();
			if (newUpdates.contains(faultyUpdate)) {
				System.out.println("faultyUpdate: " + faultyUpdate);
				newUpdates.set(newUpdates.indexOf(faultyUpdate), validUpdate);
			}
		}
	}
	
	private void addNewUpdates(ArrayList<Update> newUpdates) {
		//Dependencies are all new objects!
		
		catchFaultyUpdates(newUpdates);
		
		try {
			String cacheData = getDataToSave();
			new XMLUpdateWriter(configFile.getPathToUpdates()).addUpdatesToXML(newUpdates);
			handleInitUpdates();
			String typ = stringBetween(cacheData, "selectedType");
			if ((typ.length() <= 0) == false) {
				forceLoadData(cacheData);
			}
			
			controller.showMessageDialog("Neue Updates hinzugefügt.");
		} catch (FileNotFoundException e) {e.printStackTrace();}
	}

	public ArrayList<Update> getPath(Update taget){
		ArrayList<Update> path = new ArrayList<Update>();
		
		if (taget.getType().equals("JAM")) {
			path.addAll(getPathBetweenOldAndNew(jamUpdates.get(jamUpdates.size()-1), taget, jamUpdates));
		}else if (taget.getType().equals("ASS")) {
			Update current = taget;
			while (true) {
				if (current.getJamDependency() != null) { //The current update has a JAM dependency
					path.add(current);
					if (getNextASSUpdateWithJAM(current) == null) { // There is no next update with JAM
						path.addAll(getPathBetweenOldAndNew(jamUpdates.get(jamUpdates.size()-1), current.getJamDependency(), jamUpdates));
					} else {
						ArrayList<Update> tmp = getPathBetweenOldAndNew(getNextASSUpdateWithJAM(current).getJamDependency(), current.getJamDependency(), jamUpdates);
						if (tmp.size() > 1) {
							tmp.remove(tmp.size()-1); //Remove last update as it will be added later (next time at this else statement)
						}
						path.addAll(tmp);
					}
				} else { //The current update has no JAM dependency
					path.add(current);
				}
				
				if (current.getAssDependency() == null) break; //the current update is the last update: Break
				current = current.getAssDependency();
			}
		} else if (taget.getType().equals("FIB")) {
			
			path.addAll(getPathBetweenOldAndNew(fibUpdates.get(fibUpdates.size()-1), taget, fibUpdates));
			if (controller.appChckbxIsSelected()) {
				path.addAll(getPath(assUpdates.get(0)));
			} else {
				path.addAll(getPath(taget.getAssDependency()));
			}
		}
		return path;
	}

	private ArrayList<Update> trimPath(ArrayList<Update> path, Update installedUpdate) {
		for (int i = path.size() - 1; i >= 0; i--) {
			if (path.get(i).getType().equals(installedUpdate.getType()) == false) continue;
			
			//TODO hier besser eine Update "Before" oder "in ASS Release" einbauen um das ganze sauberer und intuitiver zu gestallten.
			if (path.get(i).equals(new Update("JAM", 5711, "08.08.2017")) && installedUpdate.equals(new Update("JAM", 5711, "08.08.2017"))) break;
			
			if (path.get(i).getReleasedateAsDate().compareTo(installedUpdate.getReleasedateAsDate()) <= 0) { //Update at i is older than installedUpdate
				path.remove(i);
			} else break;
		}
		return path;
	}

	public void typeChanged(String selectedType) {
		if (selectedType == null) return;
		this.selectedType = selectedType;
		controller.removeTargetUpdateInput();
		controller.removeJamUpdateInput();
		controller.removeAssUpdateInput();
		controller.removeFibUpdateInput();
		controller.removeAppChckbx();
		controller.removeTargetFibButton();
		controller.removeTargetAssButton();
		controller.clearList();
		
		if (selectedType.equals("ASS")) {
			controller.addTargetAssButton();
			controller.addTargetUpdateInput(getAssUpdates());
		} else if (selectedType.equals("JAM")) {
			controller.addTargetUpdateInput(getJamUpdates());
		} else if (selectedType.equals("FIB")) {
			controller.addTargetFibButton();
			controller.addTargetUpdateInput(getFibUpdates());
		}
		selectetTargetUpdate = null;
		installedJam = null;
		installedAss = null;
		installedFib = null;
		
		System.out.println("typeChanged: " + selectedType);
	}

	public void targetUpdateChanged(int selectedIndex) {
		if (selectedIndex == -1) return;
		
		Update newUpdate;
		if (selectedType.equals("ASS")) {
			newUpdate = assUpdates.get(selectedIndex);
		} else if (selectedType.equals("JAM")) {
			newUpdate = jamUpdates.get(selectedIndex);
		} else if (selectedType.equals("FIB")) {
			newUpdate = fibUpdates.get(selectedIndex);
		} else {
			return;
		}
		
		if (selectetTargetUpdate != null && selectetTargetUpdate.equals(newUpdate)) return;
		
		controller.removeJamUpdateInput();
		controller.removeAssUpdateInput();
		controller.removeFibUpdateInput();
		controller.removeAppChckbx();
		controller.clearList();
		
		selectetTargetUpdate = newUpdate;
		
		if (selectedType.equals("ASS")) {
			selectableInsJamUpdates = getJamUpdates();
			controller.addInsJamUpdateInput(selectableInsJamUpdates);
			
			controller.addAssButton();
			selectableInsAssUpdates = getPreviousUpdates(getAssUpdates(), selectedIndex);
			controller.addInsAssUpdateInput(selectableInsAssUpdates);
		} else if (selectedType.equals("JAM")) {
			selectableInsJamUpdates = getPreviousUpdates(getJamUpdates(), selectedIndex);
			controller.addInsJamUpdateInput(selectableInsJamUpdates);
		} else if (selectedType.equals("FIB")) {
			selectableInsJamUpdates = getJamUpdates();
			controller.addInsJamUpdateInput(selectableInsJamUpdates);
			
			controller.addAssButton();
			selectableInsAssUpdates = getAssUpdates();
			controller.addInsAssUpdateInput(selectableInsAssUpdates);
			
			controller.addFibButton();
			selectableInsFibUpdates = getPreviousUpdates(getFibUpdates(), selectedIndex);
			controller.addInsFibUpdateInput(selectableInsFibUpdates);
			
			controller.addAppCheckBox();
		}
		
		installedJam = null;
		installedAss = null;
		installedFib = null;
			
		System.out.println("TargetUpdate: " + selectetTargetUpdate);
	}
	
	public void insJamChanged(int selectedIndex) {
		if (installedJam != null && installedJam.equals(selectableInsJamUpdates.get(selectedIndex))) return;
		installedJam = selectableInsJamUpdates.get(selectedIndex);
		System.out.println("InsJamUpdate: " + installedJam);
		controller.clearList();
		showPath();
	}

	public void insAssChanged(int selectedIndex) {
		if (installedAss != null && installedAss.equals(selectableInsAssUpdates.get(selectedIndex))) return;
		installedAss = selectableInsAssUpdates.get(selectedIndex);
		System.out.println("InsAssUpdate: " + installedAss);
		controller.clearList();
		showPath();
	}
	
	public void insFibChanged(int selectedIndex) {
		if (installedFib != null && installedFib.equals(selectableInsFibUpdates.get(selectedIndex))) return;
		installedFib = selectableInsFibUpdates.get(selectedIndex);
		System.out.println("InsFibUpdate: " + installedFib);
		controller.clearList();
		showPath();
	}
	
	private void showPath() {
		if (selectedType == null || selectetTargetUpdate == null) return;
		if (selectedType.equals("JAM") && installedJam == null) return;
		if (selectedType.equals("ASS") && (installedJam == null || installedAss == null)) return;
		if (selectedType.equals("FIB") && (installedJam == null || installedAss == null || installedFib == null)) return;
		System.out.println("showPath");
		
		controller.clearList();
		
		path = getPath(selectetTargetUpdate);
		
		if (selectedType.equals("JAM")) {
			path = trimPath(path, installedJam);
			
			//Remove all ASS releases that were not removed by trimPath and come before the installed JAM version.
			for (int i = 0; i < path.size(); i++) {
				if (path.get(i).getType().equals("ASS") == false) continue;
				if (path.get(i).getVersion() >= 5811) continue; // If there should ever be another ASS release after 5811, it might be enough to remove this insJAM version number check.
				if (installedJam.getVersion() > path.get(i).getVersion()) {
					path.remove(i);
					i--;
				}
			}
		}
		if (selectedType.equals("ASS")) {
			path = trimPath(path, installedJam);
			path = trimPath(path, installedAss);
		}
		if (selectedType.equals("FIB")) {
			path = trimPath(path, installedJam);
			path = trimPath(path, installedAss);
			path = trimPath(path, installedFib);
		}

		removeAssReleasesFromJamPath();
		
		moveUpdatesForXFIRMP();
		
		Collections.reverse(path);
		
		for (int i = 0; i < path.size(); i++) {
			if (path.get(i).getType().equals("ASS") && Arrays.asList(ASSreleasess).contains(path.get(i)) ) {
				controller.addReleasePanel(path.get(i));
				continue;
			} else if (path.get(i).getType().equals("FIB") && path.get(i).getVersion() % 10 == 0) {
				controller.addReleasePanel(path.get(i));
				continue;
			}
			controller.addPanel(path.get(i));
		}
		
		controller.setSplitPaneDivider();
		
		if (configFile.optionalUpdatesChckbxState) {
			optionalPath = getOptionalUpdates();
			optionalPath.removeAll(path);
			Collections.reverse(path);
			
			for (int i = 0; i < optionalPath.size(); i++) {
				System.out.println("optional: " + optionalPath.get(i));
				controller.addOptionalPanel(optionalPath.get(i));	
			}
		}
		int minPanelWidth = controller.getMinPanelSize();
		controller.setMinPanelWidth(minPanelWidth);
		int maxPanelWidth = (int)Math.round(((float) minPanelWidth / 100f) * 26 + minPanelWidth);
		controller.setMaxPanelWidth(maxPanelWidth);
		controller.fireDividerPropertyChange();
	}
	
	private void removeAssReleasesFromJamPath() {
		//Remove ASS Release 5810 from the JAM path added by getPathBetweenOldAndNew if 5810 is already included in the path
		Update searchedUpdate = new Update("ASS", 5810, "25.06.2018");
		int count = 0;
		if (installedAss != null) {
			if (installedAss.getReleasedateAsDate().compareTo(searchedUpdate.getReleasedateAsDate()) >= 0) count++;
		}
		for (int i = path.size()-1; i >= 0; i--) {
			Update u = path.get(i);
			if (u.equals(searchedUpdate)) {
				count++;
				if (count > 1) {
					path.remove(i);
					i++;
				}
			}
		}
	}
	
	private void moveUpdatesForXFIRMP() {
		//FIB 5720 should be installed immediately after ASS 5720 and before ASS 5810, as it includes a change to the XFIRMP file.
		//All FIB updates prior to 5720 should be installed before ASS 5720, or—if ASS 5720 is not present—before FIB 5720.
		Update fib5720 = new Update("FIB", 5720, "15.12.2017");
		Update ass5720 = new Update("ASS", 5720, "05.12.2017");
		Update ass5810 = new Update("ASS", 5810, "25.06.2018");
		if (selectedType.equals("FIB") && path.contains(fib5720) && (path.contains(ass5810) || path.contains(ass5720))) {
			if (path.contains(ass5720)) fib5720 = path.get(path.indexOf(fib5720));
			if (path.contains(ass5810)) ass5810 = path.get(path.indexOf(ass5810));
			
			
			//Move FIB 5720
			int targetIndex = -1;
			if (path.contains(ass5720)) {
				targetIndex = path.indexOf(ass5720) - 1;
			} else {
				targetIndex = path.indexOf(ass5810);
			}
			
			path.remove(fib5720);
			path.add(targetIndex, fib5720);
			
			
			//Move all other FIB Updates before FIB 5720
			Date cutoffDate = fib5720.getReleasedateAsDate();
			ArrayList<Update> fibsToMove = new ArrayList<Update>();
			for (Update update : path) {
	            if (update.getType().equals("FIB") && update.getReleasedateAsDate().before(cutoffDate)) { 
	                fibsToMove.add(update);
	            }
	        }
			
			path.removeAll(fibsToMove);
			
			targetIndex = -1;
			if (path.contains(ass5720)) {
				targetIndex = path.indexOf(ass5720) + 1;
			} else {
				targetIndex = path.indexOf(fib5720) + 1;
			}
			path.addAll(targetIndex, fibsToMove);
		}
	}
	
	private ArrayList<Update> getOptionalUpdates() {
		//return all updates that are not actually necessary for the target update, but are also not needed for the next FIB or ASS update.
		
		if (jamUpdates.size() <= 0) return new ArrayList<Update>();
		
		Update targetASS = selectetTargetUpdate;
		if (selectetTargetUpdate.getType().equals("FIB")) {
			if (controller.appChckbxIsSelected()) {
				targetASS = assUpdates.get(0);
			} else {
				targetASS = selectetTargetUpdate.getAssDependency();
			}
			if (targetASS == null) return new ArrayList<Update>();
		}
		if (targetASS.getType().equals("ASS") == false) return new ArrayList<Update>();

		
		ArrayList<Update> optionalUpdates = new ArrayList<Update>();
		
		Update assRelease = new Update("ASS", 5810, "25.06.2018");
		if (targetASS.getReleasedateAsDate().compareTo(assRelease.getReleasedateAsDate()) >= 0 &&
				installedAss.getReleasedateAsDate().compareTo(assRelease.getReleasedateAsDate()) <= 0) {
				//TODO werde heute bestimmt nicht fertig
			
		} else {
			// get the next ASS Update with JAM
			Update futureAss = getFutureUpdate(targetASS);
			
			while (futureAss != null) {
				if (futureAss.getJamDependency() == null) futureAss = getFutureUpdate(futureAss);
				else break;
			}
			
			Update targetJAMUpdate;
			if (futureAss == null) { 
				targetJAMUpdate = jamUpdates.get(0); //targetASS is newest ASS (newest ASS means newest JAM can be installed) (can be null because of getFutureUpdate())
			} else {
				Update futureJam = futureAss.getJamDependency();
				int index = futureJam.findIndexOfMeIn(jamUpdates);
				index++;
				if (index >= jamUpdates.size()) return new ArrayList<Update>();
				targetJAMUpdate = jamUpdates.get(index);
			}
			
			
			Update nextAssWithJam = null;
			if (targetASS.getJamDependency() != null) {
				nextAssWithJam = targetASS;
			} else {
				nextAssWithJam = getNextASSUpdateWithJAM(targetASS);
				if (nextAssWithJam == null) return new ArrayList<Update>(); // targetASS is the first ASS with JAM
			}
			optionalUpdates = getPath(targetJAMUpdate);
			
			
			Update assFromList = getNewestAssFromList(optionalUpdates);
			if (assFromList != null) {
				/*  There is an ASS update in the optional path.
					This has to be an ASS release!
					The ASS release has definitely been installed and all JAM updates beforehand make no sense because all JAM requirements must already be met for the ASS release
					So remove everything before found ASS release*/
				for (int i = optionalUpdates.size() - 1; i >= 0; i--) {			
					if (optionalUpdates.get(i).getReleasedateAsDate().compareTo(assFromList.getReleasedateAsDate()) <= 0) { //Update at i is older than installedUpdate
						optionalUpdates.remove(i);
					} else break;
				}
			} else {
				optionalUpdates = trimPath(optionalUpdates, nextAssWithJam.getJamDependency());
			}
			
			optionalUpdates = trimPath(optionalUpdates, installedJam);
		}

		Collections.reverse(optionalUpdates);
		return optionalUpdates;
	}

	private Update getNewestAssFromList(ArrayList<Update> updatesList) {
		for (int i = 0; i < updatesList.size(); i++) {
			if (updatesList.get(i).getType().equals("ASS")) {
				return updatesList.get(i);
			}
		}
		return null;
	}

	private Update getFutureUpdate(Update update) {
		
		ArrayList<Update> list;
		
		if (update.getType().equals("JAM")) list = jamUpdates;
		else if (update.getType().equals("ASS")) list = assUpdates;
		else if (update.getType().equals("FIB")) list = fibUpdates;
		else return null;
		
		int index = update.findIndexOfMeIn(list);
		if (index <= 0) return null; 
		return list.get(index-1);
	}

	private ArrayList<Update> getPreviousUpdates(ArrayList<Update> list, int fromSelectedIndex) {
		ArrayList<Update> returnList = new ArrayList<Update>();
		
		for (int i = fromSelectedIndex + 1; i < list.size(); i++) {
			returnList.add(list.get(i));
		}
		return returnList;
	}
	
	private ArrayList<Update> getPathBetweenOldAndNew(Update olderUpdate, Update newerUpdate, ArrayList<Update> list) {
		//path between olderUpdate to newerUpdate in list
		ArrayList<Update> path = new ArrayList<Update>();
		String type = list.get(0).getType();
		Update currentUpdate = newerUpdate;
		
		while (olderUpdate.getReleasedateAsDate().compareTo(currentUpdate.getReleasedateAsDate()) < 0) { //while olderUpdate is older than currentUpdate
			path.add(currentUpdate);
			if (type.equals("JAM")) {
				if (currentUpdate.getJamDependency() == null && currentUpdate.getAssDependency() != null) { //JAM/ASS Release
					currentUpdate = currentUpdate.getAssDependency();
				} else {
					currentUpdate = currentUpdate.getJamDependency();
				}
			} else if (type.equals("ASS")) {
				currentUpdate = currentUpdate.getAssDependency();
			} else if (type.equals("FIB")) {
				currentUpdate = currentUpdate.getFibDependency();
			}
		}
		path.add(currentUpdate);
		return path;
	}
	
	private Update getNextASSUpdateWithJAM(Update update) {
		//This method does not return the passed update if the passed update includes a JAM update
		//if there is no subsequent update with JAM, NULL is returned
		
		update = update.getAssDependency();
		if (update == null) return null; //There is no next update
		
		while (update.getJamDependency() == null) {
			if (update.getAssDependency() == null) return null;
			update = update.getAssDependency();
		}
		return update;
	}
	
	public ArrayList<Update> getAssUpdates() {
		return assUpdates;
	}

	public ArrayList<Update> getJamUpdates() {
		return jamUpdates;
	}

	public ArrayList<Update> getFibUpdates() {
		return fibUpdates;
	}
	
	private void handleInitUpdates() {
		try {
			initUpdates();
		} catch (UpdateNotFoundException e) {
			handleInitUpdatesException(e.getMessage() + "\n\nOder geben sie eine andere XML-Datei an.");
		} catch (FileNotFoundException e) {
			handleInitUpdatesException("Updates-XML-Datei nicht gefunden. Jetzt Pfad angeben:");
		} catch (XmlFormatException e) {
			handleInitUpdatesException(e.getMessage() + "\n\nOder geben sie eine andere XML-Datei an.");
		} catch (SelfReferencingUpdateException e) {
			handleInitUpdatesException(e.getMessage() + "\n\nOder geben sie eine andere XML-Datei an.");
		} catch (IllegalUpdateException e) {
			handleInitUpdatesException(e.getMessage() + "\n\nOder geben sie eine andere XML-Datei an.");
		} catch (DuplicateUpdateException e) {
			handleInitUpdatesException(e.getMessage() + "\n\nOder geben sie eine andere XML-Datei an.");
		}

		if (isNullOrEmpty(jamUpdates) && isNullOrEmpty(assUpdates) && isNullOrEmpty(fibUpdates)) {
			controller.showMessageDialog("Kein einziges Update in Updates-XML-Datei gefunden.\nBitte gegebenenfalls eine andere Datei unter den Einstellungen angeben.");
		}
	}
	
	private void handleInitUpdatesException(String message) {
		String newPath = Controller.showInputDialog(message, configFile.getPathToUpdates());
		exitAfterNewPathEntered(newPath);
	}
	
	private void exitAfterNewPathEntered(String newPath) {
		if (newPath != null) {
			try {
				configFile.setPathToUpdates(newPath);
			} catch (FileNotFoundException e1) {
				controller.showMessageDialog("Keine Datei an angegebenem Pfad gefunden!");
				//The program will be restarted anyway and then checked.
			}
			try {
				configFile.saveToFile();
			} catch (ConfigWriteException | ConfigDeletionException e) {
				controller.showMessageDialog(e.getMessage());
			}
			controller.showMessageDialog("Programm muss neu gestartet werden,\ndamit die neuen Einstellungen einen Effekt haben.");
		}
		System.exit(0);
	}
	
	private static boolean isNullOrEmpty(ArrayList<Update> list) {
        return list == null || list.size() == 0;
    }
	
	private void initUpdates() throws UpdateNotFoundException, FileNotFoundException, SelfReferencingUpdateException, IllegalUpdateException, DuplicateUpdateException, XmlFormatException {
		initUpdates(configFile.getPathToUpdates(), true);
	}
	
	
	private void initUpdates(String pathToUpdates, boolean handleMissingUpdatesFile) throws UpdateNotFoundException, FileNotFoundException, SelfReferencingUpdateException, IllegalUpdateException, DuplicateUpdateException, XmlFormatException {
		XmlUpdateReader xur;
		try {
			xur = new XmlUpdateReader(pathToUpdates, this, handleMissingUpdatesFile);
			xur.readUpdates();
		
			jamUpdates = xur.getJamUpdates();
			assUpdates = xur.getAssUpdates();
			fibUpdates = xur.getFibUpdates();
		} catch (SAXException e) {
			throw new XmlFormatException(pathToUpdates, e.getMessage());
		}
		
		Collections.sort(jamUpdates, Collections.reverseOrder());
		Collections.sort(assUpdates, Collections.reverseOrder());
		Collections.sort(fibUpdates, Collections.reverseOrder());
	}

	public void setAssClipboard() {
		saveStrToClipboard("DSPDTAARA DTAARA(ASSLIB)");
	}
	
	public void setFibClipboard() {
		saveStrToClipboard("DSPDTAARA DTAARA(FIBLIB)");
	}

	public void openDesciptionPressed(Update update) {
		if (controller.desciptionExists()) updateModelInputs();
		controller.setDesciption(update);
		controller.setDesciptionInputs(this.getUserInputs());
		if (configFile.maintainScrollPosChckbxState == true) {
			controller.setScrollPos(controller.getScrollPos());
		}
	}

	private void updateModelInputs() {
		if (controller.getInputs() == null) return;
		userInputs = controller.getInputs();
	}

	public void appCheckBoxChanged(boolean selected) {
		boxStates.putAll(controller.getCorrelatingCheckBoxStates()); //"putAll" overites if pressent and adds if not
		controller.clearList();
		showPath();
		try {
			controller.setBoxStates(boxStates, false);
		} catch (PassedStateNotFoundException e) {
			//PassedStateNotFoundException can be ignored
		}
	}

	private String getDataToSave() {
		String data = "";
		
		if (selectedType != null) {
			if (selectedType.equals("*Anwendung")) {
				selectedType = null;
			} else {
				data += "<selectedType>" + selectedType + "</selectedType>" + "\n";
			}
		}
		
		if (selectetTargetUpdate != null) {data += "<selectetTargetUpdate>" + selectetTargetUpdate.toString() + "</selectetTargetUpdate>" + "\n";}
		if (installedJam != null) {data += "<installedJam>" + installedJam.toString() + "</installedJam>" + "\n";}
		if (installedAss != null) {data += "<installedAss>" + installedAss.toString() + "</installedAss>" + "\n";}
		if (installedFib != null) {data += "<installedFib>" + installedFib.toString() + "</installedFib>" + "\n";}
		
		data += "<appChckbx>" + controller.appChckbxIsSelected() + "</appChckbx>" + "\n";
		updateModelInputs();
		for (int i = 0; i < inputs.length; i++) {
			data += "<" + inputs[i] + ">" + userInputs.get(inputs[i]) + "</" + inputs[i] + ">" + "\n";
		}
		
		data += "<correlatingCheckBoxStates>\n";
		boxStates = new HashMap<>(); //clear before saving in case appCheckBox is false.
		boxStates.putAll(controller.getCorrelatingCheckBoxStates()); //"putAll" overites if pressent and adds if not
		for (Map.Entry<Update, ArrayList<Boolean>> entry : boxStates.entrySet()) {
			Update update = entry.getKey();
            ArrayList<Boolean> states = entry.getValue();
			//data += "\t<update>\n" + "\t\t<version>" + update + "</version>\n" + "\t\t<states>" + states.toString() + "</states>\n" + "\t</update>\n";
			data += "<update>" + "<version>" + update + "</version>" + "<states>" + states.toString() + "</states>" + "</update>\n";
		}
		
		data += "</correlatingCheckBoxStates>" + "\n";
		
		return data;
	}
	
	public int saveDataToFile() {
		//saving Data as File
		String data = getDataToSave();
		String fileName = generateUniqueFileName("Update_Data");
		return this.saveStrAsFile(data, fileName);
	}
	
	public void saveDataToClipboard() {
		//saving Data to clipboard
		String data = getDataToSave();
		saveStrToClipboard(data);
	}
	
	private boolean cancelBecauseOfUnsavedData() {
		if (startingData == null || getDataToSave() == null) return false;
		if (startingData.equals(getDataToSave()) == false) {
			int selectedValue = controller.showOptionDialog("Ihre Änderungen speichern?\n\nEs wurden ungespeicherte Änderungen gefunden!", new Object[] {"Speichern", "Nicht speichern", "Abbruch"});
		
			//System.out.println(selectedValue);
			if (selectedValue == 0) { //save
				if (saveDataToFile() == -1) { //cancel at saveDataToFile
					return true;
				}
			} else if (selectedValue == 1) { //dont save
				return false;
			} else { 
				return true; //cancel
			}
		}
		return false;
	}
	
	public void loadData(String data) {
		if (cancelBecauseOfUnsavedData()) return;
		forceLoadData(data);
	}
	
	public void forceLoadData(String data) {
		if (data == null) return;
		
		String typ;	
		
		typ = stringBetween(data, "selectedType");
		
		//type not valid
		if (!typ.equals("JAM") && !typ.equals("ASS") && !typ.equals("FIB")) {
			controller.showMessageDialog("Keine gültigen Daten gefunden!");
			return;
		}
		
		controller.setTypeComboBox(typ);

		userInputs = new HashMap<>();
		for (int i = 0; i < inputs.length; i++) {
			userInputs.put(inputs[i], stringBetween(data, inputs[i]));
		}
		
		controller.setTextfelds(this.getUserInputs());
		
		ArrayList<Update> updates = null;
		if (typ.equals("JAM")) {
			updates = getJamUpdates();
		} else if (typ.equals("ASS")) {
			updates = getAssUpdates();
		} else if (typ.equals("FIB")) {
			updates = getFibUpdates();
		}
		
		int index = -1;
		
		String targetUpdate = stringBetween(data, "selectetTargetUpdate");
		index = indexOfUpdateWithString(targetUpdate, updates);
		if (index == -1) return;
		controller.setTargetUpdateComboBox(index);
		
		
		updates = selectableInsJamUpdates;
		String installedJam = stringBetween(data, "installedJam");
		index = indexOfUpdateWithString(installedJam, updates);
		if (index == -1) return;
		controller.setInsJamUpdateComboBox(index);
		
		if (typ.equals("ASS") || typ.equals("FIB")) {
			updates = selectableInsAssUpdates;
			String installedAss = stringBetween(data, "installedAss");
			index = indexOfUpdateWithString(installedAss, updates);
			if (index == -1) return;
			controller.setInsAssUpdateComboBox(index);
			
			if (typ.equals("FIB")) {
				
				String appChckbx = stringBetween(data, "appChckbx");
				if (!appChckbx.equals("true")) appChckbx = "false";
				
				if (appChckbx.equals("true")) {
					controller.setAppChckbx(true);
					this.appCheckBoxChanged(true);
				} else {
					controller.setAppChckbx(false);
					this.appCheckBoxChanged(false);
				};
				
				updates = selectableInsFibUpdates;
				String installedFib = stringBetween(data, "installedFib");
				index = indexOfUpdateWithString(installedFib, updates);
				if (index == -1) return;
				controller.setInsFibUpdateComboBox(index);
			}
		}
		
		Map<Update, ArrayList<Boolean>> correlatingStates = getCorrelatingBoxStates(stringBetween(data, "correlatingCheckBoxStates"));
		try {
			controller.setBoxStates(correlatingStates);
		} catch (PassedStateNotFoundException e) {
			controller.showMessageDialog(e.getMessage());
		}
		
		startingData = getDataToSave();
	}
	
	private Map<Update, ArrayList<Boolean>> getCorrelatingBoxStates(String stringBetween) {
		Map<Update, ArrayList<Boolean>> correlatingStates = new HashMap<>();
		
		if (stringBetween.indexOf("<update>") == -1) return correlatingStates;
		stringBetween = stringBetween.substring(stringBetween.indexOf("<update>"));

		String[] updatesArr = stringBetween.split("<update>");
		ArrayList<String> updates = new ArrayList<String>(Arrays.asList(updatesArr));
		
		
		if (updates.size() <= 1) return correlatingStates;
		updates.remove(0);
		
		for (int i = 0; i < updates.size(); i++) {
			
			String[] components = stringBetween(updates.get(i), "version").split(" ");
			if (components.length < 3) return correlatingStates;
			String type =  components[0];
			String version = components[1];
			String releasedate = components[2];
			releasedate = releasedate.replace("(", "");
			releasedate = releasedate.replace(")", "");
			
			
			Update update = new Update(type, Integer.parseInt(version), releasedate);
			
			String boxStatesStr = stringBetween(updates.get(i), "states");
			boxStatesStr = boxStatesStr.toLowerCase();
			boxStatesStr = boxStatesStr.replaceAll(" ", "");
			boxStatesStr = boxStatesStr.replace("[", "");
			boxStatesStr = boxStatesStr.replace("]", "");
			
			String[] boxStatesArr = boxStatesStr.split(",");
			ArrayList<Boolean> boxStates = new ArrayList<Boolean>();
			
			for (String b : boxStatesArr) {
				if (b.equals("true")) {
					boxStates.add(true);
				} else {
					boxStates.add(false);
				}
			}
			
			correlatingStates.put(update, boxStates);
		}
		
		return correlatingStates;
	}

	public void loadDataFromClipboard() {
		//load Data from clipboard
		loadData(loadStrFromClipboard());
	}
	
	private String stringBetween(String data, String brackets) {
			try {
				return data.substring(data.indexOf("<" + brackets + ">") + 2 + brackets.length(), data.indexOf("</" + brackets + ">"));
			} catch (IndexOutOfBoundsException e) {
				return "";
			}
	}
	
	private int indexOfUpdateWithString(String updateStr, ArrayList<Update> updates) {
		if (updateStr.length() <= 0) return -1;
		if (!updateStr.substring(0, 3).equals("JAM") && !updateStr.substring(0, 3).equals("ASS") && !updateStr.substring(0, 3).equals("FIB")) return -1;
		
		for (int i = 0; i < updates.size(); i++) {
			if (updates.get(i).toString().equals(updateStr)) {
				return i;
			}
		}
		return -1;
	}
	
	public void exportListToClipboard() {
		saveStrToClipboard(getExportList());
	}

	public int exportListToFile() {
		String fileName = generateUniqueFileName("Update_Liste");
		return this.saveStrAsFile(getExportList(), fileName);
	}

	public String getExportList() {
		String exportStr = "";
		if (path != null) {
			for (int i = path.size() -1; i >= 0; i--) {
				exportStr += path.get(i) + "\n";
			}
		}
		
		if (optionalPath != null) {
			for (int i = optionalPath.size() -1; i >= 0; i--) {
				exportStr += optionalPath.get(i) + " (Optional)\n";
			}
		}
		return exportStr;
	}

	public void saveConfigFile(Config configFile) throws ConfigWriteException, ConfigDeletionException {
			configFile.saveToFile();
	}
	
	private boolean handlePathToUpdates(OptionPanel optionPanel) {
		//Return false if ERROR
		if (configFile.getPathToUpdates().equals(optionPanel.getTextFieldPathToUpdates())) return true;
		try {
			initUpdates(optionPanel.getTextFieldPathToUpdates(), false);
		} catch (FileNotFoundException e) {
			controller.showMessageDialog("Keine Datei an angegebenem Pfad gefunden!");
			return false;
		} catch (XmlFormatException e) {
			controller.showMessageDialog(e.getMessage() + "\nOder geben sie eine andere XML-Datei an.");
			return false;
		} catch (UpdateNotFoundException e) {
			controller.showMessageDialog(e.getMessage() + "\nOder geben sie eine andere XML-Datei an.\n\nAlternativ kann auch das Update welches " + e.update + " verwendet entfernt werden und anschließend, falls bei VEDA vorhanden, durch die automatisierte Suche wieder hinzugefügt werden.");
			return false;
		} catch (SelfReferencingUpdateException e) {
			controller.showMessageDialog(e.getMessage() + "\nOder geben sie eine andere XML-Datei an.");
			return false;
		} catch (IllegalUpdateException e) {
			controller.showMessageDialog(e.getMessage() + "\n\nOder geben sie eine andere XML-Datei an.");
			return false;
		} catch (DuplicateUpdateException e) {
			controller.showMessageDialog(e.getMessage() + "\n\nOder geben sie eine andere XML-Datei an.");
			return false;
		}

		if (isNullOrEmpty(jamUpdates) && isNullOrEmpty(assUpdates) && isNullOrEmpty(fibUpdates)) {
			controller.showMessageDialog("Kein einziges Update in Updates-XML-Datei gefunden.\nBitte eine andere Datei angeben.");
			return false;
		}
		
		try {
			configFile.setPathToUpdates(optionPanel.getTextFieldPathToUpdates());
		} catch (FileNotFoundException e) {
			controller.showMessageDialog("Keine Datei an angegebenem Pfad gefunden!"); //Should never happen because it is already handled
		}
		controller.setTypeComboBox(null);
		controller.removeDescriptionPanel();
		return true;
	}

	public boolean saveOptions(OptionPanel optionPanel) {
		Boolean FLAG = false; // If true, an error occurred, and the user is not allowed to exit the OptionPanel (values are reset to those from the config file)
		Boolean needToRestart = false;
		
		if (handlePathToUpdates(optionPanel) == false) {
			optionPanel.setTextFieldPathToUpdates(configFile.getPathToUpdates());
			FLAG = true;
		}	
		
		try {
			String textFieldText = optionPanel.textFieldBackgroundColor.getText();
			try {
				Color backgroundColor = Color.decode(textFieldText);
				System.out.println(configFile.getBackgroundColor());
				System.out.println(backgroundColor);
				if (configFile.getBackgroundColor().equals(backgroundColor) == false) {
					configFile.setBackgroundColor(backgroundColor);
					needToRestart = true;
				}
			} catch (Exception e) {
				throw new IllegalColorException("Hintergrundfarbe", textFieldText);
			}
		} catch (IllegalColorException e1) {
			controller.showMessageDialog(e1.getMessage());
			optionPanel.textFieldBackgroundColor.setText(configFile.getBackgroundColorCode());
			optionPanel.backgroundColorPreview.setBackground(configFile.getBackgroundColor());
			FLAG = true;
		}
		try {
			String textFieldText = optionPanel.textFieldSecondaryBackgroundColor.getText();
			try {
				Color secondaryBackgroundColor = Color.decode(textFieldText);
				System.out.println(configFile.getSecondaryBackgroundColor());
				System.out.println(secondaryBackgroundColor);
				if (configFile.getSecondaryBackgroundColor().equals(secondaryBackgroundColor) == false) {
					configFile.setSecondaryBackgroundColor(secondaryBackgroundColor);
					needToRestart = true;
				}
			} catch (Exception e) {
				throw new IllegalColorException("Sekundäre Hintergrundfarbe", textFieldText);
			}
		} catch (IllegalColorException e1) {
			controller.showMessageDialog(e1.getMessage());
			optionPanel.textFieldSecondaryBackgroundColor.setText(configFile.getSecondaryBackgroundColorCode());
			optionPanel.secondaryBackgroundColorPreview.setBackground(configFile.getSecondaryBackgroundColor());
			FLAG = true;
		}
		try {
			String textFieldText = optionPanel.textFieldForegroundColor.getText();
			try {
				Color foregroundColor = Color.decode(textFieldText);
				if (configFile.getForegroundColor().equals(foregroundColor) == false) {
					configFile.setForegroundColor(foregroundColor);
					needToRestart = true;
				}
			} catch (Exception e) {
				throw new IllegalColorException("Schriftfarbe", textFieldText);
			}
		} catch (IllegalColorException e1) {
			controller.showMessageDialog(e1.getMessage());
			optionPanel.textFieldForegroundColor.setText(configFile.getForegroundColorCode());
			optionPanel.foregroundColorPreview.setBackground(configFile.getForegroundColor());
			FLAG = true;
		}
		
		try {
			String textFieldText = optionPanel.textFieldSecondaryForegroundColor.getText();
			try {
				Color secondaryForegroundColor = Color.decode(textFieldText);
				System.out.println(configFile.getSecondaryForegroundColor());
				System.out.println(secondaryForegroundColor);
				if (configFile.getSecondaryForegroundColor().equals(secondaryForegroundColor) == false) {
					configFile.setSecondaryForegroundColor(secondaryForegroundColor);
					needToRestart = true;
				}
			} catch (Exception e) {
				throw new IllegalColorException("Sekundäre Schriftfarbe", textFieldText);
			}
		} catch (IllegalColorException e1) {
			controller.showMessageDialog(e1.getMessage());
			optionPanel.textFieldSecondaryForegroundColor.setText(configFile.getSecondaryForegroundColorCode());
			optionPanel.secondaryForegroundColorPreview.setBackground(configFile.getSecondaryForegroundColor());
			FLAG = true;
		}
		
		if (FLAG == true) return false;
		
		if (Float.compare(configFile.fontSize, (Float) optionPanel.spinnerFontSize.getValue()) != 0) {
			configFile.fontSize = (Float) optionPanel.spinnerFontSize.getValue();
			needToRestart = true;
		}
		
		if (configFile.pathToFibUpdateFiles.equals(optionPanel.getTextFieldPathToFibUpdateFiles()) == false) {
			configFile.pathToFibUpdateFiles = optionPanel.getTextFieldPathToFibUpdateFiles();
			needToRestart = true;
		}
		
		if (configFile.pathToFibReleaseFiles.equals(optionPanel.getTextFieldPathToFibReleaseFiles()) == false) {
			configFile.pathToFibReleaseFiles = optionPanel.getTextFieldPathToFibReleaseFiles();
			needToRestart = true;
		}
		
		if (configFile.pathToAssFiles.equals(optionPanel.getTextFieldPathToAssFiles()) == false) {
			configFile.pathToAssFiles = optionPanel.getTextFieldPathToAssFiles();
			needToRestart = true;
		}
		
		if (configFile.pathToJamFiles.equals(optionPanel.getTextFieldPathToJamFiles()) == false) {
			configFile.pathToJamFiles = optionPanel.getTextFieldPathToJamFiles();
			needToRestart = true;
		}
		
		if (configFile.updatesChckbxState != optionPanel.chckbxUpdatesCheck.isSelected()) {
			configFile.updatesChckbxState = optionPanel.chckbxUpdatesCheck.isSelected();
			needToRestart = true;
		}

		configFile.optionalUpdatesChckbxState = optionPanel.chckbxOptionalUpdates.isSelected();
		
		configFile.maintainScrollPosChckbxState = optionPanel.chckbxMaintainScrollPos.isSelected();
		
		try {
			this.saveConfigFile(configFile);
			if (needToRestart) {
				controller.showMessageDialog("Programm muss neu gestartet werden,\ndamit die neuen Einstellungen einen Effekt haben.");
			}
			return true;
		} catch (ConfigWriteException | ConfigDeletionException e) {
			controller.showMessageDialog(e.getMessage());
		}
		return false;
	}
	
	private int saveStrAsFile(String contentToSave, String fileName) {
		JFileChooser readyChooser = getReadyFileChooser(fileName);
		boolean continueWithSave = true;
		File fileToSave;
		do {
			if (readyChooser.showSaveDialog(controller.getParentForPositionOnly()) != JFileChooser.APPROVE_OPTION) return -1;
			fileToSave = readyChooser.getSelectedFile();
			if (fileToSave.exists()) {
	            int result = JOptionPane.showConfirmDialog(controller.getParentForPositionOnly(),
	                    "Die Datei existiert bereits. Möchten Sie sie überschreiben?", "Bestätigung",
	                    JOptionPane.YES_NO_OPTION);
	            if (result == JOptionPane.YES_OPTION) {
	            	continueWithSave = true;
	            } else {
	            	continueWithSave = false;
	            	continue;
	            }
	        }
		} while (continueWithSave == false);
		
		saveValueToPreferences(LAST_USED_FOLDER_KEY, fileToSave.getParent());
		
		if (!fileToSave.getAbsolutePath().endsWith(".txt")) {
            fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
        }
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
            writer.write(contentToSave);
            writer.flush();
            writer.close();
        } catch (IOException e) {
        	controller.showMessageDialog("Zugriff verweigert auf angegebenen Pfad");
        }
		startingData = getDataToSave();
		return 1;
	}
	
	private String getContentFromFile(File fileToRead) {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(fileToRead))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {e.printStackTrace();}
		return content.toString();
	}
	
	public String loadStrFromFile() {
		JFileChooser readyChooser = getReadyFileChooser();
		if (readyChooser.showOpenDialog(controller.getParentForPositionOnly()) != JFileChooser.APPROVE_OPTION) return null;
		File fileToRead = readyChooser.getSelectedFile();
		saveValueToPreferences(LAST_USED_FOLDER_KEY, fileToRead.getParent());
		
		return getContentFromFile(fileToRead);
	}
	
	private JFileChooser getReadyFileChooser() {
		return getReadyFileChooser("");
	}
	
	private JFileChooser getReadyFileChooser(String suggestedFileName) {
		JFileChooser chooser = new JFileChooser();
		String lastUsedFolderPath = getPreferencesValue(LAST_USED_FOLDER_KEY);
		if (lastUsedFolderPath != null && !lastUsedFolderPath.isEmpty()) {
			chooser.setCurrentDirectory(new File(lastUsedFolderPath));
        }
		chooser.setFileFilter(new FileNameExtensionFilter("(*.txt)", "txt"));
		if (suggestedFileName.length() > 0) {
			chooser.setSelectedFile(new File(suggestedFileName + ".txt"));
		}
		return chooser;
	}
	
	private void saveStrToClipboard(String contentToSave) {
		StringSelection stringSelection = new StringSelection(contentToSave);
		clipboard.setContents(stringSelection, null);
		startingData = getDataToSave();
	}
	
	private String loadStrFromClipboard() {
		String content = "";
		try {
			Transferable transferable = clipboard.getContents(null);
			if (transferable == null) return "";
			
			if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				System.out.println("Loaded From Clipboard");
				content = (String) transferable.getTransferData(DataFlavor.stringFlavor);
				
			} else if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                List<?> fileList = (List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                for (Object fileObj : fileList) {
                    if (fileObj instanceof File) {
                        File file = (File) fileObj;
                        
                        if (file.getName().endsWith(".txt")) {  // Prüfe, ob es eine TXT-Datei ist
                            System.out.println("Loaded From File in Clipboard: " + file.getAbsolutePath());
                            content = getContentFromFile(file);
                        }
                    }
                }
            }
		} catch (UnsupportedFlavorException | IOException e) {System.out.println("Exception at Load " + e); return "";}
		return content;
	}

	public void loadDataFromFile() {
		String data = loadStrFromFile();
		if (data == null) return; // JFileChooserOPTION != APPROVE_OPTION
		loadData(data);
	}
	
	private static String generateUniqueFileName(String baseName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        return baseName + "_" + timestamp;
    }
	
	private static String getPreferencesValue(String KEY) {
        return Preferences.userNodeForPackage(Model.class).get(KEY, null);
    }

    private static void saveValueToPreferences(String KEY, String value) {
        Preferences.userNodeForPackage(Model.class).put(KEY, value);
        /*String userPreferencesPath = System.getProperty("user.home") + "/.java/.userPrefs";
        System.out.println("Benutzerpräferenzen-Pfad: " + userPreferencesPath);
        String javaPrefsPath = System.getProperty("java.util.prefs.userRoot");
        System.out.println("Java-Präferenzen-Pfad: " + javaPrefsPath);*/
    }
    
    public Map<String, String> getUserInputs() {
    	return this.userInputs;
    }

	public Color getBackgroundColor() {
		return this.configFile.getBackgroundColor();
	}
	
	public Color getSecondaryBackgroundColor() {
		return this.configFile.getSecondaryBackgroundColor();
	}

	public Color getForegroundColor() {
		return this.configFile.getForegroundColor();
	}

	public Float getFontSize() {
		return this.configFile.fontSize;
	}
	
	public Color getSecondaryForegroundColor() {
		return this.configFile.getSecondaryForegroundColor();
	}

	public DescriptionPanel getNewDescriptionPanel(Update update) {
		DescriptionPanel newDescriptionPanel = null;
		if (update.getType().equals("ASS")) {
			if (Arrays.asList(ASSreleasess).contains(update)) {
				newDescriptionPanel = new ASSReleaseDescriptionPanel(update, controller.getFont(), configFile);
			} else {
				newDescriptionPanel = new ASSdescriptionPanel(update, controller.getFont(), configFile);
			}
		} else if (update.getType().equals("JAM")) {
			newDescriptionPanel = new JAMdescriptionPanel(update, controller.getFont(), configFile);
		} else if (update.getType().equals("FIB")) {
			if (update.getVersion() % 10 == 0) {
				newDescriptionPanel = new FIBReleaseDescriptionPanel(update, controller.getFont(), configFile);
			} else {
				newDescriptionPanel = new FIBdescriptionPanel(update, controller.getFont(), configFile);
			}
		}
		return newDescriptionPanel;
	}

	public OptionPanel getOptionPanel() {
		OptionPanel optionPanel = new OptionPanel(controller.getFont(), configFile.getBackgroundColor(), configFile.getForegroundColor(), version);
		optionPanel.setTextFieldPathToUpdates(configFile.getPathToUpdates());
		optionPanel.textFieldBackgroundColor.setText(configFile.getBackgroundColorCode());
		optionPanel.textFieldSecondaryBackgroundColor.setText(configFile.getSecondaryBackgroundColorCode());
		optionPanel.textFieldForegroundColor.setText(configFile.getForegroundColorCode());
		optionPanel.textFieldSecondaryForegroundColor.setText(configFile.getSecondaryForegroundColorCode());
		optionPanel.spinnerFontSize.setValue(new Float(configFile.fontSize));
		optionPanel.chckbxUpdatesCheck.setSelected(configFile.updatesChckbxState);
		optionPanel.chckbxOptionalUpdates.setSelected(configFile.optionalUpdatesChckbxState);
		optionPanel.chckbxMaintainScrollPos.setSelected(configFile.maintainScrollPosChckbxState);
		
		optionPanel.setTextFieldPathToFibUpdateFiles(configFile.pathToFibUpdateFiles);
		optionPanel.setTextFieldPathToFibReleaseFiles(configFile.pathToFibReleaseFiles);
		optionPanel.setTextFieldPathToAssFiles(configFile.pathToAssFiles);
		optionPanel.setTextFieldPathToJamFiles(configFile.pathToJamFiles);
		
		optionPanel.backgroundColorPreview.setBackground(Color.decode(optionPanel.textFieldBackgroundColor.getText()));
		optionPanel.secondaryBackgroundColorPreview.setBackground(Color.decode(optionPanel.textFieldSecondaryBackgroundColor.getText()));
		optionPanel.foregroundColorPreview.setBackground(Color.decode(optionPanel.textFieldForegroundColor.getText()));
		optionPanel.secondaryForegroundColorPreview.setBackground(Color.decode(optionPanel.textFieldSecondaryForegroundColor.getText()));
		return optionPanel;
	}

	public void optionCancelPressed(OptionPanel optionPanel) {
		controller.removeOptionPanel(optionPanel);
	}
	
	public String getPathToUpdate() {
		return getChooserFilePath(configFile.getPathToUpdates());
	}
	
	public String getPathToFibUpdate() {
		return getChooserDirectoryPath(configFile.pathToFibUpdateFiles);
	}
	
	public String getPathToFibRelease() {
		return getChooserDirectoryPath(configFile.pathToFibReleaseFiles);
	}
	
	public String getPathToAss() {
		return getChooserDirectoryPath(configFile.pathToAssFiles);
	}
	
	public String getPathToJam() {
		return getChooserDirectoryPath(configFile.pathToJamFiles);
	}
	
	private String getChooserFilePath(String currentDirectory) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(currentDirectory));
		chooser.setFileFilter(new FileNameExtensionFilter("(*.xml)", "xml"));
		if (chooser.showOpenDialog(controller.getParentForPositionOnly()) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getPath();
		};
		return null;
	}
	
	private String getChooserDirectoryPath(String currentDirectory) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(currentDirectory));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(controller.getParentForPositionOnly()) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getPath();
		};
		return null;
	}

	public void optionSavePressed(OptionPanel optionPanel) {
		if (saveOptions(optionPanel)) {
			controller.removeOptionPanel(optionPanel);
			showPath();
		}
	}

	public void programClosing() {
		if (cancelBecauseOfUnsavedData()) return;
		System.exit(0);
	}
	
	public void dropEvent(Transferable transferable) {
		if (!transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) return;
		File file = null;
		try {
			@SuppressWarnings("unchecked")
			java.util.List<File> droppedFiles = (java.util.List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
			if (droppedFiles.isEmpty()) return;
			file = droppedFiles.get(0);
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
		if (file == null) return;
		if (isTxt(file) == false) {
			controller.showMessageDialog("Dateityp nicht txt!");
			return;
		}
		
		loadData(getContentFromFile(file));
	}
	
	private boolean isTxt(File file) {
		String extension = "";
		int i = file.getName().lastIndexOf('.');
		if (i > 0) {
		    extension = file.getName().substring(i+1);
		}
		if (extension.equals("txt")) return true;
		return false;
	}

	public void newList() {
		if (cancelBecauseOfUnsavedData()) return;
		userInputs = new HashMap<>();
		for (int i = 0; i < inputs.length; i++) {
			if (inputs[i].equals("optName")) {
				userInputs.put(inputs[i], "OPTVEDA");
			} else if (inputs[i].equals("imgClgName")) {
				userInputs.put(inputs[i], "VEDAIMAGES");
			} else {
				userInputs.put(inputs[i], "");
			}
		}
		
		controller.setDesciptionInputs(userInputs);
		controller.setTypeComboBox(null); //This calls typeChanged() and clears many fields. Be sure to call it before getDataToSave()!
		startingData = getDataToSave();
		controller.setSplitPaneDivider(1.0d);
	}
	
	public void setGraficCheckBx(ArrayList<Update> selectedUpdates, boolean newState) {
		Map<Update, ArrayList<Boolean>> newCorrelatingStates = controller.getCorrelatingCheckBoxStates();
		for (Map.Entry<Update, ArrayList<Boolean>> entry : newCorrelatingStates.entrySet()) {
			Update update = entry.getKey();
            ArrayList<Boolean> states = entry.getValue();
            
            if (selectedUpdates.contains(update) == false) continue;
            
            if (update.getType().equals("JAM")) {
            	states.set(0, newState);
            } else {
            	states.set(1, newState);
            }
            entry.setValue(states);
		}
		
		try {
			controller.setBoxStates(newCorrelatingStates, false);
		} catch (PassedStateNotFoundException e) {
			controller.showMessageDialog(e.getMessage());
		}
	}
	
	public void setClassicCheckBx(ArrayList<Update> selectedUpdates, boolean newState) {
		Map<Update, ArrayList<Boolean>> newCorrelatingStates = controller.getCorrelatingCheckBoxStates();
		for (Map.Entry<Update, ArrayList<Boolean>> entry : newCorrelatingStates.entrySet()) {
			Update update = entry.getKey();
            ArrayList<Boolean> states = entry.getValue();
            
            if (selectedUpdates.contains(update) == false) continue;
            if (update.getType().equals("JAM")) continue;
            
            states.set(0, newState);
            entry.setValue(states);
		}
		
		try {
			controller.setBoxStates(newCorrelatingStates, false);
		} catch (PassedStateNotFoundException e) {
			controller.showMessageDialog(e.getMessage());
		}
	}

	public void chooseBackgroundColor(OptionPanel optionPanel) {
		chooseColor(configFile.getBackgroundColor(), optionPanel.textFieldBackgroundColor);
		optionPanel.backgroundColorPreview.setBackground(Color.decode(optionPanel.textFieldBackgroundColor.getText()));
	}

	public void chooseSecondaryBackgroundColor(OptionPanel optionPanel) {
		chooseColor(configFile.getSecondaryBackgroundColor(), optionPanel.textFieldSecondaryBackgroundColor);
		optionPanel.secondaryBackgroundColorPreview.setBackground(Color.decode(optionPanel.textFieldSecondaryBackgroundColor.getText()));
	}

	public void chooseForegroundColor(OptionPanel optionPanel) {
		chooseColor(configFile.getForegroundColor(), optionPanel.textFieldForegroundColor);
		optionPanel.foregroundColorPreview.setBackground(Color.decode(optionPanel.textFieldForegroundColor.getText()));
	}

	public void chooseSecondaryForegroundColor(OptionPanel optionPanel) {
		chooseColor(configFile.getSecondaryForegroundColor(), optionPanel.textFieldSecondaryForegroundColor);
		optionPanel.secondaryForegroundColorPreview.setBackground(Color.decode(optionPanel.textFieldSecondaryForegroundColor.getText()));
	}
	
	private void chooseColor(Color currentColor, JTextField textField) {
		Color currentColor_tmp = null;
		try {
			currentColor_tmp = Color.decode(textField.getText());
		} catch (Exception e) {
			currentColor_tmp = currentColor;
		}

		Color newColor = controller.openColorChooser(currentColor_tmp);
		if (newColor == null) newColor = currentColor_tmp;
		textField.setText(Config.colorToColorCode(newColor));
	}
	
	private JFileChooser createMissingUpdatesChooser(int selectedValue, File missingFile) {
		JFileChooser chooser = new JFileChooser();
		
		if (selectedValue == 0) { //create
			chooser.setDialogTitle("Ziel für neue UpdateData.xml auswählen");
		} else if (selectedValue == 1) {
			chooser.setDialogTitle("Vorhandene UpdateData.xml auswählen");
		}
        
        if (missingFile.getParentFile() != null && missingFile.getParentFile().exists()) {
            chooser.setCurrentDirectory(missingFile.getParentFile());
        }
        
        return chooser;
	}

	public File handleMissingUpdatesFile(File missingFile) {
		Object[] options = new Object[] {"Erstellen", "Laden", "Abbruch"};
		File newFile = null;
		int result = -1;
		
		while (result != JFileChooser.APPROVE_OPTION) {
			int selectedValue = JOptionPane.showOptionDialog(controller.getParentForPositionOnly(), "Das Programm hat keine UpdateData.xml Datei bei folgedem Pfad gefunden:\n" + missingFile.getAbsolutePath() + "\n\nDatei erstellen oder eine bestehende laden?",
					"Option auswählen",JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, options, options[0]);
			
			if (selectedValue != 0 && selectedValue != 1) System.exit(0); //exit
			
			JFileChooser chooser = createMissingUpdatesChooser(selectedValue, missingFile);
			if (selectedValue == 0) chooser.setSelectedFile(new File("UpdatesData.xml")); //create
			
			if (selectedValue == 0) { //create
				result = chooser.showSaveDialog(controller.getParentForPositionOnly());
				File selected = chooser.getSelectedFile();
				if (result != JFileChooser.APPROVE_OPTION) continue;
				if (!selected.getName().toLowerCase().endsWith(".xml")) {
					selected = new File(selected.getParentFile(), selected.getName() + ".xml");
				}
	            try {
	            	newFile = selected;
	                if (newFile.createNewFile()) {
	                	handleFillUpdatesDataFile(newFile);
	                } else {
	                	JOptionPane.showMessageDialog(controller.getParentForPositionOnly(), "Datei mit diesem Namen existiert bereits im selben Ordner.");
	                	result = -1;
	                	continue;
	                }
	            } catch (IOException e) { 
	                JOptionPane.showMessageDialog(controller.getParentForPositionOnly(), "Fehler beim Erstellen der Datei:\n" + e.getMessage());
	                System.exit(0);
	            }
			} else { //load
				while (true) {
					chooser = createMissingUpdatesChooser(selectedValue, missingFile);
					result = chooser.showOpenDialog(controller.getParentForPositionOnly());
					if (result != JFileChooser.APPROVE_OPTION) break;
					
					File selected = chooser.getSelectedFile();
					if (selected.exists() && selected.getName().toLowerCase().endsWith(".xml")) {
			            newFile = selected;
			            break;
			        }
					JOptionPane.showMessageDialog(controller.getParentForPositionOnly(), "Ungültige Datei gewählt.");
				}
			}
		}
		
		setPathToUpdates(newFile.getAbsolutePath());
		return newFile;
	}
	
	private void handleFillUpdatesDataFile(File newFile) {
		try {
    		fillUpdatesDataFile(newFile);
    		JOptionPane.showMessageDialog(controller.getParentForPositionOnly(), "UpdateData Datei erfolgreich erstellt.\n\n" + newFile.getAbsolutePath());
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(controller.getParentForPositionOnly(), "FileNotFoundException\n" + e.getMessage() + "\n\nDatei wird wieder gelöscht:\n" + newFile.getAbsolutePath());
			if (newFile.exists() && !newFile.delete()) {
				JOptionPane.showMessageDialog(controller.getParentForPositionOnly(), "Unvollständige Datei konnte nicht gelöscht werden.\n\n" + newFile.getAbsolutePath());
			}
		} catch (ResourceFileReadException e) {
			JOptionPane.showMessageDialog(controller.getParentForPositionOnly(), e.getMessage() + "\n\nDatei wird wieder gelöscht:\n" + newFile.getAbsolutePath());
			if (newFile.exists() && !newFile.delete()) {
				JOptionPane.showMessageDialog(controller.getParentForPositionOnly(), "Unvollständige Datei konnte nicht gelöscht werden.\n\n" + newFile.getAbsolutePath());
			}
		} catch (FileWriteException e) {
			JOptionPane.showMessageDialog(controller.getParentForPositionOnly(), e.getMessage() + "\n\nDatei wird wieder gelöscht:\n" + newFile.getAbsolutePath());
			if (newFile.exists() && !newFile.delete()) {
				JOptionPane.showMessageDialog(controller.getParentForPositionOnly(), "Unvollständige Datei konnte nicht gelöscht werden.\n\n" + newFile.getAbsolutePath());
			}
		}
	}

	private void fillUpdatesDataFile(File newFile) throws FileNotFoundException, ResourceFileReadException, FileWriteException {
		String content = "";
		content = ResourceLoader.loadResource("UpdatesData_standard.xml");
		try {
			FileWriter writer = new FileWriter(newFile);
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new FileWriteException(newFile.getAbsolutePath());
		}
	}

	private void setPathToUpdates(String newPath) {
		try {
			configFile.setPathToUpdates(newPath);
			try {
				configFile.saveToFile();
			}  catch (ConfigWriteException | ConfigDeletionException e) {
				controller.showMessageDialog(e.getMessage());
				System.exit(0);
			}
		} catch (FileNotFoundException e) {
			controller.showMessageDialog(e.getMessage()); //Should only happen if an error occurs during the automated creation of a new file and the file is then automatically deleted again.
			System.exit(0);
		}
	}
}
