package src;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import gui.SplitPastePanel;

public class RowPair {
	public JTextField left;
	public JTextField right;
	private boolean isEmpty;

	@SuppressWarnings("serial")
	public RowPair(int rowIndex, Font font, SplitPastePanel splitPastePanel) {
		isEmpty = true;
		left = new JTextField() {
			@Override
			public void paste() {
				splitPastePanel.handlePasteFromClipboard(rowIndex);
			}
		};
		left.setFont(font);
		left.setColumns(12);

		right = new JTextField();
		right.setFont(font);
		
		left.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (isEmpty && left.getText().length() > 0) {
					isEmpty = false;
					splitPastePanel.emptyChange();
				} else if (!isEmpty && left.getText().length() <= 0) {
					isEmpty = true;
					splitPastePanel.emptyChange();
				}
			}
		});
	}
}
