package core;

import javax.swing.JLabel;
import javax.swing.JPanel;

import export.Message;

public class MessageHandler {
	
	public MessageHandler(JPanel messagePanel) {
		//TODO: implement this.
		messagePanel.add(new JLabel("This is a stub for the message panel implementation."));
		messagePanel.invalidate();
	}
	
	public void sendMessage(Message m) {
		//TODO implement this class beyond being a stub.
	}

}
