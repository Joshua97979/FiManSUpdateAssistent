package gui;
//By Joshua Froitzheim 2023
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import src.Config;
import src.Update;

@SuppressWarnings("serial")
public class DescriptionPanel extends JPanel{
	
	private Clipboard clipboard;
	private GridBagLayout gbl_layout;
	private GridBagConstraints gbc_constraints;
	private int[] spaces;
	private URL copy_Icon_url;
	private URL folder_Icon_url;
	private Font font;
	private Color backgroundColor;
	private Color foregroundColor;
	private JPanel contentPanel;
	private ImageIcon copy_Icon;
	private ImageIcon folder_Icon;
	
	protected JTextField fibPgmBibTextField;
	protected JTextField fibDtaBibTextField;
	protected JTextField fibSrcBibTextField;
	protected JTextField assDtaBibTextField;
	protected JTextField assPgmBibTextField;
	protected JTextField optNameTextField;
	protected JTextField imgClgNameTextField;
	protected JTextField imgClgPathTextField;
	protected JTextField jdkTextField;
	protected JTextField osTextField;
	protected JTextField tomCatDownTextField;
	protected JTextField tomCatUpTextField;
	protected JTextField tomCatVersionTextField;
	protected JTextField aesPgmBibTextField;
	protected JTextField aesDtaBibTextField;
	protected JTextField tomCatJobNameTextField;
	protected JTextField fibInsDirPathTextField;
	protected JTextField tomCatInsDirPathTextField;
	protected JTextField fibModBibTextField;
	
	private int gridy;
	private int[] numbering;
	
	private JScrollPane scrollPane;
	
	private int size;
	private int imageSize;
	
	class MyDocumentListener implements DocumentListener {
		
		private Object[] inputs;
		private Object output;
		private boolean quote;
		private boolean spaceBefore;
		private boolean spaceAfter;
		private boolean hideIfEmpty;
		private JButton copyButton;
		
		public MyDocumentListener(Object[] inputs, JLabel output, boolean quote, boolean spaceBefore, boolean spaceAfter, boolean hideIfEmpty) throws Exception {this(inputs, ((Object) output), quote, spaceBefore, spaceAfter, hideIfEmpty);}
		public MyDocumentListener(Object[] inputs, JTextField output, boolean quote, boolean spaceBefore, boolean spaceAfter, boolean hideIfEmpty) throws Exception {this(inputs, ((Object) output), quote, spaceBefore, spaceAfter, hideIfEmpty);}
		private MyDocumentListener(Object[] inputs, Object output, boolean quote, boolean spaceBefore, boolean spaceAfter, boolean hideIfEmpty) throws Exception {this(inputs, ((Object) output), quote, spaceBefore, spaceAfter, hideIfEmpty, null);}
		private MyDocumentListener(Object[] inputs, Object output, boolean quote, boolean spaceBefore, boolean spaceAfter, boolean hideIfEmpty, JButton button) throws Exception {
			this.output = output;
			this.inputs = inputs;
			this.quote = quote;
			this.spaceBefore = spaceBefore;
			this.spaceAfter = spaceAfter;
			this.hideIfEmpty = hideIfEmpty;
			this.copyButton = button;
		}
		
		private boolean allInputsEmpty(Object[] inputs) {
			for (Object obj : inputs) {
				if (obj instanceof JTextField) {
					if (((JTextField) obj).getText().length() > 0) {
						return false;
					}
				}
			}
			return true;
		}
		
		private String getUpdatedText(Object[] inputs) {
			String displayedText = "";
			for (Object obj : inputs) {
		    	if (obj instanceof String) {
		    		displayedText += (String)obj;
				} else if (obj instanceof JTextField) {
					if (((JTextField) obj).getText().length() > 0) {
						if (spaceBefore) {displayedText += " ";}
						if (quote) {
							displayedText += "\"" + ((JTextField) obj).getText() + "\"";
						} else {
							displayedText += ((JTextField) obj).getText();
						}
						if (spaceAfter) {displayedText += " ";}
					}
				}
		    }
			return displayedText;
		}
		
		@Override
		public void removeUpdate(DocumentEvent e) {warn();}
		@Override
		public void insertUpdate(DocumentEvent e) {warn();}
		@Override
		public void changedUpdate(DocumentEvent e) {warn();}
		private void warn() {
			String displayedText = "";
			if(hideIfEmpty) {
				if (allInputsEmpty(inputs) == false) {
					displayedText = getUpdatedText(inputs);
					if (copyButton != null) {
						copyButton.setVisible(true);
					}
				} else {
					displayedText = "";
					if (copyButton != null) {
						copyButton.setVisible(false);
					}
				}
			} else {
				displayedText = getUpdatedText(inputs);
			}
			
			try {
				((JLabel)output).setText(displayedText);
			} catch (ClassCastException e) {
				((JTextField)output).setText(displayedText);
			} 
			
		}
	};
	
	private void resizeScrollMax() {
		if (this.scrollPane != null) {
			this.scrollPane.getVerticalScrollBar().setMaximum((int)this.getPreferredSize().getHeight());
		}
	}
	
	public DescriptionPanel(Update update ,Font font, Config configFile) {
		this.font = font;
		this.backgroundColor = configFile.getBackgroundColor();
		this.foregroundColor = configFile.getForegroundColor();
		this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		this.copy_Icon_url = getClass().getResource("/icons/Copy_Icon.png");
		this.folder_Icon_url = getClass().getResource("/icons/Folder_open.png");
		
		JComboBox<String> tmpBox = new JComboBox<String>();
		tmpBox.setFont(font);
		this.size = (int) Math.round(tmpBox.getPreferredSize().getHeight());
		this.imageSize = (int) (size * 0.7);
		tmpBox = null; //just to make sure
		
		Image newImage = new ImageIcon(copy_Icon_url).getImage();
		Image scaledImage = newImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
		this.copy_Icon = new ImageIcon(scaledImage);
		
		newImage = new ImageIcon(folder_Icon_url).getImage();
		scaledImage = newImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
		this.folder_Icon = new ImageIcon(scaledImage);
		
		
		this.fibPgmBibTextField = new JTextField();
		this.fibDtaBibTextField = new JTextField();
		this.fibSrcBibTextField = new JTextField();
		this.assDtaBibTextField = new JTextField();
		this.assPgmBibTextField = new JTextField();
		this.optNameTextField = new JTextField();
		this.imgClgNameTextField = new JTextField();
		this.imgClgPathTextField = new JTextField();
		this.jdkTextField = new JTextField();
		this.osTextField = new JTextField();
		this.tomCatDownTextField = new JTextField();
		this.tomCatUpTextField = new JTextField();
		this.tomCatVersionTextField = new JTextField();
		this.aesPgmBibTextField = new JTextField();
		this.aesDtaBibTextField = new JTextField();
		this.tomCatJobNameTextField = new JTextField();
		this.fibInsDirPathTextField = new JTextField();
		this.tomCatInsDirPathTextField = new JTextField();
		this.fibModBibTextField = new JTextField();
		
		this.gridy = 0;
		this.numbering = new int[]{1,0,0};
		
		this.setLayout(new BorderLayout(0, 0));
		
		this.add(new SearchBar(font, this), BorderLayout.NORTH);

		scrollPane = new JScrollPane();
    	scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    	
    	this.add(scrollPane, BorderLayout.CENTER);
    	contentPanel = new JPanel();
    	scrollPane.setViewportView(contentPanel); 
		
    	contentPanel.setBackground(backgroundColor);
		
    	gbl_layout = new GridBagLayout();
    	gbl_layout.columnWidths = new int[]{22, 464, 0};
    	gbl_layout.rowHeights = spaces;
    	gbl_layout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
    	contentPanel.setLayout(gbl_layout);
    	contentPanel.setMinimumSize(new Dimension(0,0));
    	
    	gbc_constraints = new GridBagConstraints();
    	gbc_constraints.anchor = GridBagConstraints.WEST;
    	gbc_constraints.insets = new Insets(0, 0, 5, 0);
    	gbc_constraints.fill = GridBagConstraints.VERTICAL;
    	gbc_constraints.gridx = 1;
	}
	
	public void jumpToLabel(JLabel targetLabel) {
		SwingUtilities.invokeLater(() -> {
			Rectangle bounds = targetLabel.getBounds();
			
			if (targetLabel.getParent().getParent() instanceof JPanel) {
				bounds = targetLabel.getParent().getBounds();
			}
		    
		    contentPanel.scrollRectToVisible(bounds);
		});
	}
	
	
	public static void highlightLabel(JLabel label, String wordToHighlight, Color color) {
        if (label == null || wordToHighlight == null || wordToHighlight.isEmpty()) {
            return;
        }

        String originalText = label.getText();
        String colorColde = ("#"+Integer.toHexString(color.getRGB()).substring(2));
        
        // Remove existing HTML tags if present
        if (originalText.toLowerCase().startsWith("<html>")) {
            originalText = originalText.replaceAll("(?i)</?html>", "");
        }

        // Prepare regex for case-insensitive matching
        Pattern pattern = Pattern.compile("(?i)" + Pattern.quote(wordToHighlight));
        Matcher matcher = pattern.matcher(originalText);

        // Highlight matches
        String highlightedText = matcher.replaceAll(
        		"<span style='background-color:" + colorColde + "; color:black;'>$0</span>"
        );

        // Set the new HTML-formatted text
        label.setText("<html>" + highlightedText + "</html>");
    }
	public static void removeHighlights(JLabel label) {
		removeHighlights(new JLabel[] { label });
	}
	
	public static void removeHighlights(JLabel[] labels) {
	    if (labels == null) return;

	    for (JLabel label : labels) {
	        if (label == null) continue;

	        String text = label.getText();
	        if (text == null) continue;

	        // Falls Text HTML enthält
	        if (text.toLowerCase().startsWith("<html>")) {
	            text = text.replaceAll("(?i)</?html>", ""); // Entferne <html> und </html>
	        }

	        // Entferne Hervorhebung
	        text = text.replaceAll("(?i)</?b>", ""); // Fett entfernen
	        text = text.replaceAll("(?i)<span style='[^']*'>(.*?)</span>", "$1"); // Span-Tag entfernen, Inhalt behalten

	        label.setText(text);
	    }
	}
	
	public int getMaximumScroll() {
		if (this.scrollPane == null) return 0;
		return this.scrollPane.getVerticalScrollBar().getMaximum();
	}
	
	public int getVerticalScrollPos() {
		if (this.scrollPane == null) return 0;
		return this.scrollPane.getVerticalScrollBar().getValue();
	}
	
	public void setVerticalScrollPos(int newPos) {
		if (this.scrollPane == null) return;
		this.scrollPane.getVerticalScrollBar().setValue(newPos);
	}
	
	public void addSpace(int size) {
		if (spaces == null) {
			spaces = new int[1];
		}
		if (spaces.length <= gridy) {
			int[] newSpcaes = new int[gridy+1];
			for (int i = 0; i < newSpcaes.length; i++) {
				newSpcaes[i] = 0;
			}
			
			for (int i = 0; i < spaces.length; i++) {
				newSpcaes[i] = spaces[i];
			}
			spaces = newSpcaes;
		}
		
		spaces[gridy] = size;
		gbl_layout.rowHeights = spaces;
		
		gridy++;
		resizeScrollMax();
	}

	public void addImage(String path) {
		try {
			JLabel newLabel = new JLabel(new ImageIcon(getClass().getResource(path)));
			gbc_constraints.gridy = gridy;
			contentPanel.add(newLabel, gbc_constraints);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		gridy++;
		resizeScrollMax(); 
	}

	public JTextField addBibField(String commandToCopy) {
		JTextField newTextField = new JTextField();
		
		JPanel newPanel = new JPanel();
		newPanel.setBackground(backgroundColor);
    	
		gbc_constraints.gridy = gridy;
		contentPanel.add(newPanel, gbc_constraints);
    	
		ScaledButton newCopyButton = new ScaledButton(copy_Icon, size);
    	newCopyButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			
    			StringSelection stringSelection;
    			
    			if (newTextField.getText().equals("")) {
    				stringSelection = new StringSelection(commandToCopy);
				} else {
					stringSelection = new StringSelection(newTextField.getText());
				}
    			clipboard.setContents(stringSelection, null);
    		}
    	});
    	newCopyButton.setBackground(backgroundColor);
    	newPanel.add(newCopyButton);
    	
    	newTextField.setFont(font);
    	newPanel.add(newTextField);
    	newTextField.setColumns(20);
    	
    	gridy++;
    	resizeScrollMax();
    	return newTextField;
	}

	public void addBoldLabel(String text) {
		Font currentFont = this.font;
		Font newFont = currentFont.deriveFont(Font.BOLD);
		this.addLabel(text, newFont);
	}
	public void addLabel(String text) {
		this.addLabel(new Object[] {text}, this.font);
	}
	public void addLabel(String text, Font font) {
		this.addLabel(new Object[] {text}, font);
	}
	public void addLabel(Object[] objects) {
		this.addLabel(objects, this.font);
	}
	public void addLabel(Object[] inputs, Font font) {
		this.addLabel(inputs, font, this.foregroundColor);
	}
	public void addLabel(Object[] inputs, Font font, Color color) {
		
		String displayedText = "";
    	for (Object obj : inputs) {
	    	if (obj instanceof String) {
	    		displayedText += (String)obj;
			}
	    }
    	
    	if (displayedText.contains("\n")) {
			displayedText = "<html>" + displayedText + "</html>";
			displayedText = displayedText.replace("\n", "<br>");
		}
    	
    	JLabel newLabel = new JLabel(displayedText);
    	newLabel.setFont(font);
		newLabel.setForeground(color);
    	gbc_constraints.gridy = gridy;
    	contentPanel.add(newLabel, gbc_constraints);
    	newLabel.putClientProperty("gridy", gridy);
    	
    	for (Object textField : inputs) {
	    	if (textField instanceof JTextField) {
	    		try {
					((JTextField) textField).getDocument().addDocumentListener(new MyDocumentListener(inputs, newLabel, true, false, true, false));
				} catch (Exception e1) {e1.printStackTrace();}
			}
	    }
    	gridy++;
    	resizeScrollMax();
	}
	
	public void addTitel(String text) {
		Font currentFont = this.font;
		Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.6F);
		newFont = newFont.deriveFont(Font.BOLD);
		addLabel(text, newFont);
	}

	public void addOpenFolderButtonAndCopyableText(String displayedText, String folderPath) {
		this.addButtonAndCopyableText(displayedText, folderPath, true);}
	public void addOpenFolderButtonAndCopyableText(Object[] displayedText, Object[] folderPath) {
		this.addButtonAndCopyableText(displayedText, folderPath, true);}
	public void addButtonAndCopyableText(String text) {
		addButtonAndCopyableText(text, text);}
	public void addButtonAndCopyableText(String displayedText, String copyableText, boolean isFolderButton) {
		this.addButtonAndCopyableText(new Object[] {displayedText}, new Object[] {copyableText}, isFolderButton);}
	public void addButtonAndCopyableText(String displayedText, String copyableText) {
		this.addButtonAndCopyableText(new Object[] {displayedText}, new Object[] {copyableText}, false);}
	public void addButtonAndCopyableText(Object[] text) {
		addButtonAndCopyableText(text, false);}
	public void addButtonAndCopyableText(Object[] text, boolean isFolderButton) {
		addButtonAndCopyableText(text, text, isFolderButton);}
	public void addButtonAndCopyableText(Object[] displayedText, Object[] copyableTextOrFolderPath) {
		addButtonAndCopyableText(displayedText, copyableTextOrFolderPath, false);}
	public void addButtonAndCopyableText(Object[] displayedText, Object[] copyableTextOrFolderPath, boolean isFolderButton) {
		addButtonAndCopyableText(displayedText, copyableTextOrFolderPath, isFolderButton, false);}
	public void addButtonAndCopyableText(Object[] inputs, Object[] copyableTextOrFolderPath, boolean isFolderButton, boolean hideIfEmpty) {
		JPanel newPanel = new JPanel();
		newPanel.setBackground(backgroundColor);
		newPanel.setBorder(null);
    	
		gbc_constraints.gridy = gridy;
		contentPanel.add(newPanel, gbc_constraints);
    	
		ScaledButton newButton = new ScaledButton(size);
		if (isFolderButton == false) {
			newButton.setIcon(copy_Icon);
	    	newButton.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			String copyableText = "";
	    			for (Object obj : copyableTextOrFolderPath) {
				    	if (obj instanceof String) {
				    		copyableText += (String)obj;
						} else if (obj instanceof JTextField) {
							copyableText += ((JTextField)obj).getText();
						}
				    }
	    			if (copyableText.length() <= 0) {
	    				copyableText = "copyableText is empty; Zugeordnetes TextFeld wahrscheinlich nicht gefüllt.";
					}
	    			StringSelection stringSelection = new StringSelection(copyableText);
	    			clipboard.setContents(stringSelection, null);
	    		}
	    	});
		} else if (isFolderButton) {
			newButton.setIcon(folder_Icon);
	    	newButton.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			String folderPathStr = "";
			    	for (Object obj : copyableTextOrFolderPath) {
				    	if (obj instanceof String) {
				    		folderPathStr += (String)obj;
						} else if (obj instanceof JTextField) {
							folderPathStr += ((JTextField)obj).getText();
						}
				    }
	    			try {
						Desktop.getDesktop().open(new File(folderPathStr));
					} catch (IllegalArgumentException e1) {
						JOptionPane.showMessageDialog(getRootPane(), "Keine Datei gefunden bei: " + folderPathStr);
					} catch (IOException e2) {
						e2.printStackTrace();
					}
	    		}
	    	});
		}
		newButton.setBackground(backgroundColor);
    	newPanel.add(newButton);
    	
    	String displayedTextStr = "";
    	if(hideIfEmpty == false) {
	    	for (Object obj : inputs) {
		    	if (obj instanceof String) {
		    		displayedTextStr += (String)obj;
				} else if (obj instanceof JTextField) {	
					displayedTextStr += ((JTextField)obj).getText(); //TODO necessary?
				}
		    }
    	} else {
    		newButton.setVisible(false);
    	}
    	
    	JTextField newTextField = new JTextField();
    	newTextField.setFont(font);
    	newTextField.setForeground(foregroundColor);
    	newTextField.setText(displayedTextStr);
    	newTextField.setEditable(false);
    	newTextField.setColumns(50);
    	newTextField.setBorder(null);
    	newTextField.setBackground((Color) null);
    	newTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    	newPanel.add(newTextField);
    	
    	for (Object textField : inputs) {
	    	if (textField instanceof JTextField) {
	    		try {
					((JTextField) textField).getDocument().addDocumentListener(new MyDocumentListener(inputs, newTextField, false, false, false, hideIfEmpty, newButton));
				} catch (Exception e1) {e1.printStackTrace();}
			}
	    }
    	
    	gridy++;
    	resizeScrollMax();
	}
	
	public void addCopyableText(String text) {
		addCopyableText(new Object[] {text});
	}
	
	public void addCopyableText(Object[] inputs) {
		addCopyableText(inputs, false);
	}

	public void addCopyableText(Object[] inputs, boolean hideIfEmpty) {
		
		String displayedText = "";
		if(hideIfEmpty == false) {
			for (Object obj : inputs) {
		    	if (obj instanceof String) {
		    		displayedText += (String)obj;
				}
		    }
		}

    	JTextField newTextField = new JTextField();
		newTextField.setFont(font);
		newTextField.setForeground(foregroundColor);
		newTextField.setText(displayedText);
		newTextField.setColumns(50);
		newTextField.setBackground(null);
		newTextField.setBorder(null);
		newTextField.setEditable(false);
		newTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    	gbc_constraints.gridy = gridy;
    	contentPanel.add(newTextField, gbc_constraints);
    	
    	for (Object textField : inputs) {
	    	if (textField instanceof JTextField) {
	    		try {	
					((JTextField) textField).getDocument().addDocumentListener(new MyDocumentListener(inputs, newTextField, true, false, true, hideIfEmpty));
				} catch (Exception e1) {e1.printStackTrace();}
			}
	    }
    	gridy++;
    	resizeScrollMax();
	}

	public void addLink(String displayedName, String link) {
    	JLabel hyperlink = new JLabel(displayedName);
    	hyperlink.setFont(font);
    	hyperlink.setForeground(Color.BLUE.darker());
    	hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	hyperlink.addMouseListener(new MouseAdapter() {
    	    @Override
    	    public void mouseClicked(MouseEvent e) {
    	        try {
    	             
    	            Desktop.getDesktop().browse(new URI(link));
    	             
    	        } catch (IOException | URISyntaxException e1) {
    	            e1.printStackTrace();
    	        }
    	    }
    	    @Override
            public void mouseExited(MouseEvent e) {
                hyperlink.setText(displayedName);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                hyperlink.setText("<html><a href=''>" + displayedName + "</a></html>");
            }
    	});
    	
    	gbc_constraints.gridy = gridy;
    	contentPanel.add(hyperlink, gbc_constraints);
    	hyperlink.putClientProperty("gridy", gridy);
    	
    	gridy++;
    	resizeScrollMax();
	}

	public JTextField addLabelAndTextField(String displayedText) {
		JPanel newPanel = new JPanel();
		newPanel.setBackground(backgroundColor);
    	
		gbc_constraints.gridy = gridy;
		contentPanel.add(newPanel, gbc_constraints);

    	JLabel newLabel = new JLabel(displayedText);
		newLabel.setFont(font);
		newLabel.setForeground(foregroundColor);
		newPanel.add(newLabel);
		newLabel.putClientProperty("gridy", gridy);
    	
		JTextField newTextField = new JTextField();
    	newTextField.setFont(font);
    	newPanel.add(newTextField);
    	newTextField.setColumns(20);
    	
    	gridy++;
    	resizeScrollMax();
    	return newTextField;	
	}
	
	public JLabel addButtonAndLabel(String displayedText, String objects) {
		return addButtonAndLabel(new Object[] {displayedText}, new Object[] {objects}, this.font);}
	public JLabel addButtonAndLabel(String displayedText, Object[] objects) {
		return addButtonAndLabel(new Object[] {displayedText}, objects, this.font);}
	public JLabel addButtonAndLabel(Object[] displayedText, String objects) {
		return addButtonAndLabel(displayedText, new Object[] {objects}, this.font);}
	public JLabel addButtonAndLabel(Object[] displayedTextObjects, Object[] objects) {
		return addButtonAndLabel(displayedTextObjects, objects, this.font);}
	
	public JLabel addButtonAndBoldLabel(String displayedText, String objects) {
		return addButtonAndLabel(new Object[] {displayedText}, new Object[] {objects}, this.font.deriveFont(Font.BOLD));}
	public JLabel addButtonAndBoldLabel(Object[] displayedText, String objects) {
		return addButtonAndLabel(displayedText, new Object[] {objects}, this.font.deriveFont(Font.BOLD));}
	public JLabel addButtonAndBoldLabel(String displayedText,Object[] objects) {
		return addButtonAndLabel(new Object[] {displayedText}, objects, this.font.deriveFont(Font.BOLD));}
	public JLabel addButtonAndBoldLabel(Object[] displayedText,Object[] objects) {
		return addButtonAndLabel(displayedText, objects, this.font.deriveFont(Font.BOLD));}
	
	public JLabel addButtonAndLabel(Object[] inputs, Object[] objects, Font font) {
		JPanel newPanel = new JPanel();
		newPanel.setBackground(backgroundColor);
		newPanel.setBorder(null);
    	
		gbc_constraints.gridy = gridy;
		contentPanel.add(newPanel, gbc_constraints);
    	
		ScaledButton newButton = new ScaledButton(copy_Icon, size);
    	
    	newButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			
    			String copyableText = "";
    			
    			for (Object obj : objects) {
    		    	if (obj instanceof String) {
    		    		copyableText += (String)obj;
    				} else if (obj instanceof JTextField) {
    					copyableText += ((JTextField) obj).getText();
    				} else {
    					throw new IllegalArgumentException(obj.getClass().toString() + " is an illegal Argument");
    				}
    		    }
    			StringSelection stringSelection = new StringSelection(copyableText);
    			clipboard.setContents(stringSelection, null);
    		}
    	});
    	
    	newButton.setBackground(backgroundColor);
    	newPanel.add(newButton);
    	
    	String displayedText = "";
    	for (Object obj : inputs) {
	    	if (obj instanceof String) {
	    		displayedText += (String)obj;
			}
	    }
    	
    	
    	JLabel newLabel = new JLabel(displayedText);
		newLabel.setFont(font);
		newLabel.setForeground(foregroundColor);
		newPanel.add(newLabel);
		newLabel.putClientProperty("gridy", gridy);
    	
    	for (Object textField : inputs) {
	    	if (textField instanceof JTextField) {
	    		try {
					((JTextField) textField).getDocument().addDocumentListener(new MyDocumentListener(inputs, newLabel, true, false, true, false));
				} catch (Exception e1) {e1.printStackTrace();}
			}
	    }
		
		gridy++;
		resizeScrollMax();
		return newLabel;
	}

	public JTextField addButtonAndTextField(String commandToCopy) {
		JTextField newTextField = new JTextField();
		
		JPanel newPanel = new JPanel();
		newPanel.setBackground(backgroundColor);
    	
		gbc_constraints.gridy = gridy;
		contentPanel.add(newPanel, gbc_constraints);
    	
    	/////////only difference to addBibField()/////////
		ScaledButton newCopyButton = new ScaledButton(copy_Icon, size);
    	newCopyButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			clipboard.setContents(new StringSelection(commandToCopy), null);
    		}
    	});
    	//////////////////////////////////////////////////
    	
    	newCopyButton.setBackground(backgroundColor);
    	newPanel.add(newCopyButton);
    	
    	newTextField.setFont(font);
    	newPanel.add(newTextField);
    	newTextField.setColumns(20);
    	
    	gridy++;
    	resizeScrollMax();
    	return newTextField;
	}
	
	public JTextField addCopyButtonAndLabelAndTextField(String displayedText) {
		JTextField newTextField = new JTextField();
		
		JPanel newPanel = new JPanel();
		newPanel.setBackground(backgroundColor);
    	
		gbc_constraints.gridy = gridy;
		contentPanel.add(newPanel, gbc_constraints);
    	
		ScaledButton newCopyButton = new ScaledButton(copy_Icon, size);
    	newCopyButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			clipboard.setContents(new StringSelection(newTextField.getText()), null);
    		}
    	});
    	
    	newCopyButton.setBackground(backgroundColor);
    	newPanel.add(newCopyButton);
    	
    	JLabel newLabel = new JLabel(displayedText);
		newLabel.setFont(font);
		newLabel.setForeground(foregroundColor);
		newPanel.add(newLabel);
		newLabel.putClientProperty("gridy", gridy);
    	
    	newTextField.setFont(font);
    	newPanel.add(newTextField);
    	newTextField.setColumns(20);
    	
    	gridy++;
    	resizeScrollMax();
    	return newTextField;
	}
	
	@Override
	public String toString() {
		String panelContent = "";
		JLabel[] labels = getAllJLabels();
		for (int i = 0; i < labels.length; ++i) {
			panelContent += labels[i].getText() + "\n";
		}
		return panelContent;
	}
	
	public JLabel[] getAllJLabels() {
		Component[] components = contentPanel.getComponents();
		ArrayList<JLabel> labelsList = new ArrayList<JLabel>();
		
		
		JLabel[] labels = Arrays.stream(components)
                .filter(c -> c instanceof JLabel) // nur JLabels
                .map(c -> (JLabel) c)              // casten
                .filter(label -> label.getText() != null) // Text != null
                .toArray(JLabel[]::new);           // in Array zurückwandeln
		labelsList.addAll(Arrays.asList(labels));
		
		
		
		
		JPanel[] panels = Arrays.stream(components)
                .filter(c -> c instanceof JPanel) // nur JPanels
                .map(c -> (JPanel) c)              // casten
                .toArray(JPanel[]::new);           // in Array zurückwandeln
		for (int i = 0; i < panels.length; i++) {
			for (Component comp : panels[i].getComponents()) {
		        if (comp instanceof JLabel) {
		        	labelsList.add((JLabel) comp);
		            break;
		        }
		    }
		}
				
		return labelsList.toArray(new JLabel[0]);
	}
	
	public Map<String, String> getTextfelds() {
		Map<String, String> userInputsTxFlds = new HashMap<>();
		userInputsTxFlds.put("fibPgmBib", fibPgmBibTextField.getText());
		userInputsTxFlds.put("fibDtaBib", fibDtaBibTextField.getText());
		userInputsTxFlds.put("fibSrcBib", fibSrcBibTextField.getText());
		userInputsTxFlds.put("assDtaBib", assDtaBibTextField.getText());
		userInputsTxFlds.put("assPgmBib", assPgmBibTextField.getText());
		userInputsTxFlds.put("aesPgmBib", aesPgmBibTextField.getText());
		userInputsTxFlds.put("aesDtaBib", aesDtaBibTextField.getText());
		userInputsTxFlds.put("optName", optNameTextField.getText());
		userInputsTxFlds.put("imgClgName", imgClgNameTextField.getText());
		userInputsTxFlds.put("imgClgPath", imgClgPathTextField.getText());
		userInputsTxFlds.put("jdkVersion", jdkTextField.getText());
		userInputsTxFlds.put("osVersion", osTextField.getText());
		userInputsTxFlds.put("tomCatDownCommand", tomCatDownTextField.getText());
		userInputsTxFlds.put("tomCatUpCommand", tomCatUpTextField.getText());
		userInputsTxFlds.put("tomCatVersion", tomCatVersionTextField.getText());
		userInputsTxFlds.put("tomCatJobName", tomCatJobNameTextField.getText());
		userInputsTxFlds.put("fibInsDirPath", fibInsDirPathTextField.getText());
		userInputsTxFlds.put("tomCatInsDirPath", tomCatInsDirPathTextField.getText());
		userInputsTxFlds.put("fibModBib", fibModBibTextField.getText());
		return userInputsTxFlds;
	}
	
	public void setTextfelds(Map<String, String> userInputsTxFlds) {
		for (Map.Entry<String, String> entry : userInputsTxFlds.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
				case "fibPgmBib":
					fibPgmBibTextField.setText(value);
					break;
				case "fibDtaBib":
					fibDtaBibTextField.setText(value);
					break;
				case "fibSrcBib":
					fibSrcBibTextField.setText(value);
					break;
				case "assDtaBib":
					assDtaBibTextField.setText(value);
					break;
				case "assPgmBib":
					assPgmBibTextField.setText(value);
					break;
				case "aesPgmBib":
					aesPgmBibTextField.setText(value);
					break;
				case "aesDtaBib":
					aesDtaBibTextField.setText(value);
					break;
				case "optName":
					optNameTextField.setText(value);
					break;
				case "imgClgName":
					imgClgNameTextField.setText(value);
					break;
				case "imgClgPath":
					imgClgPathTextField.setText(value);
					break;
				case "jdkVersion":
					jdkTextField.setText(value);
					break;
				case "osVersion":
					osTextField.setText(value);
					break;
				case "tomCatDownCommand":
					tomCatDownTextField.setText(value);
					break;
				case "tomCatUpCommand":
					tomCatUpTextField.setText(value);
					break;
				case "tomCatVersion":
					tomCatVersionTextField.setText(value);
					break;
				case "tomCatJobName":
					tomCatJobNameTextField.setText(value);
					break;
				case "fibInsDirPath":
					fibInsDirPathTextField.setText(value);
					break;
				case "tomCatInsDirPath":
					tomCatInsDirPathTextField.setText(value);
					break;
				case "fibModBib":
					fibModBibTextField.setText(value);
					break;
            }
        }
	}
	
	public String getNbr() {
		if (numbering[2] != 0) return this.numbering[0] + "." + this.numbering[1] + "." + this.numbering[2];
		if (numbering[1] != 0) return this.numbering[0] + "." + this.numbering[1];
		return this.numbering[0] + "";
	}
	
	public void add1AtNbr(int index) {
		for (int i = index+1; i < numbering.length; i++) {
			numbering[i] = 0;
		}
		numbering[index]++;
	}
}
