package gui;
//By Joshua Froitzheim 2023
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import Exceptions.OriginalStateNotFoundException;
import Exceptions.PassedStateNotFoundException;
import Exceptions.StateAmountException;
import src.Controller;
import src.RowPair;
import src.Update;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;

@SuppressWarnings("serial")
public class View extends JFrame{
	
	private Controller controller;
	
	private JComboBox<String> typeComboBox;
	private SearchableJComboBox insJamUpdateComboBox;
	private SearchableJComboBox insAssUpdateComboBox;
	private SearchableJComboBox insFibUpdateComboBox;
	private SearchableJComboBox targetUpdateComboBox;
	
	private JButton copyAssButton;
	private JButton copyFibButton;
	private JButton copyTargetFibButton;
	private JButton copyTargetAssButton;
	
	private ActionListener typeComboBoxListener;
	private ActionListener insJamComboBoxListener;
	private ActionListener insAssComboBoxListener;
	private ActionListener insFibComboBoxListener;
	private ActionListener targetUpdateComboBoxListener;
	private ActionListener copyAssButtonListener;
	private ActionListener copyFibButtonListener;
	
	private JPanel listContainerNorth;
	
	private JPanel mainPanel;
	
	private JSplitPane splitPane;
	private JPanel panelTop;
	private JPanel comboBoxPanel;
	private JPanel listContainer;

	private DescriptionPanel descriptionPanel;
	
	private Color backgroundColor;
	private Color secondaryBackgroundColor;
	private Color foregroundColor;
	
	private JCheckBox appChckbx;
	
	private ArrayList<JCheckBox> checkBoxes;
	
	private ArrayList<ListItemPanel> listItems;
	
	private ListItemPanel openListItemPanel;
	private ListItemPanel currentSelectedPanel;
	
	private JPanel dropPanel;
	
	private JPopupMenu popMenu;
	private JMenuItem itemCheckBoth;
	private JMenuItem itemUnCheckBoth;
	private JMenuItem itemCheckGraphic;
	private JMenuItem itemCheckClassic;
	private JMenuItem itemUnCheckGraphic;
	private JMenuItem itemUnCheckClassic;
	
	private ArrayList<ListItemPanel> multiSelectedPanels;
	
	private Color secondaryForegroundColor;
	
	private int maxPanelWidth;
	private int minPanelWidth;
	
	private JScrollPane itemScrollPane;
	
	public View(Controller controller, Color backgroundColor, Color secondaryBackgroundColor, Color foregroundColor, Float fontSize, Color secondaryForegroundColor) {
		this.controller = controller;
		listItems = new ArrayList<ListItemPanel>();
		
		Font currentFont = getContentPane().getFont();
		currentFont = new Font(Font.SANS_SERIF , Font.PLAIN, 12);
		Font newFont = currentFont.deriveFont(currentFont.getSize() * fontSize);
		this.setFont(newFont);
		
		this.backgroundColor = backgroundColor;
		this.secondaryBackgroundColor = secondaryBackgroundColor;
		this.foregroundColor = foregroundColor;
		this.secondaryForegroundColor = secondaryForegroundColor;
		
		openListItemPanel = null;
		currentSelectedPanel = null;
		
		
		typeComboBoxListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedType = (String) typeComboBox.getSelectedItem();
				controller.typeChanged(selectedType);
			}
		};
		insJamComboBoxListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (insJamUpdateComboBox.getSelectedIndex() >= 0) {
					controller.insJamChanged(insJamUpdateComboBox.getSelectedIndex());
				}
			}
		};
		insAssComboBoxListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (insAssUpdateComboBox.getSelectedIndex() >= 0) {
					controller.insAssChanged(insAssUpdateComboBox.getSelectedIndex());
				}
			}
		};
		insFibComboBoxListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (insFibUpdateComboBox.getSelectedIndex() >= 0) {
					controller.insFibChanged(insFibUpdateComboBox.getSelectedIndex());
				}
			}
		};
		targetUpdateComboBoxListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (targetUpdateComboBox.getSelectedIndex() >= 0) {
					controller.targetUpdateChanged(targetUpdateComboBox.getSelectedIndex());
				}
			}
		};
		copyAssButtonListener = new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			controller.setAssClipboard();
    		}
    	};
    	copyFibButtonListener = new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			controller.setFibClipboard();
    		}
    	};
    	
		createGui();
		
		dropPanel = getDropPanel();
		getContentPane().setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				if (isInOptionsPanel() == true) return;
				System.out.println("isInOptionsPanel(): " + isInOptionsPanel());
				evt.acceptDrop(DnDConstants.ACTION_COPY);
				getContentPane().remove(dropPanel);
				getContentPane().add(mainPanel);
				getContentPane().validate();
				getContentPane().repaint();
				controller.dropEvent(evt.getTransferable()); //Must be executed after the dropPanel has been removed. Otherwise, the calculated preferred width of the ListItemPanel will be set to 0.
			}
			
			public void dragEnter(DropTargetDragEvent evt) {	
				if (isInOptionsPanel() == true) return;
				getContentPane().add(dropPanel);
				getContentPane().remove(mainPanel);
				getContentPane().validate();
				getContentPane().repaint();
		    }
			
			public void dragExit(DropTargetEvent evt) {
		    	getContentPane().remove(dropPanel);
				getContentPane().add(mainPanel);
				getContentPane().validate();
				getContentPane().repaint();
		    }
		});
		
		//HotKeys
		class KeyAction extends AbstractAction {
			public KeyAction(String actionCommand) {
				putValue(ACTION_COMMAND_KEY, actionCommand);
			}
			
			@Override
			public void actionPerformed(ActionEvent actionEvt) {
				if (isInOptionsPanel() == true) return;
				
				if (actionEvt.getActionCommand().equals("control S")) {
					controller.saveDataToFile();
				} else if (actionEvt.getActionCommand().equals("control L")) {
					controller.loadDataFromFile();
				} else if (actionEvt.getActionCommand().equals("control X")) {
					controller.exportListToFile();
				} else if (actionEvt.getActionCommand().equals("control alt S")) {
					controller.saveDataToClipboard();
				} else if (actionEvt.getActionCommand().equals("control alt L")) {
					controller.loadDataFromClipboard();
				} else if (actionEvt.getActionCommand().equals("control alt X")) {
					controller.exportListToClipboard();
				}
			}
		}
		
		splitPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"),"SaveData");
		splitPane.getActionMap().put("SaveData", new KeyAction("control S"));
		splitPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control L"),"LoadData");
		splitPane.getActionMap().put("LoadData", new KeyAction("control L"));
		splitPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control X"),"ExportData");
		splitPane.getActionMap().put("ExportData", new KeyAction("control X"));
		splitPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control alt S"),"SaveAltData");
		splitPane.getActionMap().put("SaveAltData", new KeyAction("control alt S"));
		splitPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control alt L"),"LoadAltData");
		splitPane.getActionMap().put("LoadAltData", new KeyAction("control alt L"));
		splitPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control alt X"),"ExportAltData");
		splitPane.getActionMap().put("ExportAltData", new KeyAction("control alt X"));
	}
	
	private boolean isInOptionsPanel() {
		Component[] components = getContentPane().getComponents();
		OptionPanel[] optionPanels = Arrays.stream(components)
                .filter(c -> c instanceof OptionPanel)
                .map(c -> (OptionPanel) c) // casten
                .toArray(OptionPanel[]::new);
		
		if (optionPanels.length >= 1) return true;
		return false;
	}
	
	private JPanel getDropPanel() {
		dropPanel = new JPanel();
		if (isColorDark(backgroundColor)) {
			dropPanel.setBorder(new LineBorder(backgroundColor, 50));
		} else {
			dropPanel.setBorder(new LineBorder(secondaryBackgroundColor, 50));
		}
		dropPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Drag&Drop files here");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(this.getFont()); 
		dropPanel.add(lblNewLabel);
		return dropPanel;
	}
	
	public void addAppCheckBox() {
		appChckbx = new JCheckBox("APP");
		appChckbx.setToolTipText("Wird die APP verwendet? Ja | Nein");
		appChckbx.setFont(this.getFont());
		
		appChckbx.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		controller.appCheckBoxChanged(appChckbx.isSelected());
    		}
		});
		comboBoxPanel.add(appChckbx);
	}
	
	public void addAssButton() {
		copyAssButton = new ScaledButton(getClass().getResource("/icons/Copy_Icon.png"), this.getFont());
		copyAssButton.addActionListener(copyAssButtonListener);
		copyAssButton.setToolTipText("DSPDTAARA Befehl in Zwischenablage kopieren");
		comboBoxPanel.add(copyAssButton);
	}
	
	public void addFibButton() {
		copyFibButton = new ScaledButton(getClass().getResource("/icons/Copy_Icon.png"), this.getFont());
		copyFibButton.addActionListener(copyFibButtonListener);
		copyFibButton.setToolTipText("DSPDTAARA Befehl in Zwischenablage kopieren");
		comboBoxPanel.add(copyFibButton);
	}
	
	public void addTargetFibButton() {
		copyTargetFibButton = new ScaledButton(getClass().getResource("/icons/Copy_Icon.png"), this.getFont());
		copyTargetFibButton.addActionListener(copyFibButtonListener);
		copyTargetFibButton.setToolTipText("DSPDTAARA Befehl in Zwischenablage kopieren");
		comboBoxPanel.add(copyTargetFibButton);
	}
	
	public void addTargetAssButton() {
		copyTargetAssButton = new ScaledButton(getClass().getResource("/icons/Copy_Icon.png"), this.getFont());
		copyTargetAssButton.addActionListener(copyAssButtonListener);
		copyTargetAssButton.setToolTipText("DSPDTAARA Befehl in Zwischenablage kopieren");
		comboBoxPanel.add(copyTargetAssButton);
	}

	public void addTargetUpdateInput(ArrayList<Update> list) {
		targetUpdateComboBox = addComboBox(list, targetUpdateComboBoxListener, "*Ziel Update", "Das Update, auf das geupdatet werden soll");
	}
	
	public void addInsJamUpdateInput(ArrayList<Update> list) {
		insJamUpdateComboBox = addComboBox(list, insJamComboBoxListener, "*Ins. JAM", "Die aktuell installierte JAM-Version (Meistens unter resources\\version.properties \"system\")");
	}
	
	public void addInsAssUpdateInput(ArrayList<Update> list) {
		insAssUpdateComboBox = addComboBox(list, insAssComboBoxListener, "*Ins. ASS", "Die aktuell installierte ASS-Version");
	}
	
	public void addInsFibUpdateInput(ArrayList<Update> list) {
		insFibUpdateComboBox = addComboBox(list, insFibComboBoxListener, "*Ins. FIB", "Die aktuell installierte FIB-Version");
	}
	
	public void addPanel(Update update) {
		this.addPanel(update, update.toString());
	}
	
	public void addOptionalPanel(Update update) {
		this.addPanel(update, update.toString() + "  -  (Optional)");
	}
	
	public void addReleasePanel(Update update) {
		this.addPanel(update, update.toString() + " R");
	}
	
	private void addPanel(Update update, String updStr) {
		ListItemPanel newPanel = new ListItemPanel(update, updStr, this, secondaryBackgroundColor, secondaryForegroundColor);
		checkBoxes.addAll(newPanel.checkBoxes);
		listItems.add(newPanel);
		
		listContainer.add(newPanel);
        listContainer.validate();
        newPanel.requestFocus(); //Remove potential focus from ComboBox
		getContentPane().validate();
        repaint();
	}
	
	private void createGui() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			showMessageDialog(e.toString());
		}
		setTitle("FiManS Update Assistent");
		try {
			BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource("pictures/fimans_logo_FI_neu.png"));
		    super.setIconImage(image);
		} catch (Exception e) {
			showMessageDialog("kein Bild an Pfad pictures/fimans_logo_FI_neu.png gefunden!\n\n" + e.toString());
			System.exit(0);
		}
		
		mainPanel = new JPanel();
		
		getContentPane().add(mainPanel);
		
		mainPanel.setLayout(new BorderLayout());
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPanel.add(splitPane);
		
		JPanel listPanel = new JPanel();
		splitPane.setLeftComponent(listPanel);
		splitPane.getLeftComponent().setMinimumSize(new Dimension(200, 0));
		listPanel.setLayout(new BorderLayout(0, 0));
		
		panelTop = new JPanel();
		mainPanel.add(panelTop, BorderLayout.NORTH);
		panelTop.setLayout(new BorderLayout());
		
		final JPopupMenu menu = new JPopupMenu("Menu");
		
		class ScaledMenuItem extends JMenuItem {
			ScaledMenuItem(String text, URL url, Font font) {
				JComboBox<String> tmpBox = new JComboBox<String>();
				tmpBox.setFont(font);
				int size = (int) Math.round(tmpBox.getPreferredSize().getHeight());
				int imageSize = (int) (size * 0.7);
				tmpBox = null; //just to make sure

				Image newImage = new ImageIcon(url).getImage();
				Image scaledImage = newImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
				ImageIcon copyTargetAssIcon = new ImageIcon(scaledImage);
		 
				this.setIcon(copyTargetAssIcon);
				this.setText(text);
			}
		}
		
		JMenuItem itemSave = new ScaledMenuItem("Speichern", getClass().getResource("/icons/Save_Icon.png"), this.getFont());
		itemSave.setToolTipText("STRG + S oder STRG + S + ALT für Zwischenablage");
		itemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.saveDataToFile();
			}
		});
		itemSave.setFont(this.getFont());
        menu.add(itemSave);
        
        menu.addSeparator();
        
        JMenuItem itemLoad = new ScaledMenuItem("Laden", getClass().getResource("/icons/Download_Icon.png"), this.getFont());
        itemLoad.setToolTipText("STRG + L oder STRG + L + ALT für Zwischenablage");
        itemLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.loadDataFromFile();
			}
		});
        itemLoad.setFont(this.getFont());
        menu.add(itemLoad);
        
        menu.addSeparator();
        
        JMenuItem itemExport = new ScaledMenuItem("Liste Exportieren", getClass().getResource("/icons/Description_Icon.png"), this.getFont());
        itemExport.setToolTipText("STRG + X oder STRG + X + ALT für Zwischenablage");
        itemExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.exportListToFile();
			}
		});
        itemExport.setFont(this.getFont());
        menu.add(itemExport);
        
        menu.addSeparator();
        
        JMenuItem itemNewList = new ScaledMenuItem("Neue Liste erstellen", getClass().getResource("/icons/NewList_Icon.png"), this.getFont());
        itemNewList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.newList();
			}
		});
        itemNewList.setFont(this.getFont());
        menu.add(itemNewList);
        
        menu.addSeparator();
        
        JMenuItem itemOptions = new ScaledMenuItem("Einstellungen", getClass().getResource("/icons/Gear_Icon.png"), this.getFont());
        itemOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showOptions();
			}
		});
        itemOptions.setFont(this.getFont());
        menu.add(itemOptions);
        
        menu.setFont(this.getFont());
        
        
        
        
        
        
        JButton menuButton = new ScaledButton(getClass().getResource("/icons/Menu_Icon.png"), this.getFont());
		menuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu.show(menuButton, 0, menuButton.getHeight());
			}
		});
		menuButton.setToolTipText("Menü");
		panelTop.add(menuButton, BorderLayout.WEST);
		
		comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.X_AXIS));
        panelTop.add(comboBoxPanel, BorderLayout.CENTER);
		
		
		
		
		
		typeComboBox = new JComboBox<String>()
		{
		    @Override
		    public Object getSelectedItem()
		    {
		        Object selected = super.getSelectedItem();
		        if (selected == null)
		            selected = "*Anwendung";
		        return selected;
		    }
		};
		typeComboBox.setFont(this.getFont());
		typeComboBox.setToolTipText("Die Anwendung, die geupdatet werden soll");
		comboBoxPanel.add(typeComboBox);	
		
		JPanel panelBottom = new JPanel();
		listPanel.add(panelBottom, BorderLayout.CENTER);
		panelBottom.setLayout(new BorderLayout(0, 0));
		
		itemScrollPane = new JScrollPane();
		disableArrowKeys(itemScrollPane);
		itemScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		panelBottom.add(itemScrollPane, BorderLayout.CENTER);
		
		listContainerNorth = new JPanel();
		listContainerNorth.setBackground(backgroundColor); 
		itemScrollPane.setViewportView(listContainerNorth);
		listContainerNorth.setLayout(new BorderLayout());
		
		listContainer = new JPanel(new GridLayout(0, 1));
		
		listContainerNorth.add(listContainer, BorderLayout.NORTH);
		
		JPanel tmpPanel = getNewTmpPanel(); // will be replaced by a DescriptionPanel
		splitPane.setRightComponent(tmpPanel);
		splitPane.setOneTouchExpandable(true);    
	    
		Toolkit toolkit =  Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
		float width = (70f/100)*dim.width;
		float height = (65f/100)*dim.height;
		setSize((int)width, (int)height);
		setMinimumSize(new Dimension(740, 420));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	controller.programClosing();
            }
        });
		setVisible(true);
		splitPane.getRightComponent().setMinimumSize(new Dimension());
		splitPane.setDividerLocation(1.0d);
		
		try {
			Field m = BasicSplitPaneUI.class.getDeclaredField("keepHidden");
			m.setAccessible(true);
			m.set(splitPane.getUI(), true); // Divider position will stick
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		popMenu = new JPopupMenu("popMenu");
		
		itemCheckBoth = new JMenuItem("Beide Ankreuzen");
		itemCheckBoth.setToolTipText("Häkchen setzen bei Classic- und Grafik-Teil");
		itemCheckBoth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setClassicCheckBx(getSelectedUpdates(), true);
				controller.setGraficCheckBx(getSelectedUpdates(), true);
			}
		});
		itemCheckBoth.setFont(this.getFont());
		
		itemUnCheckBoth = new JMenuItem("Beide Entkreuzen");
		itemUnCheckBoth.setToolTipText("Häkchen entfernen bei Classic- und Grafik-Teil");
		itemUnCheckBoth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setClassicCheckBx(getSelectedUpdates(), false);
				controller.setGraficCheckBx(getSelectedUpdates(), false);
			}
		});
		itemUnCheckBoth.setFont(this.getFont());
		
		itemCheckClassic = new JMenuItem("Classic Ankreuzen");
		itemCheckClassic.setToolTipText("Häkchen setzen bei Classic-Teil");
		itemCheckClassic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setClassicCheckBx(getSelectedUpdates(), true);
			}
		});
		itemCheckClassic.setFont(this.getFont());
		
		itemCheckGraphic = new JMenuItem("Grafik Ankreuzen");
		itemCheckGraphic.setToolTipText("Häkchen setzen bei Grafik-Teil");
		itemCheckGraphic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setGraficCheckBx(getSelectedUpdates(), true);
			}
		});
		itemCheckGraphic.setFont(this.getFont());
		
		itemUnCheckClassic = new JMenuItem("Classic Entkreuzen");
		itemUnCheckClassic.setToolTipText("Häkchen entfernen bei Classic-Teil");
		itemUnCheckClassic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setClassicCheckBx(getSelectedUpdates(), false);
			}
		});
		itemUnCheckClassic.setFont(this.getFont());
		
		itemUnCheckGraphic = new JMenuItem("Grafik Entkreuzen");
		itemUnCheckGraphic.setToolTipText("Häkchen entfernen bei Grafik-Teil");
		itemUnCheckGraphic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setGraficCheckBx(getSelectedUpdates(), false);
			}
		});
		itemUnCheckGraphic.setFont(this.getFont());
		
		popMenu.setFont(this.getFont());
		
		maxPanelWidth = 9999;
		minPanelWidth = 0;
		
		this.splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
            	SwingUtilities.invokeLater(new Runnable() {
            		public void run() {
                    	int leftComponentWidth = splitPane.getSize().width - splitPane.getRightComponent().getWidth() - (splitPane.getDividerSize() * 6);     	
                    	if (between(leftComponentWidth, minPanelWidth, maxPanelWidth)) {
    	            		setPanelWidth(leftComponentWidth);
    					} else if (leftComponentWidth > maxPanelWidth) {
    						setPanelWidth(maxPanelWidth);
    					} else if (leftComponentWidth < minPanelWidth) {
    						setPanelWidth(minPanelWidth);
    					}
            		}
            	});
            }
        });
		
		SplitPaneUI spui = this.splitPane.getUI();
	    if (spui instanceof BasicSplitPaneUI) {
	        ((BasicSplitPaneUI) spui).getDivider().addMouseListener(new MouseAdapter() {
	        	
	            @Override
	            public void mouseClicked(MouseEvent arg0) {
	                if (arg0.getClickCount() == 2) {
	                	splitPane.setDividerLocation(minPanelWidth + ((minPanelWidth/100)*6));
	                }
	            }
	        });
	    }
	}
	
	private static void disableArrowKeys(JScrollPane scrollPane) {
		InputMap inputMap = scrollPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		// Dummy action that does nothing - used to block the key press
		Action dummyAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Do nothing - this blocks the key
			}
		};
		
		String[] arrowKeys = {"UP", "DOWN"};
		for (String key : arrowKeys) {
			inputMap.put(KeyStroke.getKeyStroke(key), "none_" + key);
			scrollPane.getActionMap().put("none_" + key, dummyAction);
		}
	}
	
	public void fireDividerPropertyChange() { //triggers PropertyChangeListener and is called in showPath()
		splitPane.firePropertyChange(JSplitPane.DIVIDER_LOCATION_PROPERTY, - 1, splitPane.getDividerLocation()); //Name, Old, New (-1 is a fake Value)
	}
	
	public void setMaxPanelWidth(int newWidth) {
		this.maxPanelWidth = newWidth;
	}
	
	public void setMinPanelWidth(int newWidth) {
		this.minPanelWidth = newWidth;
	}
	
	private static boolean between(int variable, int minValueInclusive, int maxValueInclusive) {
	    return variable >= minValueInclusive && variable <= maxValueInclusive;
	}
	
	private void setPanelWidth(int newWidth) {
        for (int i = 0; i < listItems.size(); i++) {
        	listItems.get(i).setInnerPanelPreferredWidth(newWidth);
        	listItems.get(i).revalidate();
		}
    }
	
	private void generatePopMenu(ArrayList<Update> selectedUpdates) {
		boolean allChecked = true;
        boolean noneChecked = true;
        boolean hasJamOnly = true;
        boolean allGraficChecked = true;
        boolean allClassicChecked = true;
        boolean onlyOneEntry = true;
		
		Map<Update, ArrayList<Boolean>> correlatingStates = getCorrelatingCheckBoxStates();
		
		int index = 0;
		for (Map.Entry<Update, ArrayList<Boolean>> entry : correlatingStates.entrySet()) {
			Update update = entry.getKey();
            ArrayList<Boolean> states = entry.getValue();
            if (selectedUpdates.contains(update) == false) continue;
            index++;
            if (index > 1) onlyOneEntry = false;
			if (update.getType().equals("JAM") == false) {
				hasJamOnly = false;
			}
			
			for (Boolean state : states) {
                if (!state) allChecked = false;
                if (state) noneChecked = false;
            }
			
			if (update.getType().equals("JAM")) {
				if (!states.get(0)) allGraficChecked = false;
			} else {
				if (!states.get(0)) allClassicChecked = false;
				if (!states.get(1)) allGraficChecked = false;
			}
		}
		
		popMenu.removeAll();
		
		if (hasJamOnly) {
			if (allChecked) {
				popMenu.add(itemUnCheckGraphic);
			} else if (noneChecked) {
				popMenu.add(itemCheckGraphic);
			} else {
				popMenu.add(itemUnCheckGraphic);
				popMenu.addSeparator();
				popMenu.add(itemCheckGraphic);
			}
		} else {
			if (allChecked) {
				popMenu.add(itemUnCheckBoth);
				popMenu.addSeparator();
				popMenu.add(itemUnCheckClassic);
				popMenu.addSeparator();
				popMenu.add(itemUnCheckGraphic);
			} else if (noneChecked) {
				popMenu.add(itemCheckBoth);
				popMenu.addSeparator();
				popMenu.add(itemCheckClassic);
				popMenu.addSeparator();
				popMenu.add(itemCheckGraphic);
			} else {
				if (allClassicChecked) {
					if (onlyOneEntry) {
						popMenu.add(itemCheckGraphic);
						popMenu.addSeparator();
						popMenu.add(itemUnCheckClassic);
					} else {
						popMenu.add(itemCheckGraphic);
						popMenu.addSeparator();
						popMenu.add(itemUnCheckBoth);
						popMenu.addSeparator();
						popMenu.add(itemUnCheckClassic);
						popMenu.addSeparator();
						popMenu.add(itemUnCheckGraphic);
					}
				} else if (allGraficChecked) {
					if (onlyOneEntry) {
						popMenu.add(itemCheckClassic);
						popMenu.addSeparator();
						popMenu.add(itemUnCheckGraphic);
					} else {
						popMenu.add(itemCheckClassic);
						popMenu.addSeparator();
						popMenu.add(itemUnCheckBoth);
						popMenu.addSeparator();
						popMenu.add(itemUnCheckClassic);
						popMenu.addSeparator();
						popMenu.add(itemUnCheckGraphic);
					}
				} else {
					popMenu.add(itemCheckBoth);
					popMenu.addSeparator();
					popMenu.add(itemCheckClassic);
					popMenu.addSeparator();
					popMenu.add(itemCheckGraphic);
					popMenu.addSeparator();
					popMenu.add(itemUnCheckBoth);
					popMenu.addSeparator();
					popMenu.add(itemUnCheckClassic);
					popMenu.addSeparator();
					popMenu.add(itemUnCheckGraphic);
				}
			}
		}
	}
	
	public void removeDescriptionPanel() {
		splitPane.setRightComponent(getNewTmpPanel());
		splitPane.setDividerLocation(1.0d);
	}
	
	private JPanel getNewTmpPanel() {
		JPanel tmpPanel = new JPanel();
		tmpPanel.setLayout(new BorderLayout());
		tmpPanel.setBackground(backgroundColor);
		
		JLabel label = new JLabel("<html><body><div style='text-align: center;'>Bitte ein Update aus dem linken Bereich auswählen.<br><br><br><br>FiManS Update Assistent<br>By Joshua Froitzheim 2025</body></html>");
		label.setForeground(foregroundColor);
		label.setFont(this.getFont());
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		
		tmpPanel.add(label, BorderLayout.CENTER);
		return tmpPanel;
	}
	
	private SearchableJComboBox addComboBox(ArrayList<Update> arrayList, ActionListener actionListener, String placeholderText, String toolTip) {
		SearchableJComboBox newComboBox = new SearchableJComboBox(){
		    @Override
		    public Object getSelectedItem()
		    {
		        Object selected = super.getSelectedItem();
		        if (selected == null) selected = placeholderText;
		        return selected;
		    }
		};
		newComboBox.setMinimumSize(new Dimension(80, newComboBox.getHeight()));
		newComboBox.setFont(this.getFont());
		newComboBox.setToolTipText(toolTip);
		comboBoxPanel.add(newComboBox);
		setComboBox(newComboBox, arrayList, actionListener);
		comboBoxPanel.validate();
		return newComboBox;
	}
	
	private void setComboBox(JComboBox<Update> newComboBox, ArrayList<Update> arrayList, ActionListener actionListener) {
		if (newComboBox.getActionListeners().length > 0) {
			newComboBox.removeActionListener(typeComboBoxListener);;
		}
		newComboBox.removeAllItems();
		for (int i = 0; i < arrayList.size(); i++) {
			newComboBox.addItem(arrayList.get(i));
		}
		newComboBox.setSelectedItem(null);
		newComboBox.addActionListener(actionListener);
	}

	private void removeComponent(Component component) {
		if (component == null) return;
		comboBoxPanel.remove(component);
		comboBoxPanel.validate();
	}
	
	public void setTypes(String[] types) {
		if (typeComboBox.getActionListeners().length > 0) {
			typeComboBox.removeActionListener(typeComboBoxListener);;
		}
		typeComboBox.removeAllItems();
		for (int i = 0; i < types.length; i++) {
			typeComboBox.addItem(types[i]);
		}
		typeComboBox.setSelectedItem(null);
		typeComboBox.addActionListener(typeComboBoxListener);
	}

	public void removeTargetUpdateInput() {
		removeComponent(copyAssButton);
		removeComponent(copyFibButton);
		removeComponent(targetUpdateComboBox);
	}
	public void removeJamUpdateInput() {
		removeComponent(copyAssButton);
		removeComponent(copyFibButton);
		removeComponent(insJamUpdateComboBox);
	}
	public void removeAssUpdateInput() {
		if (targetUpdateComboBox != null) {
			if (targetUpdateComboBox.getItemCount() > 0) {
				if (targetUpdateComboBox.getItemAt(0).getType().equals("FIB")) {
					removeComponent(copyAssButton);
				}
			}
		}
		removeComponent(insAssUpdateComboBox);
	}
	public void removeFibUpdateInput() {
		removeComponent(insFibUpdateComboBox);
	}
	public void removeAppChckbx() {
		removeComponent(appChckbx);
	}
	
	public void removeTargetFibButton() {
		removeComponent(copyTargetFibButton);
	}
	
	public void removeTargetAssButton() {
		removeComponent(copyTargetAssButton);
	}
	
	
	public void clearList() {
		checkBoxes = new ArrayList<JCheckBox>();
		listItems = new ArrayList<ListItemPanel>();
		listContainer.removeAll();
		listContainer.validate();
		getContentPane().validate();
		
		currentSelectedPanel = null;
	}

	public void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	
	public void setDesciption(Update update) {
		descriptionPanel = controller.getNewDescriptionPanel(update);
		
		splitPane.setRightComponent(descriptionPanel);
		
		if ((splitPane.getDividerLocation() + 50) > getContentPane().getWidth()) {
			splitPane.setDividerLocation(0.42d);
		} else {
			splitPane.setDividerLocation(splitPane.getDividerLocation());
		}
	}
	
	public void setScrollPos(int scrollPos) {
		descriptionPanel.setVerticalScrollPos(scrollPos);
	}
	
	public int getScrollPos() {
		if (openListItemPanel == null) return 0;
		return this.openListItemPanel.descriptionVerticalScrollPos;
	}
	
	public boolean appChckbxIsSelected() {
		if (appChckbx != null) {
			return appChckbx.isSelected();
		} else {
			return false;
		}
	}
	
	public void setAppChckbx(boolean state) {
		appChckbx.setSelected(state);
	}
	
	public void setTextfelds(Map<String, String> userInputsTxFlds) {
		if (descriptionPanel == null) return; //redundant because of desciptionExists();
		descriptionPanel.setTextfelds(userInputsTxFlds);
	}
	
	public Map<String, String> getTextfeldsValues() {
		if (descriptionPanel == null) return null;
		return descriptionPanel.getTextfelds();
	}
	
	public Map<Update, ArrayList<Boolean>> getCorrelatingCheckBoxStates() {
		
		Map<Update, ArrayList<Boolean>> correlatingStates = new HashMap<>();
		
		if (listItems == null) return correlatingStates;
		
		for (int i = 0; i < listItems.size(); i++) {
			
			ArrayList<Boolean> checkBoxStates = new ArrayList<Boolean>();
			
			for (JCheckBox jCheckBox : listItems.get(i).checkBoxes) {
				checkBoxStates.add(jCheckBox.isSelected());
			}
			
			correlatingStates.put(listItems.get(i).getUpdate(), checkBoxStates);
		}
		return correlatingStates;
	}
	
	public void setBoxStates(Map<Update, ArrayList<Boolean>> correlatingStates) throws PassedStateNotFoundException {
		setBoxStates(correlatingStates, true);
	}
	
	public void setBoxStates(Map<Update, ArrayList<Boolean>> correlatingStates, boolean showOriginalStateNotFoundException) throws PassedStateNotFoundException {
		if (correlatingStates == null) return;
		if (correlatingStates.size() <= 0) return;
		
		ArrayList<Update> containtUpdates = new ArrayList<Update>();
		
		for (Map.Entry<Update, ArrayList<Boolean>> entry : correlatingStates.entrySet()) {
			containtUpdates.add(entry.getKey());
		}
		
		String originalStateNotFoundUpdates = "";
		String stateAmountExceptionUpdates = "";
		String passedStateNotFoundUpdates = "";
		for (int item = 0; item < listItems.size(); item++) {
			Update update = listItems.get(item).getUpdate();
			ArrayList<JCheckBox> checkBoxes = listItems.get(item).checkBoxes;
			
			if (containtUpdates.contains(update) == false) {
				originalStateNotFoundUpdates += update.toString() + "\n";
				continue;
			}
			
			Map.Entry<Update, ArrayList<Boolean>> entry = getEntry(correlatingStates, update);
			if (entry == null) return;
			correlatingStates.remove(entry.getKey());
			
			if (entry.getValue().size() != checkBoxes.size()) {
				stateAmountExceptionUpdates += update.toString() + "\n";
				continue;
			}
			
			for (int box = 0; box < checkBoxes.size(); box++) {
				checkBoxes.get(box).setSelected(entry.getValue().get(box));
			}
		}
		if (correlatingStates.size() > 0) {
			for (Map.Entry<Update, ArrayList<Boolean>> entry : correlatingStates.entrySet()) {
				passedStateNotFoundUpdates += entry.getKey() + "\n";
			}
		}
		
		if (showOriginalStateNotFoundException && originalStateNotFoundUpdates.length() > 0) {
			controller.showMessageDialog(new OriginalStateNotFoundException(originalStateNotFoundUpdates).getMessage()); //Cannot be thrown so that subsequent exceptions can also be displayed.
		}
		
		if (stateAmountExceptionUpdates.length() > 0) {
			controller.showMessageDialog(new StateAmountException(stateAmountExceptionUpdates).getMessage()); //Cannot be thrown so that subsequent exceptions can also be displayed.
		}
		
		if (passedStateNotFoundUpdates.length() > 0) {
			throw new PassedStateNotFoundException(passedStateNotFoundUpdates);
		}
		getContentPane().validate();
		getContentPane().repaint();
	}
	
	private Map.Entry<Update, ArrayList<Boolean>> getEntry(Map<Update, ArrayList<Boolean>> correlatingStates, Update update) {
		for (Map.Entry<Update, ArrayList<Boolean>> entry : correlatingStates.entrySet()) {
			if (entry.getKey().equals(update)) {
				return entry;
			}
		}
		return null;
	}
	
	public void setTypeComboBox(String type) {
		this.typeComboBox.setSelectedItem(type);
	}
	
	public void setTargetUpdateComboBox(int index) {
		this.targetUpdateComboBox.setSelectedIndex(index);
	}
	
	public void setInsJamUpdateComboBox(int index) {
		this.insJamUpdateComboBox.setSelectedIndex(index);
	}
	
	public void setInsAssUpdateComboBox(int index) {
		this.insAssUpdateComboBox.setSelectedIndex(index);
	}
	
	public void setInsFibUpdateComboBox(int index) {
		this.insFibUpdateComboBox.setSelectedIndex(index);
	}
	
	public String getFibModBib() {
		String fibModBib = "";
		if (this.descriptionPanel != null) fibModBib = descriptionPanel.getTextfelds().get("fibModBib");
		return fibModBib;
	}
	
	public void showModListInput(ModListPanel modListPanel) {
		getContentPane().remove(mainPanel);
		
		modListPanel.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getContentPane().remove(modListPanel);
				getContentPane().add(mainPanel);
				getContentPane().validate();
				getContentPane().repaint();
			}
		});
		
		modListPanel.btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<RowPair> input = modListPanel.getList();
				getContentPane().remove(modListPanel);
				getContentPane().add(mainPanel);
				getContentPane().validate();
				getContentPane().repaint();
				controller.generateModListOutput(input);
			}
		});
		
		getContentPane().add(modListPanel);
		getContentPane().validate();
		getContentPane().repaint();
	}
	
	private void showOptions() {
		getContentPane().remove(mainPanel);
		
		OptionPanel optionPanel = controller.getOptionPanel();
		
		optionPanel.btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.optionCancelPressed(optionPanel);
			}
		});
		
		optionPanel.btnChoosePathToUpdateFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = controller.getPathToUpdate();
				optionPanel.setTextFieldPathToUpdates(path);
			}
		});
		
		optionPanel.btnChoosePathToFibUpdateFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = controller.getPathToFibUpdate();
				optionPanel.setTextFieldPathToFibUpdateFiles(path);
			}
		});
		
		optionPanel.btnChoosePathToFibReleaseFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = controller.getPathToFibRelease();
				optionPanel.setTextFieldPathToFibReleaseFiles(path);
			}
		});
		
		optionPanel.btnChoosePathToAssFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = controller.getPathToAss();
				optionPanel.setTextFieldPathToAssFiles(path);
			}
		});
		
		optionPanel.btnChoosePathToJamFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = controller.getPathToJam();
				optionPanel.setTextFieldPathToJamFiles(path);
			}
		});
		
		optionPanel.btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.optionSavePressed(optionPanel);
			}
		});
		
		optionPanel.btnCheckForVedaUpdatesNow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.checkForVedaUpdatesNow();
			}
		});
		
		optionPanel.btnChooseBackgroundColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.chooseBackgroundColor(optionPanel);
			}
		});
		
		optionPanel.btnChooseSecondaryBackgroundColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.chooseSecondaryBackgroundColor(optionPanel);
			}
		});
		
		optionPanel.btnChooseForegroundColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.chooseForegroundColor(optionPanel);
			}
		});
		
		optionPanel.btnChooseSecondaryForegroundColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.chooseSecondaryForegroundColor(optionPanel);
			}
		});
		
		
		optionPanel.textFieldBackgroundColor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					Color newColor = Color.decode(optionPanel.textFieldBackgroundColor.getText());
					optionPanel.backgroundColorPreview.setBackground(newColor);
				} catch (Exception e2) {
					optionPanel.backgroundColorPreview.setBackground(Color.black);
				}	
			}
		});
		
		optionPanel.textFieldSecondaryBackgroundColor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					Color newColor = Color.decode(optionPanel.textFieldSecondaryBackgroundColor.getText());
					optionPanel.secondaryBackgroundColorPreview.setBackground(newColor);
				} catch (Exception e2) {
					optionPanel.secondaryBackgroundColorPreview.setBackground(Color.black);
				}	
			}
		});
		
		optionPanel.textFieldForegroundColor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					Color newColor = Color.decode(optionPanel.textFieldForegroundColor.getText());
					optionPanel.foregroundColorPreview.setBackground(newColor);
				} catch (Exception e2) {
					optionPanel.foregroundColorPreview.setBackground(Color.black);
				}	
			}
		});
		
		optionPanel.textFieldSecondaryForegroundColor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					Color newColor = Color.decode(optionPanel.textFieldSecondaryForegroundColor.getText());
					optionPanel.secondaryForegroundColorPreview.setBackground(newColor);
				} catch (Exception e2) {
					optionPanel.secondaryForegroundColorPreview.setBackground(Color.black);
				}	
			}
		});
		
		optionPanel.btnChoosePathToVersionCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = controller.getPathToVersionCSV();
				optionPanel.setTextFieldPathToVersionCSV(path);
			}
		});
		
		getContentPane().add(optionPanel);
		getContentPane().validate();
		getContentPane().repaint();
	}
	
	public Color openColorChooser(Color initialColor) {
		Color selectedColor = JColorChooser.showDialog(this, "Wählen Sie eine Farbe", initialColor);
		return selectedColor;
	}

	public void removeItemHighlighted() {
		for (int i = 0; i < listItems.size(); i++) {
			listItems.get(i).isSelected = false;
			listItems.get(i).removeHighlightOnMe();
		}
	}

	public void openDesciptionPressed(ListItemPanel listIItemPanel) {
		if (this.openListItemPanel != null && this.descriptionPanel != null) {
			openListItemPanel.descriptionVerticalScrollPos = this.descriptionPanel.getVerticalScrollPos();
		}
		openListItemPanel = listIItemPanel;
		
		controller.openDesciptionPressed(listIItemPanel.getUpdate());
	}

	public boolean desciptionExists() {
		return (descriptionPanel != null);
	}

	public void removeOptionPanel(OptionPanel optionPanel) {
		getContentPane().remove(optionPanel);
		getContentPane().add(mainPanel);
		getContentPane().validate();
		getContentPane().repaint();
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		listContainerNorth.setBackground(backgroundColor);
		listContainerNorth.repaint();
		listContainerNorth.validate();
		splitPane.getRightComponent().setBackground(backgroundColor);
	}
	
	public void setSplitPaneDivider() {
		if ((splitPane.getDividerLocation()) <= 1) {
			setSplitPaneDivider(0.42d);
		} else {
			setSplitPaneDivider(splitPane.getDividerLocation());
		}
	}
	
	public void setSplitPaneDivider(double location) {
		splitPane.setDividerLocation(location);
	}
	
	public void setSplitPaneDivider(int location) {
		splitPane.setDividerLocation(location);
	}
	
	public Point getViewLocation() {
		return this.getLocationOnScreen();
	}
	
	public void rightButtonPressed(MouseEvent e, ListItemPanel panel) {
		if (panel.isSelected) {
			openPopMenu(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	public void leftButtonPressed(ListItemPanel selectedPanel) {
		multiSelectedPanels = new ArrayList<ListItemPanel>();
		multiSelectedPanels.add(selectedPanel);
		currentSelectedPanel = selectedPanel;
		removeItemHighlighted();
		selectedPanel.makeSelected();
		selectedPanel.requestFocus(); //Remove potential focus from ComboBox
		openDesciptionPressed(selectedPanel);
	}
	
	public void shiftLeftButtonPressed(ListItemPanel selectedPanel) {
		/*When a new list is created via the corresponding button, the function newList() -> setTypeComboBox() triggers the function typeChanged().
		This in turn calls clearList(), which ultimately sets currentSelectedPanel to null.
		Otherwise, currentSelectedPanel from the previous list would be retained, which would result in an ArrayIndexOutOfBoundsException here.
		setTypeComboBox() is also called when loading a list.*/
		
		if (multiSelectedPanels == null) multiSelectedPanels = new ArrayList<ListItemPanel>();
		multiSelectedPanels = getMultiSelectedPanels(selectedPanel);
		removeItemHighlighted();
		for (int i = 0; i < multiSelectedPanels.size(); i++) {
			multiSelectedPanels.get(i).makeSelected();
		}
	}
	
	public void ctrlLeftButtonPressed(ListItemPanel selectedPanel) {
		if (multiSelectedPanels == null) multiSelectedPanels = new ArrayList<ListItemPanel>();
		currentSelectedPanel = selectedPanel;
		
		if (selectedPanel.isSelected == false) {
			multiSelectedPanels.add(selectedPanel);
			selectedPanel.makeSelected();
		} else {
			multiSelectedPanels.remove(multiSelectedPanels.indexOf(selectedPanel));
			selectedPanel.removeSelected();
		}
	}

	private ArrayList<ListItemPanel> getMultiSelectedPanels(ListItemPanel selectedPanel) {
		ArrayList<ListItemPanel> result = new ArrayList<ListItemPanel>();
		int start = 0;
		int end = 0;
		if (currentSelectedPanel == null) {
			currentSelectedPanel = listItems.get(0);
		}
		
		if (listItems.indexOf(currentSelectedPanel) > listItems.indexOf(selectedPanel)) {
			start = listItems.indexOf(selectedPanel);
			end = listItems.indexOf(currentSelectedPanel);
		} else {
			start = listItems.indexOf(currentSelectedPanel);
			end = listItems.indexOf(selectedPanel);
		}
		
		for (int i = start; i < listItems.size(); i++) {
			result.add(listItems.get(i));
			if (i == end) break;
		}
		return result;
	}
	
	public ArrayList<Update> getSelectedUpdates() {
		ArrayList<Update> result = new ArrayList<Update>();
		for (int i = 0; i < multiSelectedPanels.size(); i++) {
			result.add(multiSelectedPanels.get(i).getUpdate());
		}
		return result;
	}

	public void openPopMenu(Component invoker, int x, int y) {
		ArrayList<Update> selectedUpdates = getSelectedUpdates();
		generatePopMenu(selectedUpdates);
		popMenu.show(invoker, x, y);
		getContentPane().validate();
		getContentPane().repaint();
	}

	public int getMinPanelSize() {
		int minPanelSize = 0;
		
		for (ListItemPanel item : listItems) {
			if (item.getMinPanelSize() > minPanelSize) {
				minPanelSize = item.getMinPanelSize();
			}
		}
		
		return minPanelSize;
	}
	
	public static boolean isColorDark(Color color) {
        int sum = color.getRed() + color.getGreen() + color.getBlue();
        double brightness = sum / 3.0 / 255.0;
        return brightness < 0.5;
    }

	public void goDown() {
		if (listItems.size() == 1) return;
		removeItemHighlighted();
		multiSelectedPanels = new ArrayList<ListItemPanel>();
		
		int index = listItems.indexOf(currentSelectedPanel);
		if (listItems.size()-1 > index) {
			index++;
		} else {
			index = 0;
		}
		currentSelectedPanel = listItems.get(index);

		multiSelectedPanels.add(currentSelectedPanel);
		currentSelectedPanel.makeSelected();
		currentSelectedPanel.requestFocus(); //Remove potential focus from ComboBox
		openDesciptionPressed(currentSelectedPanel);
		jumpToItem(listItems.get(index));
	}

	private void jumpToItem(ListItemPanel targetItem) {
		SwingUtilities.invokeLater(() -> {
			Rectangle bounds = targetItem.getBounds();
			listContainer.scrollRectToVisible(bounds);
		});
	}
	
	public void goUp() {
		if (listItems.size() == 1) return;
		removeItemHighlighted();
		multiSelectedPanels = new ArrayList<ListItemPanel>();
		
		int index = listItems.indexOf(currentSelectedPanel);
		if (index != 0) {
			index--;
		} else {
			index = listItems.size()-1;
		}
		currentSelectedPanel = listItems.get(index);
		
		multiSelectedPanels.add(currentSelectedPanel);
		currentSelectedPanel.makeSelected();
		currentSelectedPanel.requestFocus(); //Remove potential focus from ComboBox
		openDesciptionPressed(currentSelectedPanel);
		jumpToItem(listItems.get(index));
	}

	public void goControlDown() {
		if (listItems.size() == 1) return;
		removeItemHighlighted();
		multiSelectedPanels = new ArrayList<ListItemPanel>();
		currentSelectedPanel = listItems.get(listItems.size()-1);
		
		multiSelectedPanels.add(currentSelectedPanel);
		currentSelectedPanel.makeSelected();
		currentSelectedPanel.requestFocus(); //Remove potential focus from ComboBox
		openDesciptionPressed(currentSelectedPanel);
		jumpToItem(listItems.get(listItems.size()-1));
	}

	public void goControlUp() {
		if (listItems.size() == 1) return;
		removeItemHighlighted();
		multiSelectedPanels = new ArrayList<ListItemPanel>();
		currentSelectedPanel = listItems.get(0);
		
		multiSelectedPanels.add(currentSelectedPanel);
		currentSelectedPanel.makeSelected();
		currentSelectedPanel.requestFocus(); //Remove potential focus from ComboBox
		openDesciptionPressed(currentSelectedPanel);
		jumpToItem(listItems.get(0));
	}

	public void setModListOutput(String output) {
		if (this.descriptionPanel == null) return;
		this.descriptionPanel.setModListOutput(output);
	}
}
