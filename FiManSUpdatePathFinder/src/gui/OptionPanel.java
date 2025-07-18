package gui;

//By Joshua Froitzheim 2023
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class OptionPanel extends JPanel{
	
	private Color backgroundColor, foregroundColor;
	private JPanel centerContentPanel;
	
	private JTextField textFieldPathToUpdates, textFieldPathToFibUpdateFiles, textFieldPathToFibReleaseFiles, textFieldPathToAssFiles, textFieldPathToJamFiles;
	
	public JTextField textFieldBackgroundColor, textFieldSecondaryBackgroundColor, textFieldForegroundColor,
		textFieldSecondaryForegroundColor;
	public JSpinner spinnerFontSize;
	public JButton btnSave, btnCancel, btnChoosePathToUpdateFile, btnCheckForVedaUpdatesNow, btnChoosePathToFibUpdateFiles, btnChoosePathToFibReleaseFiles,
		btnChoosePathToAssFiles, btnChoosePathToJamFiles;
	public JLabel lblBackgroundColor, lblForegroundColor;
	public JCheckBox chckbxUpdatesCheck, chckbxOptionalUpdates, chckbxMaintainScrollPos;
	public JButton btnChooseBackgroundColor, btnChooseSecondaryBackgroundColor, btnChooseForegroundColor, btnChooseSecondaryForegroundColor;
	public JPanel backgroundColorPreview, secondaryBackgroundColorPreview, foregroundColorPreview, secondaryForegroundColorPreview;
	
	
	private int gridy;
	
	private String version;
	
	public OptionPanel(Font font, Color backgroundColor, Color foregroundColor, String version) {
		this.setFont(font);
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.gridy = 1;
		this.version = version;
		
		createGui();
	}
	
	private void createGui() {
		
		this.setLayout(new BorderLayout(0, 0));
		this.add(createAndGetNothPanel(), BorderLayout.NORTH);
		this.add(createAndGetSouthPanel(), BorderLayout.SOUTH);
		this.add(createAndGetEmptyCenterPanel(), BorderLayout.CENTER);
		
		JPanel panelFile = new JPanel();
		panelFile.setLayout(new BorderLayout(0, 0));
		textFieldPathToUpdates = new JTextField();
		textFieldPathToUpdates.setFont(this.getFont());
		panelFile.add(textFieldPathToUpdates);
		btnChoosePathToUpdateFile = new ScaledButton(getClass().getResource("/icons/Folder_open.png"), this.getFont());
		panelFile.add(btnChoosePathToUpdateFile, BorderLayout.EAST);
		addOption("Pfad zu Updates XML", panelFile);
		
		
		
		JPanel panelBackgroundColor = new JPanel();
		panelBackgroundColor.setLayout(new BorderLayout(0, 0));
		textFieldBackgroundColor = new JTextField();
		textFieldBackgroundColor.setFont(this.getFont());
		panelBackgroundColor.add(textFieldBackgroundColor);
		backgroundColorPreview = new JPanel();
		backgroundColorPreview.setBackground(Color.BLACK);
		backgroundColorPreview.setBorder(BorderFactory.createLineBorder(Color.white));
		backgroundColorPreview.setPreferredSize(new Dimension(50, backgroundColorPreview.getHeight()));
		panelBackgroundColor.add(backgroundColorPreview, BorderLayout.WEST);
		btnChooseBackgroundColor = new ScaledButton(getClass().getResource("/icons/ColorPalette_Icon.png"), this.getFont());
		btnChooseBackgroundColor.setToolTipText("ColorChooser �ffnen");
		panelBackgroundColor.add(btnChooseBackgroundColor, BorderLayout.EAST);
		addOption("Hintergrundfarbe", panelBackgroundColor);
		
		JPanel panelSecondaryBackgroundColor = new JPanel();
		panelSecondaryBackgroundColor.setLayout(new BorderLayout(0, 0));
		textFieldSecondaryBackgroundColor = new JTextField();
		textFieldSecondaryBackgroundColor.setFont(this.getFont());
		panelSecondaryBackgroundColor.add(textFieldSecondaryBackgroundColor);
		secondaryBackgroundColorPreview = new JPanel();
		secondaryBackgroundColorPreview.setBackground(Color.BLACK);
		secondaryBackgroundColorPreview.setBorder(BorderFactory.createLineBorder(Color.white));
		secondaryBackgroundColorPreview.setPreferredSize(new Dimension(50, secondaryBackgroundColorPreview.getHeight()));
		panelSecondaryBackgroundColor.add(secondaryBackgroundColorPreview, BorderLayout.WEST);
		btnChooseSecondaryBackgroundColor = new ScaledButton(getClass().getResource("/icons/ColorPalette_Icon.png"), this.getFont());
		btnChooseSecondaryBackgroundColor.setToolTipText("ColorChooser �ffnen");
		panelSecondaryBackgroundColor.add(btnChooseSecondaryBackgroundColor, BorderLayout.EAST);
		addOption("Sekund�re Hintergrundfarbe", panelSecondaryBackgroundColor);
		
		JPanel panelForegroundColor = new JPanel();
		panelForegroundColor.setLayout(new BorderLayout(0, 0));
		textFieldForegroundColor = new JTextField();
		textFieldForegroundColor.setFont(this.getFont());
		panelForegroundColor.add(textFieldForegroundColor);
		foregroundColorPreview = new JPanel();
		foregroundColorPreview.setBackground(Color.BLACK);
		foregroundColorPreview.setBorder(BorderFactory.createLineBorder(Color.white));
		foregroundColorPreview.setPreferredSize(new Dimension(50, foregroundColorPreview.getHeight()));
		panelForegroundColor.add(foregroundColorPreview, BorderLayout.WEST);
		btnChooseForegroundColor = new ScaledButton(getClass().getResource("/icons/ColorPalette_Icon.png"), this.getFont());
		btnChooseForegroundColor.setToolTipText("ColorChooser �ffnen");
		panelForegroundColor.add(btnChooseForegroundColor, BorderLayout.EAST);
		addOption("Schriftfarbe", panelForegroundColor);
		
		JPanel panelSecondaryForegroundColor = new JPanel();
		panelSecondaryForegroundColor.setLayout(new BorderLayout(0, 0));
		textFieldSecondaryForegroundColor = new JTextField();
		textFieldSecondaryForegroundColor.setFont(this.getFont());
		panelSecondaryForegroundColor.add(textFieldSecondaryForegroundColor);
		secondaryForegroundColorPreview = new JPanel();
		secondaryForegroundColorPreview.setBackground(Color.BLACK);
		secondaryForegroundColorPreview.setBorder(BorderFactory.createLineBorder(Color.white));
		secondaryForegroundColorPreview.setPreferredSize(new Dimension(50, secondaryForegroundColorPreview.getHeight()));
		panelSecondaryForegroundColor.add(secondaryForegroundColorPreview, BorderLayout.WEST);
		btnChooseSecondaryForegroundColor = new ScaledButton(getClass().getResource("/icons/ColorPalette_Icon.png"), this.getFont());
		btnChooseSecondaryForegroundColor.setToolTipText("ColorChooser �ffnen");
		panelSecondaryForegroundColor.add(btnChooseSecondaryForegroundColor, BorderLayout.EAST);
		addOption("Sekund�re Schriftfarbe", panelSecondaryForegroundColor);
		
		spinnerFontSize = new JSpinner();
		spinnerFontSize.setModel(new SpinnerNumberModel(new Float(1), new Float(0.1), new Float(100), new Float(0.1)));
		addOption("Schriftgr\u00F6\u00DFe", spinnerFontSize, false);
		
		JPanel panelPathToFibUpdateFiles = new JPanel();
		panelPathToFibUpdateFiles.setLayout(new BorderLayout(0, 0));
		textFieldPathToFibUpdateFiles = new JTextField();
		textFieldPathToFibUpdateFiles.setFont(this.getFont());
		panelPathToFibUpdateFiles.add(textFieldPathToFibUpdateFiles);
		btnChoosePathToFibUpdateFiles = new ScaledButton(getClass().getResource("/icons/Folder_open.png"), this.getFont());
		panelPathToFibUpdateFiles.add(btnChoosePathToFibUpdateFiles, BorderLayout.EAST);
		addOption("Pfad zu Fib-Update-Dateien", panelPathToFibUpdateFiles);
		
		
		
		JPanel panelPathToFibReleaseFiles = new JPanel();
		panelPathToFibReleaseFiles.setLayout(new BorderLayout(0, 0));
		textFieldPathToFibReleaseFiles = new JTextField();
		textFieldPathToFibReleaseFiles.setFont(this.getFont());
		panelPathToFibReleaseFiles.add(textFieldPathToFibReleaseFiles);
		btnChoosePathToFibReleaseFiles = new ScaledButton(getClass().getResource("/icons/Folder_open.png"), this.getFont());
		panelPathToFibReleaseFiles.add(btnChoosePathToFibReleaseFiles, BorderLayout.EAST);
		addOption("Pfad zu Fib-Release-Dateien", panelPathToFibReleaseFiles);
		
		
		
		JPanel panelPathToAssFiles = new JPanel();
		panelPathToAssFiles.setLayout(new BorderLayout(0, 0));
		textFieldPathToAssFiles = new JTextField();
		textFieldPathToAssFiles.setFont(this.getFont());
		panelPathToAssFiles.add(textFieldPathToAssFiles);
		btnChoosePathToAssFiles = new ScaledButton(getClass().getResource("/icons/Folder_open.png"), this.getFont());
		panelPathToAssFiles.add(btnChoosePathToAssFiles, BorderLayout.EAST);
		addOption("Pfad zu ASS-Dateien", panelPathToAssFiles);

		
		JPanel panelPathToJamFiles = new JPanel();
		panelPathToJamFiles.setLayout(new BorderLayout(0, 0));
		textFieldPathToJamFiles = new JTextField();
		textFieldPathToJamFiles.setFont(this.getFont());
		panelPathToJamFiles.add(textFieldPathToJamFiles);
		btnChoosePathToJamFiles = new ScaledButton(getClass().getResource("/icons/Folder_open.png"), this.getFont());
		panelPathToJamFiles.add(btnChoosePathToJamFiles, BorderLayout.EAST);
		addOption("Pfad zu JAM-Dateien", panelPathToJamFiles);
		
		
		chckbxUpdatesCheck = new JCheckBox("");
		chckbxUpdatesCheck.setBackground(backgroundColor);
		chckbxUpdatesCheck.setToolTipText("Soll bei Start des Programms nach neuen Updates von VEDA gesucht werden?  Ja | Nein");
		addOption("Pr�fen auf neue VEDA-Updates bei Start", chckbxUpdatesCheck, false);
		addLabel("(Nur ein Mal pro Tag)");
		
		btnCheckForVedaUpdatesNow = new JButton("Suchen");
		btnCheckForVedaUpdatesNow.setBackground(backgroundColor);
		btnCheckForVedaUpdatesNow.setToolTipText("Jetzt nach neuen VEDA Updates Suchen");
		addOption("Jetzt nach neuen VEDA-Updates Suchen", btnCheckForVedaUpdatesNow, false);
		
		
		chckbxOptionalUpdates = new JCheckBox("");
		chckbxOptionalUpdates.setBackground(backgroundColor);
		chckbxOptionalUpdates.setToolTipText("<html>Sollen Optionale ASS und JAM-Updates angezeigt werden?  Ja | Nein<br>(Optionale Updates sind meistens Fehlerbehebungen und sollten installiert werden)</html>");
		addOption("Optionale Updates anzeigen", chckbxOptionalUpdates, false);
		
		chckbxMaintainScrollPos = new JCheckBox("");
		chckbxMaintainScrollPos.setBackground(backgroundColor);
		chckbxMaintainScrollPos.setToolTipText("Soll die Scrollbalken Position beibehalten werden beim Wechseln von Updates?  Ja | Nein");
		addOption("Scrollbalken Position merken", chckbxMaintainScrollPos, false);
		
		
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.LINE_END;
		c.weighty = 100.0;
		c.gridheight = GridBagConstraints.REMAINDER;
		centerContentPanel.add(Box.createGlue(), c);
	}
	
	private JPanel createAndGetNothPanel() {
		JPanel panelNorth = new JPanel();
		Font boldFont = this.getFont().deriveFont(Font.BOLD);
		JLabel lblTitel = new JLabel("Einstellungen");
		lblTitel.setFont(boldFont);
		panelNorth.add(lblTitel);
		return panelNorth;
	}
	
	private JPanel createAndGetSouthPanel() {
		JPanel panelSouth = new JPanel();
		JPanel panelSouthCenter = new JPanel();
		JPanel panelSouthEast = new JPanel();
		
		btnSave = new JButton("Speichern");
		btnCancel = new JButton("Abbruch");
		btnSave.setFont(this.getFont());
		btnSave.setBackground(backgroundColor);
		btnCancel.setFont(this.getFont());
		btnCancel.setBackground(backgroundColor);
		panelSouthCenter.add(btnSave);
		panelSouthCenter.add(btnCancel);
		
		JLabel versionLabel = new JLabel(version);
		versionLabel.setFont(this.getFont());
		versionLabel.setForeground(foregroundColor);
		panelSouthEast.add(versionLabel);
		
		
		JPanel panelSouthWest = new JPanel(); //only to center buttons
		
		
		panelSouthEast.setBackground(backgroundColor);
		panelSouthCenter.setBackground(backgroundColor);
		panelSouthWest.setBackground(backgroundColor);
		
		panelSouth.setLayout(new BorderLayout());
		panelSouth.add(panelSouthCenter, BorderLayout.CENTER);
		panelSouth.add(panelSouthEast, BorderLayout.EAST);
		panelSouth.add(panelSouthWest, BorderLayout.WEST);
		
		return panelSouth;
	}
	
	private JScrollPane createAndGetEmptyCenterPanel() {
		
		centerContentPanel = new JPanel();
		centerContentPanel.setBackground(backgroundColor);
        GridBagLayout gbl_panelCenter = new GridBagLayout();
		gbl_panelCenter.columnWidths = new int[] {30, 0, 15, 0, 30};
		gbl_panelCenter.rowHeights = new int[] {30};
		gbl_panelCenter.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		centerContentPanel.setLayout(gbl_panelCenter);
        
        JScrollPane scrollPane = new JScrollPane(centerContentPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        return scrollPane;
	}
	
	private void addLabel(String text) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = this.gridy;
		this.gridy++;
		
		JLabel lbl = new JLabel(text);
		lbl.setFont(this.getFont());
		lbl.setForeground(foregroundColor);
		centerContentPanel.add(lbl, gbc);
	}

	private void addOption(String text, Component component) {
		this.addOption(text, component, true);
	}
	
	private void addOption(String text, Component component, Boolean fill) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = this.gridy;
		this.gridy++;
		
		JLabel lbl = new JLabel(text);
		lbl.setFont(this.getFont());
		lbl.setForeground(foregroundColor);
		centerContentPanel.add(lbl, gbc);

		if (fill) gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		component.setFont(this.getFont());
		
		centerContentPanel.add(component, gbc);
	}
	
	public String getTextFieldPathToUpdates() {
		return this.textFieldPathToUpdates.getText();
	}
	
	public void setTextFieldPathToUpdates(String path) {
		if (path != null && path.length() > 0) {
			this.textFieldPathToUpdates.setText(path);
		}
	}
	
	public String getTextFieldPathToFibUpdateFiles() {
		return this.textFieldPathToFibUpdateFiles.getText();
	}

	public void setTextFieldPathToFibUpdateFiles(String path) {
		if (path != null && path.length() > 0) {
			this.textFieldPathToFibUpdateFiles.setText(path);
		}
	}
	
	public String getTextFieldPathToFibReleaseFiles() {
		return this.textFieldPathToFibReleaseFiles.getText();
	}
	
	public void setTextFieldPathToFibReleaseFiles(String path) {
		if (path != null && path.length() > 0) {
			this.textFieldPathToFibReleaseFiles.setText(path);
		}
	}
	
	public String getTextFieldPathToAssFiles() {
		return this.textFieldPathToAssFiles.getText();
	}
	
	public void setTextFieldPathToAssFiles(String path) {
		if (path != null && path.length() > 0) {
			this.textFieldPathToAssFiles.setText(path);
		}
	}
	
	public String getTextFieldPathToJamFiles() {
		return this.textFieldPathToJamFiles.getText();
	}
	
	public void setTextFieldPathToJamFiles(String path) {
		if (path != null && path.length() > 0) {
			this.textFieldPathToJamFiles.setText(path);
		}
	}
	
	
}
