package src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowModListListener implements ActionListener{

	private Controller controller;
	
	public ShowModListListener(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.openModListInput();
	}
}
