package src;
//By Joshua Froitzheim 2023
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JOptionPane;

import Exceptions.PassedStateNotFoundException;
import gui.DescriptionPanel;
import gui.OptionPanel;
import gui.View;

public class Controller {

	private View view;
	private Model model;
	
	public Controller(Model model) {
		this.model = model;
		this.view = new View(this, model.getBackgroundColor(), model.getSecondaryBackgroundColor(), model.getForegroundColor(), model.getFontSize(), model.getSecondaryForegroundColor());
	}
	
	public void addPanel(Update update) {
		view.addPanel(update);
	}
	
	public void addOptionalPanel(Update update) {
		view.addOptionalPanel(update);
	}
	
	public void addReleasePanel(Update update) {
		view.addReleasePanel(update);
	}
	
	public void setTypes(String[] types) {
		view.setTypes(types);
	}
	
	public void typeChanged(String selectedType) {
		model.typeChanged(selectedType);
	}

	public void targetUpdateChanged(int selectedIndex) {
		model.targetUpdateChanged(selectedIndex);
	}

	public void insJamChanged(int selectedIndex) {
		model.insJamChanged(selectedIndex);
	}

	public void insAssChanged(int selectedIndex) {
		model.insAssChanged(selectedIndex);
	}
	
	public void insFibChanged(int selectedIndex) {
		model.insFibChanged(selectedIndex);
	}
	
	public void addTargetUpdateInput(ArrayList<Update> list) {
		view.addTargetUpdateInput(list);
	}
	
	public void addInsJamUpdateInput(ArrayList<Update> list) {
		view.addInsJamUpdateInput(list);
	}
	
	public void addInsAssUpdateInput(ArrayList<Update> list) {
		view.addInsAssUpdateInput(list);
	}
	
	public void addInsFibUpdateInput(ArrayList<Update> list) {
		view.addInsFibUpdateInput(list);
	}
	
	public void removeTargetUpdateInput() {
		view.removeTargetUpdateInput();
	}
	
	public void removeJamUpdateInput() {
		view.removeJamUpdateInput();
	}
	
	public void removeAssUpdateInput() {
		view.removeAssUpdateInput();
	}
	
	public void removeFibUpdateInput() {
		view.removeFibUpdateInput();
	}
	
	public void removeTargetFibButton() {
		view.removeTargetFibButton();
	}
	
	public void removeTargetAssButton() {
		view.removeTargetAssButton();
	}

	public void clearList() {
		view.clearList();
	}

	public void showMessageDialog(String message) {
		view.showMessageDialog(message);
	}

	public void setAssClipboard() {
		model.setAssClipboard();
	}
	
	public void setFibClipboard() {
		model.setFibClipboard();
	}

	public void openDesciptionPressed(Update update) {
		model.openDesciptionPressed(update);
	}

	public void setDesciption(Update update) {
		view.setDesciption(update);
	}
	
	public void addAssButton() {
		view.addAssButton();
	}
	
	public void addFibButton() {
		view.addFibButton();
	}
	
	public void addTargetFibButton() {
		view.addTargetFibButton();
	}
	
	public void addTargetAssButton() {
		view.addTargetAssButton();
	}
	
	public void addAppCheckBox() {
		view.addAppCheckBox();
	}

	public void removeAppChckbx() {
		view.removeAppChckbx();
	}
	
	public boolean appChckbxIsSelected() {
		return view.appChckbxIsSelected();
	}
	
	public void setAppChckbx(boolean state) {
		view.setAppChckbx(state);
	}

	public void appCheckBoxChanged(boolean selected) {
		model.appCheckBoxChanged(selected);
	}

	public void saveDataToFile() {
		model.saveDataToFile();
	}

	public void loadDataFromFile() {
		model.loadDataFromFile();
	}
	
	public void saveDataToClipboard() {
		model.saveDataToClipboard();
	}

	public void loadDataFromClipboard() {
		model.loadDataFromClipboard();
	}
	
	public void setBoxStates(Map<Update, ArrayList<Boolean>> correlatingBoxStates) throws PassedStateNotFoundException {
		view.setBoxStates(correlatingBoxStates);
	}
	
	public void setBoxStates(Map<Update, ArrayList<Boolean>> correlatingBoxStates, boolean showOriginalStateNotFoundException) throws PassedStateNotFoundException {
		view.setBoxStates(correlatingBoxStates, showOriginalStateNotFoundException);
	}
	
	public void setTypeComboBox(String type) {
		view.setTypeComboBox(type);
	}
	
	public void setTargetUpdateComboBox(int index) {
		view.setTargetUpdateComboBox(index);
	}
	
	public void setInsJamUpdateComboBox(int index) {
		view.setInsJamUpdateComboBox(index);
	}
	
	public void setInsAssUpdateComboBox(int index) {
		view.setInsAssUpdateComboBox(index);
	}
	
	public void setInsFibUpdateComboBox(int index) {
		view.setInsFibUpdateComboBox(index);
	}

	public static String showInputDialog(String message, String currentPath) {
		return JOptionPane.showInputDialog(message, currentPath);
	}
	
	public int showOptionDialog(String message, Object[] options) {
		return JOptionPane.showOptionDialog(view , message, "Option auswählen", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, options, options[0]);
	}
	
	public boolean saveOptions(OptionPanel optionPanel) {
		return model.saveOptions(optionPanel);
	}

	public void exportListToFile() {
		model.exportListToFile();
	}

	public void exportListToClipboard() {
		model.exportListToClipboard();
	}
	
	public void setTextfelds(Map<String, String> userInputsTxFlds) {
		view.setTextfelds(userInputsTxFlds);
	}

	public Map<String, String> getInputs() {
		return view.getTextfeldsValues();
	}

	public void setDesciptionInputs(Map<String, String> userInputs) {
		view.setTextfelds(userInputs);
	}

	public boolean desciptionExists() {
		return view.desciptionExists();
	}

	public DescriptionPanel getNewDescriptionPanel(Update update) {
		return model.getNewDescriptionPanel(update);
	}

	public Font getFont() {
		return view.getFont();
	}

	public OptionPanel getOptionPanel() {
		return model.getOptionPanel();
	}

	public void removeOptionPanel(OptionPanel optionPanel) {
		view.removeOptionPanel(optionPanel);
	}

	public void optionCancelPressed(OptionPanel optionPanel) {
		model.optionCancelPressed(optionPanel);
	}

	public void optionSavePressed(OptionPanel optionPanel) {
		model.optionSavePressed(optionPanel);
	}

	public void programClosing() {
		model.programClosing();
	}

	public void checkForVedaUpdatesNow() {
		model.checkForVedaUpdatesNow();
	}

	public void setSplitPaneDivider() {
		view.setSplitPaneDivider();
	}
	
	public void setSplitPaneDivider(double location) {
		view.setSplitPaneDivider(location);
	}

	public int getScrollPos() {
		return view.getScrollPos();
	}

	public void setScrollPos(int scrollPos) {
		view.setScrollPos(scrollPos);
	}

	public Map<Update, ArrayList<Boolean>> getCorrelatingCheckBoxStates() {
		return view.getCorrelatingCheckBoxStates();
	}

	public void dropEvent(Transferable transferable) {
		model.dropEvent(transferable);
	}
	
	public void removeDescriptionPanel() {
		view.removeDescriptionPanel();
	}
	
	public Component getParentForPositionOnly() {
		return view; //I really don't like this but there doesn't seem to be any other way to position the JFileChooser
	}

	public void newList() {
		model.newList();
	}
	public void setGraficCheckBx(ArrayList<Update> selectedPanels, boolean newState) {
		model.setGraficCheckBx(selectedPanels, newState);
	}

	public void setClassicCheckBx(ArrayList<Update> selectedPanels, boolean newState) {
		model.setClassicCheckBx(selectedPanels, newState);
	}
	
	public void setMaxPanelWidth(int newSize) {
		view.setMaxPanelWidth(newSize);
	}
	
	public void setMinPanelWidth(int newWidth) {
		view.setMinPanelWidth(newWidth);
	}
	
	public void fireDividerPropertyChange() {
		view.fireDividerPropertyChange();
	}

	public int getMinPanelSize() {
		return view.getMinPanelSize();
	}

	public Color openColorChooser(Color initialColor) {
		return view.openColorChooser(initialColor);
	}
	
	public void chooseBackgroundColor(OptionPanel optionPanel) {
		model.chooseBackgroundColor(optionPanel);
	}

	public void chooseSecondaryBackgroundColor(OptionPanel optionPanel) {
		model.chooseSecondaryBackgroundColor(optionPanel);
		
	}

	public void chooseForegroundColor(OptionPanel optionPanel) {
		model.chooseForegroundColor(optionPanel);
	}

	public void chooseSecondaryForegroundColor(OptionPanel optionPanel) {
		model.chooseSecondaryForegroundColor(optionPanel);
	}

	public String getPathToUpdate() {
		return model.getPathToUpdate();
	}

	public String getPathToFibUpdate() {
		return model.getPathToFibUpdate();
	}

	public String getPathToFibRelease() {
		return model.getPathToFibRelease();
	}
	
	public String getPathToAss() {
		return model.getPathToAss();
	}
	
	public String getPathToJam() {
		return model.getPathToJam();
	}
}
