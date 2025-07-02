import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicArrowButton;

@SuppressWarnings("serial")
public class SearchBar extends JPanel{

	protected JLabel[] labels;
	private int searchIndex;
	private int searchCount;
	private JLabel searchInfoLabel;
	private Font font;
	private DescriptionPanel descriptionPanel;
	
	public SearchBar(Font font, DescriptionPanel descriptionPanel) {
		this.font = font;
		this.descriptionPanel = descriptionPanel;
		
		this.searchIndex = 1;
		this.searchCount = 0;
		this.labels = null;
		createSearchBar();
	}

	private void createSearchBar() {
		this.setLayout(new BorderLayout());
		
		JPanel searchPanelLeft = new JPanel();
		searchPanelLeft.setLayout(new BorderLayout());
		this.add(searchPanelLeft, BorderLayout.CENTER);
		
		JTextField searchField = new JTextField();
		searchField.setFont(font);
		searchPanelLeft.add(searchField, BorderLayout.CENTER);
		
		searchInfoLabel = new JLabel("");
		searchInfoLabel.setFont(font);
		searchPanelLeft.add(searchInfoLabel, BorderLayout.EAST);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		this.add(buttonPanel, BorderLayout.EAST);
		
		BasicArrowButton btnSearchUp = new BasicArrowButton(1);
		BasicArrowButton btnSearchDown = new BasicArrowButton(5);
		buttonPanel.add(btnSearchUp, BorderLayout.NORTH);
		buttonPanel.add(btnSearchDown, BorderLayout.SOUTH);
		
		
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchIndex = 0;
				searchCount = 0;
				searchForText(searchField.getText());
			}
		});
		
		btnSearchUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchIndex--;
				searchForText(searchField.getText());
			}
		});
		
		btnSearchDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchIndex++;
				searchForText(searchField.getText());
			}
		});
	}

	private void setSearchInfoLabel(int index, int count) {
		if (index <= 0 && count <= 0) {
			searchInfoLabel.setText("");
		} else {
			searchInfoLabel.setText(index + "/" + count);
		}
	}
	
	private void sortLabels() {
		Arrays.sort(labels, (a, b) -> Integer.compare((int)a.getClientProperty("gridy"), (int)b.getClientProperty("gridy")));
	}

	private void searchForText(String searchText) {
		DescriptionPanel.removeHighlights(labels);
		setSearchInfoLabel(0, 0);
		if (searchText.length() <= 0) return;
		
		if(labels == null) {
			labels = descriptionPanel.getAllJLabels();
			sortLabels();
		}
		
		searchText = searchText.toLowerCase();
		ArrayList<JLabel> matchingLabels = new ArrayList<JLabel>();
		for (int i = 0; i < labels.length; i++) {
			if (labels[i].getText().toLowerCase().contains(searchText)) {
				matchingLabels.add(labels[i]);
			}
		}
		if (matchingLabels.size() <= 0) return;
		if (searchIndex >= matchingLabels.size()) searchIndex = 0;
		else if (searchIndex < 0) searchIndex = matchingLabels.size() -1;
		
		for (int i = 0; i < matchingLabels.size(); i++) {
			DescriptionPanel.highlightLabel(matchingLabels.get(i), searchText, new Color(255, 255, 119)); //yellow
		}
		
		
		DescriptionPanel.removeHighlights(matchingLabels.get(searchIndex));
		DescriptionPanel.highlightLabel(matchingLabels.get(searchIndex), searchText, new Color(237, 151, 56)); //orange
		descriptionPanel.jumpToLabel(matchingLabels.get(searchIndex));
		
		searchCount = matchingLabels.size();
		setSearchInfoLabel(searchIndex + 1, searchCount);
	}
}
