package builtinPlugins;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import export.Message;
import export.MessageHandler;
import export.Plugin;


public class WelcomePlugin implements Plugin {
	private static final String name = "Welcome Plugin";
	private static final String LAUNCH_MESSAGE = name+" Launched.";
	private static final String STOP_MESSAGE = name + " Stopped";
	
	private JPanel renderPanel;
	private MessageHandler messageHandler;

	@Override
	public void Load(JPanel renderPanel, MessageHandler messageHandler) {
		messageHandler.sendMessage(new Message(name, LAUNCH_MESSAGE));
		this.messageHandler = messageHandler;
		this.renderPanel = renderPanel;
		renderPanel.setLayout(new BoxLayout(renderPanel, BoxLayout.Y_AXIS));
	}

	@Override
	public void Run() {
		JLabel one = new JLabel("Welcome! Select a plugin from the list at left in order to get started!");
		one.setAlignmentX(Component.CENTER_ALIGNMENT);
		renderPanel.add(one);
		JLabel two = new JLabel("To add plugins: copy them into the Plugins directory located");
		two.setAlignmentX(Component.CENTER_ALIGNMENT);
		renderPanel.add(two);
		JLabel three = new JLabel("in the startup directory!");
		three.setAlignmentX(Component.CENTER_ALIGNMENT);
		renderPanel.add(three);
		renderPanel.revalidate();
		renderPanel.repaint();
	}

	@Override
	public void Stop() {
		messageHandler.sendMessage(new Message(name, STOP_MESSAGE));
		renderPanel.setLayout(new FlowLayout());
	}

	@Override
	public String getIdentifier() {
		return name;
	}

}
