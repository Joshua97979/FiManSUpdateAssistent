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
	
	private int size;
	private int imageSize;
	
	ScaledButton(URL url, int size, int imageSize){
		this.size = size;
		this.imageSize = imageSize;
		this.setScaledIcon(url);
		
		this.setPreferredSize(new Dimension(size, size)); 
		this.setMaximumSize(new Dimension(size, size));
	}
	
	ScaledButton(URL url, Font font){
		this(font);
		this.setScaledIcon(url);
	}

	public ScaledButton(Font font) {
		JComboBox<String> tmpBox = new JComboBox<String>();
		tmpBox.setFont(font);
		this.size = (int) Math.round(tmpBox.getPreferredSize().getHeight());
		this.imageSize = (int) (size * 0.7);
		tmpBox = null; //just to make sure
		
		this.setPreferredSize(new Dimension(size, size)); 
		this.setMaximumSize(new Dimension(size, size));
	}
	
	public ScaledButton(int size) {
		this.size = size;
		
		this.setPreferredSize(new Dimension(size, size)); 
		this.setMaximumSize(new Dimension(size, size));
	}
	
	public ScaledButton(ImageIcon icon, int size) {
	//Double the performance
		this.size = size;
		
		this.setIcon(icon);
		
		this.setPreferredSize(new Dimension(size, size)); 
		this.setMaximumSize(new Dimension(size, size));
	}

	public void setScaledIcon(URL url) {
		Image newImage = new ImageIcon(url).getImage();
		Image scaledImage = newImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
		ImageIcon copyTargetAssIcon = new ImageIcon(scaledImage);
 
		this.setIcon(copyTargetAssIcon);
	}
}
