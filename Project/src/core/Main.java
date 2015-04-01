package core;

import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

import export.MessageHandler;

public class Main {
	public static final ImageIcon icon = new ImageIcon("./Images/plug.jpg");
	public static final String TITLE = "Midnight Oil Plugins";
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	private JFrame mainFrame;
	private JPanel listingPanel;
	private JPanel statusPanel;
	private JPanel renderPanel;
	
	public static Main program;
	
	public Main() {
		mainFrame = new JFrame();
		mainFrame.setIconImage(icon.getImage());
		mainFrame.setTitle(TITLE);
		mainFrame.setSize(WIDTH, HEIGHT);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.listingPanel = new JPanel();
		JScrollPane listingContainer = new JScrollPane(listingPanel);
		this.statusPanel = new JPanel();
		JScrollPane statusContainer = new JScrollPane(statusPanel);
		this.renderPanel = new JPanel();
		Dimension listingPanelDimension = new Dimension(WIDTH/4, HEIGHT);
		Dimension renderPanelDimension = new Dimension(3*WIDTH/4, 3*HEIGHT/4);
		Dimension statusPanelDimension = new Dimension(3*WIDTH/4, HEIGHT/4);
		
		MessageHandler mHandler = new MessageHandler(statusPanel, statusContainer);
		PluginHandler pHandler = new PluginHandler(listingPanel, renderPanel, mHandler);
		PluginLoader loader = new PluginLoader(pHandler, mHandler);
//		loader.loadPlugins();
		try {
			loader.registerWatcher();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loader.watchDirectory();
		
		Container contentPane = mainFrame.getContentPane();
		SpringLayout layout = new SpringLayout();
		contentPane.setLayout(layout);
		contentPane.add(listingContainer);
		contentPane.add(renderPanel);
		contentPane.add(statusContainer);
		
		//Set hte listing panel's size.
		listingContainer.setPreferredSize(listingPanelDimension);
		listingContainer.setMaximumSize(listingPanelDimension);
		
		//Set the listing panel to be as tall as the container, and set on the left.
		layout.putConstraint(SpringLayout.WEST, listingContainer, 0, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, listingContainer, 0, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, listingContainer, 0, SpringLayout.SOUTH, contentPane);
		
		//Set the main render panel to have a maximum size and preferred size
		renderPanel.setPreferredSize(renderPanelDimension);
		renderPanel.setMaximumSize(renderPanelDimension);
		
		//constrain the render panel to the top right, to the right of the listing panel.
		layout.putConstraint(SpringLayout.WEST, renderPanel, 0, SpringLayout.EAST, listingContainer);
		layout.putConstraint(SpringLayout.EAST, renderPanel, 0, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, renderPanel, 0, SpringLayout.NORTH, contentPane);
		
		//set the status panel to have maximum and preferred size.
		statusContainer.setPreferredSize(statusPanelDimension);
		statusContainer.setMaximumSize(statusPanelDimension);
		
		//constrain the status panel to the bottom right.
		layout.putConstraint(SpringLayout.WEST, statusContainer, 0, SpringLayout.EAST, listingContainer);
		layout.putConstraint(SpringLayout.SOUTH, statusContainer, 0, SpringLayout.SOUTH, contentPane);
		layout.putConstraint(SpringLayout.NORTH, statusContainer, 0, SpringLayout.SOUTH, renderPanel);
		layout.putConstraint(SpringLayout.EAST, statusContainer, 0, SpringLayout.EAST, contentPane);
		
		mainFrame.setVisible(true);
	}

	public static void main(String[] args) {
		program = new Main();
	}

}
