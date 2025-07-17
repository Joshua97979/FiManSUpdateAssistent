package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class ScaledButton extends JButton{
	ScaledButton(URL url, Font font){
		JComboBox<String> tmpBox = new JComboBox<String>();
		tmpBox.setFont(font);
		int size = (int) Math.round(tmpBox.getPreferredSize().getHeight());
		int imageSize = (int) (size * 0.7);
		tmpBox = null; //just to make sure

		Image newImage = new ImageIcon(url).getImage();
		Image scaledImage = newImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
		ImageIcon copyTargetAssIcon = new ImageIcon(scaledImage);
 
		this.setIcon(copyTargetAssIcon);
		
		this.setPreferredSize(new Dimension(size, size)); 
		this.setMaximumSize(new Dimension(size, size));
	}
}
