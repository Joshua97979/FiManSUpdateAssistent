package gui;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import src.RowPair;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class ModListPanel extends JPanel {
	
	private Color backgroundColor;
	private Color secondaryBackgroundColor;
	private Color foregroundColor;
	private Color secondaryForegroundColor;
	private SplitPastePanel splitPastePanel;
	
	public JButton btnConfirm;
	public JButton btnCancel;
	
	private JPanel panelWest;
	private JPanel panelEast;
	
	private String fibModBib;
	private JButton btnNonEmptyLefts;
	
	private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	public ModListPanel(Font font, String fibModBib, List<RowPair> modInput, Color backgroundColor, Color secondaryBackgroundColor, Color foregroundColor, Color secondaryForegroundColor) {
		this.setFont(font);
		this.fibModBib = fibModBib;
		this.backgroundColor = backgroundColor;
		this.secondaryBackgroundColor = secondaryBackgroundColor;
		this.foregroundColor = foregroundColor;
		this.secondaryForegroundColor = secondaryForegroundColor;
		
		createGui();
		if (modInput.size() > 0) {
			this.setList(modInput);
		}
	}
	
	private void createGui() {
		this.setLayout(new BorderLayout(0, 0));
		splitPastePanel = new SplitPastePanel(this.getFont(), this, backgroundColor, secondaryBackgroundColor, foregroundColor, secondaryForegroundColor);
		this.add(splitPastePanel, BorderLayout.CENTER);
		this.add(getToolBarPanel(), BorderLayout.SOUTH);
		this.add(getNorthPanel(), BorderLayout.NORTH);
	}
	
	private JPanel getToolBarPanel() {
		JPanel panelToolBar = new JPanel(new BorderLayout());
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(backgroundColor);
		panelToolBar.add(panelCenter, BorderLayout.CENTER);
		
		this.panelEast = new JPanel();
		panelEast.setBackground(backgroundColor);
		panelToolBar.add(panelEast, BorderLayout.EAST);
		
		//Cancel-Button
		btnCancel = new JButton("Abbruch");
		btnCancel.setFont(this.getFont());
		btnCancel.setBackground(backgroundColor);
		btnCancel.setFocusable(false);
		btnCancel.setToolTipText("Eingabe abbrechen");
		panelCenter.add(btnCancel);

		//AddRows-Button
		JButton btnAddRows = new JButton("+8 Reihen");
		btnAddRows.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				splitPastePanel.addRows(8);
			}
		});
		btnAddRows.setFont(this.getFont());
		btnAddRows.setBackground(backgroundColor);
		btnAddRows.setFocusable(false);
		btnAddRows.setToolTipText("8 weitere Reihen hinzufügen");
		panelCenter.add(btnAddRows);
		
		//GetSQL-Button
		JButton btnGetSQL = new JButton("SQL");
		btnGetSQL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fibModBib.length() <= 0) {
					String message = "Es wurde noch keine Modifikationsbibliothek eingetragen!";
					JOptionPane.showMessageDialog(splitPastePanel, message);
				} else {
					String sql = "SELECT SYSTEM_TABLE_MEMBER AS filename,\n" +
							"CAST(PARTITION_TEXT AS VARCHAR(2000) CCSID 1141) AS text\n" +
							"FROM QSYS2.SYSPARTITIONSTAT WHERE TABLE_SCHEMA = '" + fibModBib + "' AND\n" +
							"TABLE_NAME IN ('QRPGSRC', 'QDDSSRC', 'QDDXSRC', 'QCLSRC') AND \n" +
							"SYSTEM_TABLE_MEMBER NOT LIKE 'FT%' AND SYSTEM_TABLE_MEMBER LIKE 'F%'";
					StringSelection stringSelection = new StringSelection(sql);
					clipboard.setContents(stringSelection, null);
				}
			}
		});
		btnGetSQL.setFont(this.getFont());
		btnGetSQL.setBackground(backgroundColor);
		btnGetSQL.setFocusable(false);
		btnGetSQL.setToolTipText("SQL zum auflisten der angepassten Kundenquellen in zwischenablage kopieren");
		btnGetSQL.setPreferredSize(btnAddRows.getPreferredSize());
		panelCenter.add(btnGetSQL);
		
		//Confirm-Button
		btnConfirm = new JButton("Weiter");
		btnConfirm.setFont(this.getFont());
		btnConfirm.setBackground(backgroundColor);
		btnConfirm.setFocusable(false);
		btnConfirm.setToolTipText(
				"<html>Verarbeitet die hier eingegebenen, beim Kunden angepassten Quellen<br>"
				+ "und erstellt anschließend eine Liste der bei der Update-Installation zu prüfenden Quellen.</html>");
		btnConfirm.setPreferredSize(btnAddRows.getPreferredSize());
		panelCenter.add(btnConfirm);
		
		
		
		//NonEmptyLefts-Button
		btnNonEmptyLefts = new JButton("Anzahl: 0");
		btnNonEmptyLefts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fibModBib.length() <= 0) {
					String message = "Es wurde noch keine Modifikationsbibliothek eingetragen!";
					JOptionPane.showMessageDialog(splitPastePanel, message);
				} else {
					String sql = "SELECT COUNT(SYSTEM_TABLE_MEMBER)\n" + 
							"FROM QSYS2.SYSPARTITIONSTAT WHERE TABLE_SCHEMA = '" + fibModBib + "' AND\n" +
							"TABLE_NAME IN ('QRPGSRC', 'QDDSSRC', 'QDDXSRC', 'QCLSRC') AND \n" +
							"SYSTEM_TABLE_MEMBER NOT LIKE 'FT%' AND SYSTEM_TABLE_MEMBER LIKE 'F%'";
					StringSelection stringSelection = new StringSelection(sql);
					clipboard.setContents(stringSelection, null);
				}
			}
		});
		btnNonEmptyLefts.setFont(this.getFont());
		btnNonEmptyLefts.setBackground(backgroundColor);
		btnNonEmptyLefts.setFocusable(false);
		btnNonEmptyLefts.setToolTipText("Anzahl eingegebener Quellen und SQL für Anzahl der angepassten Kundenquellen in zwischenablage kopieren");
		panelEast.add(btnNonEmptyLefts);
		
		
		//panelWest
		this.panelWest = new JPanel();
		panelWest.setBackground(backgroundColor);
		panelWest.setPreferredSize(panelEast.getPreferredSize());
		panelToolBar.add(panelWest, BorderLayout.WEST);
		
		
		
		btnCancel.setPreferredSize(btnAddRows.getPreferredSize());
		
		return panelToolBar;
	}
	
	private JPanel getNorthPanel() {
		JPanel panelNorth = new JPanel();
		Font boldFont = this.getFont().deriveFont(Font.BOLD);
		JLabel lblTitel = new JLabel("Angepasste Kundenquellen eingeben");
		lblTitel.setFont(boldFont);
		panelNorth.add(lblTitel);
		return panelNorth;
	}

	public List<RowPair> getList() {
		return splitPastePanel.getRows();
	}
	
	private void setList(List<RowPair> modInput) {
		splitPastePanel.setRows(modInput);
	}

	public void setNonEmptyLefts(String text) {
		this.btnNonEmptyLefts.setText(text);
		this.panelWest.setPreferredSize(this.panelEast.getPreferredSize());
	}
}
