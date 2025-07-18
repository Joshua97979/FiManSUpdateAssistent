package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.Update;

@SuppressWarnings("serial")
public class ListItemPanel extends JPanel{

	public ArrayList<JCheckBox> checkBoxes;
	public boolean isSelected;
	private HoverMouseListener mouseListener;
	private Component[] highlightableComponents;
	private Color originalColor;
	private Color highlightColor;
	private Color selectedColor;
	private Update update;
	private View view;
	private Font font;
	private JPanel innerPanel;
	private JPanel innerPanelRight;
	private JPanel innerPanelLeft;
	private int preferredHeight;
	
	public int descriptionVerticalScrollPos;
	
	public static Color darkenColor(Color color, double factor) {
        int red = (int) (color.getRed() * (1 - factor));
        int green = (int) (color.getGreen() * (1 - factor));
        int blue = (int) (color.getBlue() * (1 - factor));

        red = Math.max(0, red);
        green = Math.max(0, green);
        blue = Math.max(0, blue);

        return new Color(red, green, blue);
    }
	
	private void setHighlightColor() {
		if (View.isColorDark(originalColor)) {
			this.highlightColor = originalColor.brighter();
		} else {
			this.highlightColor = darkenColor(originalColor, 0.1);
		}
	}
	
	public void setSelectedColor() {
		if (View.isColorDark(originalColor)) {
			this.selectedColor = originalColor.brighter().brighter();
		} else {
			this.selectedColor = darkenColor(originalColor, 0.2);
		}
	}
	
	public ListItemPanel(Update update, View view) {
		this(update, update.toString(), view, view.getBackground(), view.getForeground());
	}
	
	public ListItemPanel(Update update, String updStr, View view) {
		this(update, updStr, view, view.getBackground(), view.getForeground());
	}
	
	public ListItemPanel(Update update, String updStr, View view, Color backgroundColor, Color foregroundColor) {
		this.font = view.getFont();
		this.update = update;
		this.view = view;
		this.isSelected = false;
		this.checkBoxes = new ArrayList<JCheckBox>();
		this.originalColor = backgroundColor;
		this.setBackground(backgroundColor);
		setHighlightColor();
		setSelectedColor();
		
		JComboBox<String> tmpBox = new JComboBox<String>();
		tmpBox.setFont(font);
		int size = (int) Math.round(tmpBox.getPreferredSize().getHeight());
		size *= 1.8;
		tmpBox = null; //just to make sure
		
		preferredHeight = size;
		
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        this.setPreferredSize(new Dimension(0, preferredHeight)); //Width is overridden from outside in setInnerPanelPreferredSize().
        
        innerPanel = new JPanel(new BorderLayout());
        innerPanel.setPreferredSize(new Dimension(0, preferredHeight)); //Width is overridden from outside in setInnerPanelPreferredSize().
        innerPanel.setOpaque(true);
        innerPanel.setBackground(new Color(0,0,0,0));
        
        // Panel links
        innerPanelLeft = new JPanel();
        innerPanelLeft.setOpaque(true);
        innerPanelLeft.setBackground(new Color(0,0,0,0));
        innerPanel.add(innerPanelLeft, BorderLayout.WEST);

        JLabel label = new JLabel(updStr);
		label.setFont(font);
		label.setForeground(foregroundColor);
		innerPanelLeft.add(label);
        
        
        // Panel rechts
        innerPanelRight = new JPanel();
        innerPanelRight.setOpaque(true);
        innerPanelRight.setBackground(new Color(0,0,0,0));
        innerPanel.add(innerPanelRight, BorderLayout.EAST);

        JCheckBox classicChckbx = null;
		if (update.getType().equals("ASS") || update.getType().equals("FIB")) {
			JLabel classicLabel = new JLabel("Classic Teil");
			classicLabel.setFont(font);
			classicLabel.setForeground(foregroundColor);
			innerPanelRight.add(classicLabel);
			
			classicChckbx = new JCheckBox("");
			classicChckbx.setBackground(backgroundColor);
			classicChckbx.setBorder(null);
			innerPanelRight.add(classicChckbx);
			checkBoxes.add(classicChckbx);
		}
		
		JLabel graphicLabel = new JLabel("  |  Grafik Teil");
		graphicLabel.setFont(font);
		graphicLabel.setForeground(foregroundColor);
		innerPanelRight.add(graphicLabel);
		JCheckBox graphicChckbx = new JCheckBox("");
		graphicChckbx.setBackground(backgroundColor);
		graphicChckbx.setBorder(null);
		innerPanelRight.add(graphicChckbx);
		checkBoxes.add(graphicChckbx);

		
		this.add(innerPanel, BorderLayout.CENTER);
		
		mouseListener = new HoverMouseListener(this);
		this.addMouseListener(mouseListener);
		
		addKeyAdapter();
		
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, (font.getSize() * 60)));
		
		highlightableComponents = new Component[] {this, classicChckbx, graphicChckbx};
	}
	
	private void addKeyAdapter() {
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 40) {
					if (e.isControlDown()) {
						view.goControlDown();
					} else {
						view.goDown();
					}
				} else if (e.getKeyCode() == 38) {
					if (e.isControlDown()) {
						view.goControlUp();
					} else {
						view.goUp();
					}
				}
			}
		});
	}

	public Update getUpdate() {
		return this.update;
	}
	
	public void removeSelected() {
		this.isSelected = false;
		setColor(originalColor);
	}
	
	public void makeSelected() {
		this.isSelected = true;
		this.setColor(selectedColor);
	}
	
	public void highlightMe() {
		this.setColor(highlightColor);
	}
	
	public void removeHighlightOnMe() {
		this.setColor(originalColor);
	}
	
	public void setColor(Color color) {
		for (int i = 0; i < highlightableComponents.length; i++) {
        	if (highlightableComponents[i] != null) {
        		highlightableComponents[i].setBackground(color);
        	}
		}
	}
	
	public void rightButtonPressed(MouseEvent e) {
		view.rightButtonPressed(e, this);
	}
	
	public void leftButtonPressed() {
		view.leftButtonPressed(this);
	}
	
	public void shiftLeftButtonPressed() {
		view.shiftLeftButtonPressed(this);
	}
	
	public void ctrlLeftButtonPressed() {
		view.ctrlLeftButtonPressed(this);
	}
	
	public Dimension getInnerInnerPanelPreferredSize() {
		return this.innerPanel.getPreferredSize();
	}
	
	public void setInnerPanelPreferredWidth(int preferredWidth) {	
		this.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
		this.innerPanel.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
	}

	static class HoverMouseListener extends MouseAdapter {
		private ListItemPanel item;
        
        HoverMouseListener(ListItemPanel item) {
        	this.item = item;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        	if (item.isSelected) return;
        	item.highlightMe();
        }

        @Override
        public void mouseExited(MouseEvent e) {
        	if (item.isSelected) return;
            item.removeHighlightOnMe();
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
        	if (e.getButton() == MouseEvent.BUTTON1 && !e.isShiftDown() && !e.isControlDown()) {
        		leftButtonPressed();
        	} else if (e.getButton() == MouseEvent.BUTTON3) {
        		rightButtonPressed(e);
        	} else if (e.getButton() == MouseEvent.BUTTON1 && e.isShiftDown()) {
        		shiftLeftButtonPressed();
        	} else if (e.getButton() == MouseEvent.BUTTON1 && e.isControlDown()) {
        		ctrlLeftButtonPressed();
			}
        	
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
        	if (e.getButton() == MouseEvent.BUTTON3) {
        		rightButtonPressed(e);
        	}
        	
        }
        
        private void leftButtonPressed() {
        	item.leftButtonPressed();
        }
        
        private void rightButtonPressed(MouseEvent e) {
        	item.rightButtonPressed(e);
        }
        
        private void shiftLeftButtonPressed() {
        	item.shiftLeftButtonPressed();
        }
        
        private void ctrlLeftButtonPressed() {
        	item.ctrlLeftButtonPressed();
        }
    }

	public Color getSelectedColor() {
		return this.selectedColor;
	}

	public int getMinPanelSize() {
		return innerPanelLeft.getWidth() + innerPanelRight.getWidth();
	}
}
