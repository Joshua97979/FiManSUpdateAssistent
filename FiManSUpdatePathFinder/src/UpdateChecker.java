//By Joshua Froitzheim 2023
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class UpdateChecker {
	private ArrayList<Update> allUpdates;
	private static String html;
	private static String url = "http://updates.veda.net/autoupdate/updates.xml";
	
	
	public UpdateChecker(ArrayList<Update> jamUpdates, ArrayList<Update> assUpdates) throws UrlNotFoundException {
		allUpdates = new ArrayList<Update>();
		allUpdates.addAll(jamUpdates);
		allUpdates.addAll(assUpdates);
		
		html = getHTMLasString(url);
	}
	
	private static Update stringToUpdate(String strUpdate) {
		return new Update(getNextProduct(strUpdate), Integer.parseInt(getNextRelease(strUpdate)), getNextDate(strUpdate), getNextURL(strUpdate));
	}
	
	public static Update getSpecificUpdate(Update searchUpdate) {
		String[] updates = html.split("<Update ");
		
		for (int i = 0; i < updates.length; i++) {
			if (isValidUpdate(updates[i]) == false) continue;
			if (getNextType(updates[i]).equals("ci") == false) continue;
			if (getNextProduct(updates[i]).equals("ASS") == false &&
					getNextProduct(updates[i]).equals("JAM") == false) continue;
			
			Update tmpUpdate = stringToUpdate(updates[i]);
			
			if (tmpUpdate.equals(searchUpdate)) {
				String[] dependencys = getDependencys(updates[i]);
				for (int j = 0; j < dependencys.length; j++) {
					if (isValidUpdate(dependencys[j]) == false) continue;
					Update tmpDependencyUpdate = new Update(getNextProduct(dependencys[j]), Integer.parseInt(getNextRelease(dependencys[j])), getNextDate(dependencys[j]), getNextURL(updates[i]));
					if (tmpDependencyUpdate.getType().equals("JAM")) {
						tmpUpdate.setJamDependency(tmpDependencyUpdate);
					} else if (tmpDependencyUpdate.getType().equals("ASS")) {
						tmpUpdate.setAssDependency(tmpDependencyUpdate);
					}
				}
				return tmpUpdate;
			}
		}
		return null;
	}

	public ArrayList<Update> getNewUpdates() {
		String[] updates = html.split("<Update ");
		
		ArrayList<Update> newUpdates = new ArrayList<Update>();
		
		for (int i = 0; i < updates.length; i++) {
			
			if (isValidUpdate(updates[i]) == false) continue;
			if (getNextType(updates[i]).equals("ci") == false) continue;
			if (getNextProduct(updates[i]).equals("ASS") == false &&
					getNextProduct(updates[i]).equals("JAM") == false) continue;
			
			Update tmpUpdate = new Update(getNextProduct(updates[i]), Integer.parseInt(getNextRelease(updates[i])), getNextDate(updates[i]), getNextURL(updates[i]));
			if (allUpdates.contains(tmpUpdate) == false) {
				String[] dependencys = getDependencys(updates[i]);
				for (int j = 0; j < dependencys.length; j++) {
					if (isValidUpdate(dependencys[j]) == false) continue;
					Update tmpDependencyUpdate = new Update(getNextProduct(dependencys[j]), Integer.parseInt(getNextRelease(dependencys[j])), getNextDate(dependencys[j]), getNextURL(updates[i]));
					if (tmpDependencyUpdate.getType().equals("JAM")) {
						tmpUpdate.setJamDependency(tmpDependencyUpdate);
					} else if (tmpDependencyUpdate.getType().equals("ASS")) {
						tmpUpdate.setAssDependency(tmpDependencyUpdate);
					}
				}
				newUpdates.add(tmpUpdate);
			}
		}
		return newUpdates;
	}
	
	private static String[] getDependencys(String update) {
		update = update.substring(update.indexOf("<Condition"));
		return update.split("<Condition");
	}
	
	private static boolean isValidUpdate(String update) {
		if (update.indexOf("<Condition") > 0) update = update.substring(0, update.indexOf("<Condition"));
		if (update.contains("Product") == false) return false;
		if (update.contains("Release") == false) return false;
		if (update.contains("Date") == false) return false;
		return true;
	}
	
	public static String getNextAttributeOf(String update, String attribute) {
		String returnAtt = update.substring(update.indexOf(attribute + "=\"") + attribute.length() + 2);
		if (returnAtt.length() == 0) return "";
		returnAtt = returnAtt.substring(0, returnAtt.indexOf("\""));
		return returnAtt;
	}
	
	private static String getNextType(String update) {
		return getNextAttributeOf(update, "Type");
	}
	
	private static String getNextProduct(String update) {
		return getNextAttributeOf(update, "Product");
	}
	
	private static String getNextRelease(String update) {
		return getNextAttributeOf(update, "Release");
	}
	
	private static String getNextDate(String update) {
		return getNextAttributeOf(update, "Date");
	}
	
	private static String getNextURL(String update) {
		return getNextAttributeOf(update, "URL");
	}
	
	public static int showNewUpdates(ArrayList<Update> newUpdates, String pathToUpdates) {
		String message = "";
		for (int i = 0; i < newUpdates.size(); i++) {
			message += newUpdates.get(i).toString() + "\n";
			message += "\t" + newUpdates.get(i).getLink() + "\n";
			if (newUpdates.get(i).getAssDependency() != null) {
				message += "\tAssDependency: " + newUpdates.get(i).getAssDependency().toString() + "\n";
			}
			if (newUpdates.get(i).getJamDependency() != null) {
				message += "\tJamDependency: " + newUpdates.get(i).getJamDependency().toString() + "\n";
			}
			message += "\n";
		}
		
		JPanel panelCenter = new JPanel(new BorderLayout(0, 5));
		
		JScrollPane scrollPane = new JScrollPane();
		JTextArea textArea = new JTextArea(10, 80);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		panelCenter.add(scrollPane, BorderLayout.CENTER);
		
		textArea.setText(message);
		
		JPanel panelTop = new JPanel(new BorderLayout(0, 5));
		panelCenter.add(panelTop, BorderLayout.NORTH);
		
		JLabel lblTopText = new JLabel("Neue Updates bei VEDA gefunden!");
		lblTopText.setHorizontalAlignment(SwingConstants.CENTER);
		panelTop.add(lblTopText, BorderLayout.NORTH);
		
		String displayedName = "updates.veda.net";
		JLabel lblTopLink = new JLabel(displayedName);
		lblTopLink.setHorizontalAlignment(SwingConstants.CENTER);
		lblTopLink.setForeground(Color.BLUE.darker());
		lblTopLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblTopLink.addMouseListener(new MouseAdapter() {
    	    @Override
    	    public void mouseClicked(MouseEvent e) {
    	        try {
    	             
    	            Desktop.getDesktop().browse(new URI(url));
    	             
    	        } catch (IOException | URISyntaxException e1) {
    	            e1.printStackTrace();
    	        }
    	    }
    	    @Override
            public void mouseExited(MouseEvent e) {
    	    	lblTopLink.setText(displayedName);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            	lblTopLink.setText("<html><a href=''>" + displayedName + "</a></html>");
            }
    	});
		panelTop.add(lblTopLink, BorderLayout.SOUTH);
		

		
		JPanel panelBottom = new JPanel();
		panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.Y_AXIS));
		panelCenter.add(panelBottom, BorderLayout.SOUTH);
		
		JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.add(new JLabel("<html><div style='text-align: center;'>Bitte in UpdateData.xml einpflegen,<br>oder diese Nachricht in den Einstellungen deaktivieren.</html>"));
        panelBottom.add(infoPanel);
        
		String displayedNameBottom = pathToUpdates;
		
		JLabel lblBottomLink = new JLabel(displayedNameBottom);
		lblBottomLink.setForeground(Color.BLUE.darker());
		lblBottomLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblBottomLink.addMouseListener(new MouseAdapter() {
    	    @Override
    	    public void mouseClicked(MouseEvent e) {
    	    	try {
					Desktop.getDesktop().open(new File(pathToUpdates));
				} catch (IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(null, "Keine Datei gefunden bei: " + pathToUpdates);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
    	    }
    	    @Override
            public void mouseExited(MouseEvent e) {
    	    	lblBottomLink.setText(displayedNameBottom);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            	lblBottomLink.setText("<html><a href=''>" + displayedNameBottom + "</a></html>");
            }
    	});
        JPanel linkPanel = new JPanel(new FlowLayout());
		
		linkPanel.add(lblBottomLink);
        panelBottom.add(linkPanel);
		
		//JOptionPane.showMessageDialog(null, panelCenter, "Neue Updates", JOptionPane.PLAIN_MESSAGE);
		return JOptionPane.showOptionDialog(null , panelCenter, "Neue Updates", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, new String[] {"Hinzufügen", "Abbruch"}, "Abbruch");
	}
	
	public static String getHTMLasString(String url) throws UrlNotFoundException {
		String html = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String inputLine = "";
			while ((inputLine = in.readLine()) != null) {
				html+= inputLine;
				html+= "\n";
			}
			in.close();
		} catch (IOException e) {
			throw new UrlNotFoundException(url);
		}
		return html;
	}
}
