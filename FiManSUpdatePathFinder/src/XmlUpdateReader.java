import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class XmlUpdateReader {

	private ArrayList<Update> jamUpdates;
	private ArrayList<Update> assUpdates;
	private ArrayList<Update> fibUpdates;
	
	Map<Update, Update> faultyUpdates; //faulty, valid
	
	SimpleDateFormat sdformat;
	File updatesDataFile;
	
	Document doc;

	private boolean elementsExistsInElement(String searchString, Element rootElement) {
		if (rootElement.getElementsByTagName(searchString).getLength() > 0)
			return true;
		return false;
	}

	private String[] getElementsTextIfElementsExist(String searchString, Element rootElement) {
		if (elementsExistsInElement(searchString, rootElement)) {
			String[] elementsStrings = new String[rootElement.getElementsByTagName(searchString).getLength()];
			for (int i = 0; i < elementsStrings.length; i++) {
				elementsStrings[i] = rootElement.getElementsByTagName(searchString).item(i).getTextContent();
				if (elementsStrings[i].length() <= 0) return null;
			}
			return elementsStrings;
		}
		return null;
	}
	
	private String getFormatetDate(String oldDate) {
		//Ensuring the formatting of releasedate
		String newDate = oldDate;
		try { Date dateAsDate;	
		dateAsDate = sdformat.parse(oldDate); // To Date
		newDate = sdformat.format(dateAsDate); // Back to Sring
		} catch (ParseException e) {e.printStackTrace();}
		return newDate;
	}
	
	private Update getUpdateFromElement(Element element) {
		String type = null;
		String version = null;
		String releasedate = null;
		String link = null;
		
		if (getElementsTextIfElementsExist("type", element) != null) {
			type = (getElementsTextIfElementsExist("type", element))[0]; // returns NULL if it doesn't exist
		}
		if (getElementsTextIfElementsExist("version", element) != null) {
			version = (getElementsTextIfElementsExist("version", element))[0];
		}
		if (getElementsTextIfElementsExist("releasedate", element) != null) {
			releasedate = (getElementsTextIfElementsExist("releasedate", element))[0];
		}
		if (getElementsTextIfElementsExist("link", element) != null) {
			link = (getElementsTextIfElementsExist("link", element))[0];
		}

		if (type == null || version == null || releasedate == null) return null;

		releasedate = getFormatetDate(releasedate);
		return new Update(type, Integer.parseInt(version), releasedate, link);
	}
	
	public Map<Update, Update> getFaultyUpdates() {
		//Dependencies are all new objects!
		NodeList list = doc.getElementsByTagName("faultyUpdate");
		faultyUpdates = new HashMap<>();
		for (int i = 0; i < list.getLength(); i++) {
			Node updateFromList = list.item(i);

			if (updateFromList.getNodeType() != Node.ELEMENT_NODE) continue;
			Element element = (Element) updateFromList;		
			
			Element faultyElement = (Element) element.getElementsByTagName("faulty").item(0);
			Update faultyUpdate = getUpdateFromElement(faultyElement);
			if (faultyUpdate == null) continue;
			//Dependencies are not important for faultyUpdate
			
			

			Element validElement = (Element) element.getElementsByTagName("valid").item(0);
			Update validUpdate = getUpdateFromElement(validElement);
			if (validUpdate == null) continue;
			
			if (hasJAMDependency(validElement) == false && hasASSDependency(validElement) == false && hasFIBDependency(validElement) == false) continue;
			
			String[] dependencyTypes = getElementsTextIfElementsExist("dependentType", validElement);
			String[] dependencyVersions = getElementsTextIfElementsExist("dependentVersion", validElement);
			String[] dependencyReleasedates = getElementsTextIfElementsExist("dependentReleasedate", validElement);
			
			if (dependencyTypes == null || dependencyVersions == null || dependencyReleasedates == null) continue;
			
			for (int d = 0; d < dependencyTypes.length; d++) {
				
				if (dependencyTypes[d].equals("JAM")) {
					validUpdate.setJamDependency(new Update(dependencyTypes[d], Integer.parseInt(dependencyVersions[d]), dependencyReleasedates[d]));
				} else if (dependencyTypes[i].equals("ASS")) {
					validUpdate.setAssDependency(new Update(dependencyTypes[d], Integer.parseInt(dependencyVersions[d]), dependencyReleasedates[d]));
				} else if (dependencyTypes[i].equals("FIB")) {
					validUpdate.setFibDependency(new Update(dependencyTypes[d], Integer.parseInt(dependencyVersions[d]), dependencyReleasedates[d]));
				}
			}
			
			faultyUpdates.put(faultyUpdate, validUpdate);
			//TODO Find duplicates
		}
		return faultyUpdates;
	}
	
	public void readUpdates() throws UpdateNotFoundException, SelfReferencingUpdateException, IllegalUpdateException, DuplicateUpdateException {
		NodeList list = doc.getElementsByTagName("update");
		for (int i = 0; i < list.getLength(); i++) {
			Node updateFromList = list.item(i);

			if (updateFromList.getNodeType() != Node.ELEMENT_NODE) continue;
			Element element = (Element) updateFromList;
			Update newUpdate = getUpdateFromElement(element);
			
			
			if (newUpdate != null) {
				if (newUpdate.getType().equals("JAM")) {		
					jamUpdates.add(newUpdate);
				} else if (newUpdate.getType().equals("ASS")) {
					assUpdates.add(newUpdate);
				} else if (newUpdate.getType().equals("FIB")) {
					fibUpdates.add(newUpdate);
				} else {
					throw new IllegalUpdateException(newUpdate, updatesDataFile.getAbsolutePath());
				}
			}
		}
		
		catchDuplicates();
		
		//second Loop
		for (int i = 0; i < list.getLength(); i++) {
			Node updateFromList = list.item(i);

			if (updateFromList.getNodeType() != Node.ELEMENT_NODE) continue;
			Element element = (Element) updateFromList;
			Update newUpdate = getUpdateFromElement(element);
			if (newUpdate == null) continue;
			
			if (hasJAMDependency(element) == false && hasASSDependency(element) == false && hasFIBDependency(element) == false) continue;
			
			String[] dependencyTypes = getElementsTextIfElementsExist("dependentType", element);
			String[] dependencyVersions = getElementsTextIfElementsExist("dependentVersion", element);
			String[] dependencyReleasedates = getElementsTextIfElementsExist("dependentReleasedate", element);
			
			if (dependencyTypes == null || dependencyVersions == null || dependencyReleasedates == null) continue;
			
			for (int d = 0; d < dependencyTypes.length; d++) {
				
				if (newUpdate.getType().equals(dependencyTypes[d]) && newUpdate.getVersion() == (Integer.parseInt(dependencyVersions[d])) && newUpdate.getReleasedate().equals(dependencyReleasedates[d])) {
					throw new SelfReferencingUpdateException(updatesDataFile.getAbsolutePath(), new Update(newUpdate.getType(), newUpdate.getVersion(), newUpdate.getReleasedate()));
				}
				
				
				if (newUpdate.getType().equals("JAM")) {
					setDependency(newUpdate.getType(), newUpdate.getVersion(), newUpdate.getReleasedate(), dependencyTypes[d], dependencyVersions[d], dependencyReleasedates[d],  jamUpdates);
				} else if (newUpdate.getType().equals("ASS")) {
					setDependency(newUpdate.getType(), newUpdate.getVersion(), newUpdate.getReleasedate(), dependencyTypes[d], dependencyVersions[d], dependencyReleasedates[d],  assUpdates);
				} else if (newUpdate.getType().equals("FIB")) {
					setDependency(newUpdate.getType(), newUpdate.getVersion(), newUpdate.getReleasedate(), dependencyTypes[d], dependencyVersions[d], dependencyReleasedates[d],  fibUpdates);
				}
			}
		}
	}

	private void catchDuplicates() throws DuplicateUpdateException {
		ArrayList<Update> uniqueUpdates = new ArrayList<Update>();
		ArrayList<Update> duplicates = new ArrayList<Update>();
		for (Update update : jamUpdates) {
			if (uniqueUpdates.contains(update)) {
				duplicates.add(update);
			} else {
				uniqueUpdates.add(update);
			}
		}
		for (Update update : assUpdates) {
			if (uniqueUpdates.contains(update)) {
				duplicates.add(update);
			} else {
				uniqueUpdates.add(update);
			}
		}
		for (Update update : fibUpdates) {
			if (uniqueUpdates.contains(update)) {
				duplicates.add(update);
			} else {
				uniqueUpdates.add(update);
			}
		}
		if (duplicates.size() > 0) {
			throw DuplicateUpdateException.create(duplicates, updatesDataFile.getAbsolutePath());
		}
	}
	
	public XmlUpdateReader(String pathToFile, Model model, boolean handleMissingUpdatesFile) throws FileNotFoundException, SAXException {
		updatesDataFile = new File(pathToFile);
		
		/*If handleMissingUpdatesFile is set to true, the corresponding method in the model is called when the updatesData file does not exist.
		Within this method, the user is given several options to resolve the issue.

		This mechanism is redundant in the options menu, as it is already impossible to proceed there with a missing updatesData file.
		In that context, a missing file is handled via a FileNotFoundException.

		The handleMissingUpdatesFile method should only be called if the updatesData file specified in the configuration file cannot be found when the program starts.*/
		if (!updatesDataFile.exists() && handleMissingUpdatesFile == false) throw new FileNotFoundException();
		if (!updatesDataFile.exists() && handleMissingUpdatesFile) updatesDataFile = model.handleMissingUpdatesFile(updatesDataFile);
			
		jamUpdates = new ArrayList<Update>();
		assUpdates = new ArrayList<Update>();
		fibUpdates = new ArrayList<Update>();
		
		sdformat = new SimpleDateFormat("dd.MM.yyyy");
		
		// Instantiate the Factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// optional, but recommended
		// process XML securely, avoid attacks like XML External Entities (XXE)
		try {
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			// parse XML file
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(updatesDataFile);
			// optional, but recommended
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException | IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Update> getJamUpdates() {
		return jamUpdates;
	}

	public ArrayList<Update> getAssUpdates() {
		return assUpdates;
	}

	public ArrayList<Update> getFibUpdates() {
		return fibUpdates;
	}

	private void setDependency(String type, int version, String releasedate, String dependencyType, String dependencyVersion, String dependencyReleasedate, ArrayList<Update> list) throws UpdateNotFoundException {
		int index = new Update(type, version, releasedate).findIndexOfMeIn(list);
		if (index == -1) {//findIndexOf returns -1 if not found
			throw new UpdateNotFoundException(updatesDataFile.getAbsolutePath(), new Update(dependencyType, Integer.parseInt(dependencyVersion), dependencyReleasedate));
			//return;
		}
		
		int dIndex;
		if (dependencyType.equals("JAM")) {
			dIndex = new Update(dependencyType, Integer.parseInt(dependencyVersion), dependencyReleasedate).findIndexOfMeIn(jamUpdates);
			if (dIndex == -1) {//findIndexOf returns -1 if not found
				throw new UpdateNotFoundException(updatesDataFile.getAbsolutePath(), new Update(dependencyType, Integer.parseInt(dependencyVersion), dependencyReleasedate));
			}
			list.get(index).setJamDependency(jamUpdates.get(dIndex));
		} else if (dependencyType.equals("ASS")) {
			dIndex = new Update(dependencyType, Integer.parseInt(dependencyVersion), dependencyReleasedate).findIndexOfMeIn(assUpdates);
			if (dIndex == -1) {//findIndexOf returns -1 if not found
				throw new UpdateNotFoundException(updatesDataFile.getAbsolutePath(), new Update(dependencyType, Integer.parseInt(dependencyVersion), dependencyReleasedate));
			}
			list.get(index).setAssDependency(assUpdates.get(dIndex));
		} else if (dependencyType.equals("FIB")) {
			dIndex = new Update(dependencyType, Integer.parseInt(dependencyVersion), dependencyReleasedate).findIndexOfMeIn(fibUpdates);
			if (dIndex == -1) {//findIndexOf returns -1 if not found
				throw new UpdateNotFoundException(updatesDataFile.getAbsolutePath(), new Update(dependencyType, Integer.parseInt(dependencyVersion), dependencyReleasedate));
			}
			list.get(index).setFibDependency(fibUpdates.get(dIndex));
		}
	}
	
	private boolean hasJAMDependency(Element element) {
		if (hasDependency(element) == false) return false;
		String[] dependentTypes = getElementsTextIfElementsExist("dependentType", element);
		if (dependentTypes == null) return false;
		for (int i = 0; i < dependentTypes.length; i++) {
			if (dependentTypes[i].equals("JAM")) return true;
		}
		return false;
	}
	
	private boolean hasASSDependency(Element element) {
		if (hasDependency(element) == false) return false;
		String[] dependentTypes = getElementsTextIfElementsExist("dependentType", element);
		if (dependentTypes == null) return false;
		for (int i = 0; i < dependentTypes.length; i++) {
			if (dependentTypes[i].equals("ASS")) return true;
		}
		return false;
	}
	
	private boolean hasFIBDependency(Element element) {
		if (hasDependency(element) == false) return false;
		String[] dependentTypes = getElementsTextIfElementsExist("dependentType", element);
		if (dependentTypes == null) return false;
		for (int i = 0; i < dependentTypes.length; i++) {
			if (dependentTypes[i].equals("FIB")) return true;
		}
		return false;
	}
	
	private boolean hasDependency(Element element) {
		if (elementsExistsInElement("dependency", element) == false) return false;
		return true;
	}
}
