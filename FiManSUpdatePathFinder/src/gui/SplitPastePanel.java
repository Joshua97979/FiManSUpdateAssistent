package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import src.RowPair;

@SuppressWarnings("serial")
public class SplitPastePanel extends JPanel{
	private static final int INITIAL_ROWS = 16;
	
	private final JPanel gridPanel;
	private int rowCount = 0;
	private GridBagConstraints gbc;
	private Font font;
	
	private List<RowPair> rows;
	
	private ModListPanel modListPanel;
	
	public SplitPastePanel(Font font, ModListPanel modListPanel, Color backgroundColor, Color secondaryBackgroundColor, Color foregroundColor, Color secondaryForegroundColor) {
		this.rows = new ArrayList<>();
		this.font = font;
		this.setFont(font);
	
		this.modListPanel = modListPanel;
		
		this.setLayout(new BorderLayout());
        
		this.gridPanel = new JPanel(new GridBagLayout());
		this.gridPanel.setBackground(backgroundColor);
        
        //Wrapper-Panel for top-alignment in ScrollPane
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(backgroundColor);
        wrapper.add(gridPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        this.add(scrollPane, BorderLayout.CENTER);
        
        this.gbc = new GridBagConstraints();
        this.gbc.fill = GridBagConstraints.HORIZONTAL;
        
        this.addRows(INITIAL_ROWS);
        SwingUtilities.invokeLater(() -> {
        	rows.get(0).left.requestFocusInWindow();
		});
        
        InputMap im = gridPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = gridPanel.getActionMap();

        im.put(KeyStroke.getKeyStroke("PAGE_DOWN"), "addRowsPageDown");

        am.put("addRowsPageDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRows(8);
            }
        });
	}

	public void addRows(int numberOfRows) {
		for (int i = 0; i < numberOfRows; i++, rowCount++) {
			RowPair rowPair = new RowPair(rowCount, font, this);
			
			//left Textfeld
			this.gbc.gridx = 0;
			this.gbc.gridy = rowCount;
			this.gbc.weightx = 0;
			this.gridPanel.add(rowPair.left, gbc);

            //right Textfeld
			this.gbc.gridx = 1;
			this.gbc.gridy = rowCount;
			this.gbc.weightx = 1;
			this.gridPanel.add(rowPair.right, gbc);
            
			this.rows.add(rowPair);
		}
		
		revalidate();
        repaint();
	}
	
	public void emptyChange() {
		this.modListPanel.setNonEmptyLefts("Anzahl: " + countNonEmptyLefts());
	}
	
	private int countNonEmptyLefts() {
		int count = 0;
		for (RowPair row : rows) {
			if (!row.left.getText().isEmpty()) {
				count++;
			}
		}
		return count;
	}
	
	public void handlePasteFromClipboard(int startRow) {
		String data = readClipboardString();
        if (data == null || data.isEmpty()) return;

        String[] lines = normalizeAndSplitLines(data);

        applyLines(startRow, lines);

        focusAfterPaste(startRow, lines);
        emptyChange();
	}
	
	private String readClipboardString() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String content = "";
		try {
			Transferable transferable = clipboard.getContents(null);
			if (transferable == null) return null;
			if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor) == false) return null;
			content = (String) transferable.getTransferData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException e) {}
		return content;
    }
	
	private String[] normalizeAndSplitLines(String string) {
		String normalized = string.replace("\r\n", "\n").replace("\r", "\n");
		return normalized.split("\n");
    }

	private void applyLines(int startRow, String[] lines) {
		int currentRow = startRow;
		for (String line : lines) {
			ensureRowsUpTo(currentRow);
			applyLineToRow(currentRow, line);
			currentRow++;
		}
		scrollRowToVisible(currentRow - 1);
	}
	
	private void ensureRowsUpTo(int rowIndex) {
		if (rowIndex < rows.size()) return; //Is the current rowIndex less than the table size => return
		int needed = rowIndex - rows.size() + 1; //"needed" is theoretically never greater than 1 if only called from applyLines()
		if (needed > 0) addRows(needed);
    }
	
	private void applyLineToRow(int currentRow, String line) {
		if (currentRow < 0 || currentRow >= rows.size()) return;
		
		int space = line.indexOf(' ');
		if (space >= 0) {
			rows.get(currentRow).left.setText(line.substring(0, space));
			rows.get(currentRow).right.setText(line.substring(space + 1).trim());
		} else {
			rows.get(currentRow).left.setText(line);
			// right remains unchanged
		}
	}
	
	//Set the focus appropriately after pasting:
	//If the last processed line contains a 'right' -> focus on that 'right'
	//Otherwise, focus on the next 'left' (if present) or the last 'left'.
	private void focusAfterPaste(int startRow, String[] lines) {
		int lastProcessed = startRow + lines.length - 1;

		String lastLine = lines[Math.max(0, lines.length - 1)];
		boolean lastHadRight = lastLine.indexOf(' ') >= 0;

		if (lastHadRight) {
			rows.get(lastProcessed).right.requestFocusInWindow();
		} else {
			int next = startRow + lines.length;
			if (next < rows.size()) rows.get(next).left.requestFocusInWindow();
			else rows.get(lastProcessed).left.requestFocusInWindow();
		}
	}
	
	private void scrollRowToVisible(int rowIndex) {
		if (rowIndex < 0 || rowIndex >= rows.size()) return;

		JTextField field = rows.get(rowIndex).left;
		
		SwingUtilities.invokeLater(() -> {
			Rectangle bounds = field.getBounds();
			gridPanel.scrollRectToVisible(bounds);
		});
	}
	
	public List<RowPair> getRows() {
		return this.rows;
	}

	public void setRows(List<RowPair> newRows) {
		if (newRows == null) return;
		
		this.gridPanel.removeAll();
		this.rows.clear();
		this.rowCount = 0;
		
		addRows(newRows.size());
		
		for (int i = 0; i < newRows.size(); i++) {
			RowPair newPair = newRows.get(i);
			if (newPair.left != null) {
				this.rows.get(i).left.setText(newPair.left.getText());
			}
			if (newPair.right != null) {
				this.rows.get(i).right.setText(newPair.right.getText());
			}
		}
		emptyChange();
	}
}
