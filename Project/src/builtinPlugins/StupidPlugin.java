package builtinPlugins;

import javax.swing.JLabel;
import javax.swing.JPanel;

import core.Message;
import core.MessageHandler;
import core.Plugin;


public class StupidPlugin implements Plugin {
	private static final String name = "StupidPlugin";
	private static final String LAUNCH_MESSAGE = "Stupid Plugin Launched.";
	private static final String STOP_MESSAGE = "Stupid Plugin Stopped";
	
	private JPanel renderPanel;
	private MessageHandler messageHandler;

	@Override
	public void Load(JPanel renderPanel, MessageHandler messageHandler) {
		messageHandler.sendMessage(new Message(name, LAUNCH_MESSAGE));
		this.messageHandler = messageHandler;
		this.renderPanel = renderPanel;
	}

	@Override
	public void Run() {
		renderPanel.add(new JLabel("Duh. This is stupid. I don't do anything."));
		renderPanel.revalidate();
		renderPanel.repaint();
	}

	@Override
	public void Stop() {
		messageHandler.sendMessage(new Message(name, STOP_MESSAGE));
	}

	@Override
	public String getIdentifier() {
		return name;
	}

}
