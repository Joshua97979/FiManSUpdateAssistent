package gui;
//By Joshua Froitzheim 2024
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import src.Update;

@SuppressWarnings("serial")
//inspiert by FilterComboBox from gtiwari333
//https://stackoverflow.com/questions/10368856/jcombobox-filter-in-java-look-and-feel-independent
public class SearchableJComboBox extends JComboBox<Update>{
	
	private Update[] originalItems;
	private String originalText;
	private final JTextField textField;
	private Highlighter highlighter;
    private HighlightPainter painter;
	
	public SearchableJComboBox() {
		this(new Update[0]);
	}
	
	public SearchableJComboBox(Update[] model) {
		super(model);
		this.originalItems = model;
		this.setEditable(true);
		
		textField = (JTextField) this.getEditor().getEditorComponent();
		highlighter = textField.getHighlighter();
		painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(217, 217, 217));
		
		textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
            	if (ke.getKeyCode() == 38) { // up
					return;
				} else if (ke.getKeyCode() == 40) { // down
					//System.out.println(textField.getText());
					return;
				}
            	SwingUtilities.invokeLater(new Runnable() {
            		public void run() {
            			int caretPos = textField.getCaretPosition();
            			String enteredText = textField.getText().substring(0, caretPos);
            			String fullText = textField.getText();
            			
            			textField.setText(enteredText);
            			setModelButKeepText();
            			if (!isPopupVisible()) {
            				showPopup();
            			}
            			removeSelectionFromText();
            			textField.setText(fullText);
            			
	            		if (enteredText.equals("") ) {
	            			textField.setText(enteredText);
	            		} else {
	            			int index = getClosestItemIndexToText(enteredText);
	            			if (index >= 0) {
	            				String closestItem = originalItems[index].toString();
	            				//System.out.println("ClosestItem: " + closestItem);
	            				int fromIndex = closestItem.toLowerCase().indexOf(enteredText.toLowerCase());
	            				if (fromIndex != -1) {
	            					fromIndex += enteredText.length();
	            					String autocompleteText = closestItem.substring(fromIndex);
	            					textField.setText(enteredText + autocompleteText);
	            					textField.setCaretPosition(caretPos);
	            					int p0 = caretPos;
	                                int p1 = textField.getText().length();
	                                try {
	                                    highlighter.removeAllHighlights();
	                                    highlighter.addHighlight(p0, p1, painter);
	                                } catch (BadLocationException e) {
	                                    e.printStackTrace();
	                                }
	                                
	                                
	                                
	            				} else {
	            					textField.setText(enteredText);
	            				}
	        				}
	            		}
            		}
            	});
            }
		});
		
		textField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				highlighter.removeAllHighlights();
				if (textField.getText().equals("")) {
					textField.setText(originalText);
					return;
				}
				int index = getClosestItemIndexToText(textField.getText());
				if (index != getSelectedIndex()) { // Set the item only if the item at the index is different from current.
					setSelectedItem(originalItems[index]);
					//System.out.println("Closest item: " + originalItems[index]);
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				originalText = textField.getText();
				textField.setText("");
				int selectedIndex = getSelectedIndex();
				setModelButKeepText();
				if (selectedIndex >= 0) {
					textField.setText(originalText);
					addSelectionToText();
				}
				if (!isPopupVisible()) {
					showPopup();
				}
			}
		});
		
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!isPopupVisible()) {
					showPopup();
				}
			}
		});
	}
	
	private void setModelButKeepText() {
		String enteredText = textField.getText(); //save enteredText from setModel()
		this.setModel(getFilteredModel(enteredText));
		setSelectedItem(enteredText);
		getRootPane().validate();
	}
	
	private void addSelectionToText() {
		textField.requestFocus();
		int textLength = textField.getDocument().getLength();
		textField.setSelectionStart(0);
		textField.setSelectionEnd(textLength);
	}
	
	private void removeSelectionFromText() {
		textField.requestFocus();
		int textLength = textField.getDocument().getLength();
		textField.setSelectionStart(textLength);
		textField.setSelectionEnd(textLength);
	}
	
	@Override
	public int getSelectedIndex() {
		Object obj = dataModel.getSelectedItem();
		if (obj == null) return -1;
		if (originalItems == null) return -1;
        for (int i = 0; i < originalItems.length; i++) {
            if (obj.equals(originalItems[i])) return i;
        }
        return -1;
	}
	
	@Override
	public void addItem(Update item) {
		super.addItem(item);
		
		Update[] newItems = new Update[originalItems.length+1];
		for (int i = 0; i < originalItems.length; i++) {
			newItems[i] = originalItems[i];
		}
		newItems[originalItems.length] = item;
		this.originalItems = newItems;
	}
	
	public void setFilteredModel(String enteredText) {
		
		
		ArrayList<Update> filteredItems = new ArrayList<Update>();
		for (int i = 0; i < originalItems.length; i++) {
			if (originalItems[i].toString().toLowerCase().contains(enteredText.toLowerCase())) {
				filteredItems.add(originalItems[i]);
			}
		}
		if (filteredItems.size() > 0) {;
			this.setModel(new DefaultComboBoxModel<Update>(filteredItems.toArray(new Update[0])));
			this.setSelectedItem(enteredText);
			if (!this.isPopupVisible()) {
				this.showPopup();
			}
        }
        else {
        	this.hidePopup();
        }
	}
	
	private ArrayList<Update> getFilteredList(String enteredText) {
		ArrayList<Update> filteredItems = new ArrayList<Update>();
		for (int i = 0; i < originalItems.length; i++) {
			if (originalItems[i].toString().toLowerCase().contains(enteredText.toLowerCase())) {
				filteredItems.add(originalItems[i]);
			}
		}
		
		return filteredItems;
	}
	
	public DefaultComboBoxModel<Update> getFilteredModel(String enteredText) {
		ArrayList<Update> filteredItems = getFilteredList(enteredText);
		return new DefaultComboBoxModel<Update>(filteredItems.toArray(new Update[0]));
	}

	private int getClosestVersionIndex(ArrayList<String> versions, String enteredText) {
		if (enteredText.length() > 4) {
			enteredText = enteredText.substring(0, 3);
		}
		ArrayList<String> shortenedVersions = new ArrayList<String>();
		for (int i = 0; i < versions.size(); i++) {
			if (versions.get(i).length() < enteredText.length()) {
				shortenedVersions.add(versions.get(i));
			} else {
				shortenedVersions.add(versions.get(i).substring(0, enteredText.length()));
			}
		}
		return shortenedVersions.indexOf(enteredText);
	}
	
	private int getColosestDateIndex(ArrayList<String> dates, String enteredText) {
		if (enteredText.length() > 10) {
			enteredText = enteredText.substring(0, 9);
		}
		ArrayList<String> shortenedDates = new ArrayList<String>();
		for (int i = 0; i < dates.size(); i++) {
			shortenedDates.add(dates.get(i).substring(0, enteredText.length()));
		}
		return shortenedDates.indexOf(enteredText);
	}

	public int getClosestItemIndexToText(String enteredText) {
		if (enteredText.equals("") == true) return -1;
		if (originalItems.length <= 0) return -1;
		ArrayList<Update> filteredItems = getFilteredList(enteredText);
		if (filteredItems.size() <= 0) return 0;
		ArrayList<String> versions = new ArrayList<String>();
		ArrayList<String> dates = new ArrayList<String>();
		int index = -1;
		for (Update item : filteredItems) {
			String version = Integer.toString(item.getVersion());
			String date =  item.getReleasedate();
			versions.add(version);
			dates.add(date);
		}
		index = getClosestVersionIndex(versions, enteredText);
		if (index == -1) {
			index = getColosestDateIndex(dates, enteredText);
		}
		if (index == -1) {
			index = 0;
		}
		return Arrays.asList(originalItems).indexOf(filteredItems.get(index));
	}

    public static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}
