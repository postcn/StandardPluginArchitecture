package core;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

import export.Message;
import export.MessageHandler;
import export.Plugin;
import builtinPlugins.WelcomePlugin;


public class PluginHandler {
	public static final String SYSTEM_NAME = "Plugin Handler";
	public static final String SYSTEM_MESSAGE_FORMAT = "Plugin %s registered and available for launch.";
	
	private Plugin currentPlugin;
	private JPanel pluginPanel;
	
	private ArrayList<Plugin> plugins;
	private JList<String> list;
	private DefaultListModel<String> model;
	private MessageHandler messageHandler;
	
	public PluginHandler(JPanel renderer, JPanel pluginPanel, MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
		this.plugins = new ArrayList<>();
		model = new DefaultListModel<>();
		list = new JList<>(model);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<?> list = (JList<?>) evt.getSource();
				int index = list.locationToIndex(evt.getPoint());
				launchPlugin(index);
			}
		});
		
		renderer.add(list);
		this.pluginPanel = pluginPanel;
		addDefaultPlugins();
	}
	
	public void launchPlugin(int index) {
		if (currentPlugin != null) {
			messageHandler.sendSystemMessage(new Message(SYSTEM_NAME, "Stopping Current Plugin."));
			currentPlugin.Stop();
			pluginPanel.removeAll();
		}
		messageHandler.sendSystemMessage(new Message(SYSTEM_NAME, "Launching New Plugin."));
		Plugin toLaunch = plugins.get(index);
		toLaunch.Load(pluginPanel, messageHandler);
		currentPlugin = toLaunch;
		toLaunch.Run();
	}
	
	public void register(Plugin newPlugin) {
		this.plugins.add(newPlugin);
		model.addElement(newPlugin.getIdentifier());
		messageHandler.sendSystemMessage(new Message(SYSTEM_NAME, String.format(SYSTEM_MESSAGE_FORMAT, newPlugin.getIdentifier())));
		list.repaint();
	}
	
	private void addDefaultPlugins() {
		register(new WelcomePlugin());
		launchPlugin(0);
	}

}
