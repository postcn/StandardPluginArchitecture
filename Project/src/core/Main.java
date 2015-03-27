package core;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class Main {
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	private JFrame mainFrame;
	private JPanel listingPanel;
	private JPanel statusPanel;
	private JPanel renderPanel;
	
	public static Main program;
	
	public Main() {
		mainFrame = new JFrame();
		mainFrame.setSize(WIDTH, HEIGHT);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		mainFrame.setLayout(new GridBagLayout());
		this.listingPanel = new JPanel();
		this.statusPanel = new JPanel();
		this.renderPanel = new JPanel();
		renderPanel.setPreferredSize(new Dimension(3*WIDTH/4, 3*HEIGHT/4));
		statusPanel.setPreferredSize(new Dimension(3*WIDTH/4, HEIGHT/4));
		
		MessageHandler mHandler = new MessageHandler(statusPanel);
		PluginHandler pHandler = new PluginHandler(listingPanel, renderPanel, mHandler);
		
		
		//Add them in position
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.ipady = HEIGHT;
		c.ipadx = WIDTH/4;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 4;
		mainFrame.add(new JScrollPane(listingPanel), c);
		
		c.ipady = 0;
		c.ipadx = 0;
//		c.ipady= 3*HEIGHT/4;
//		c.ipadx = 3*WIDTH/4;
		c.gridx = 1;
		c.gridheight = 3;
		c.gridwidth = 2;
		mainFrame.add(renderPanel, c);
		
//		c.ipady = HEIGHT/4;
		c.gridy = 3;
		c.gridheight = 1;
		mainFrame.add(new JButton("Status Panel"), c);
		
		mainFrame.setVisible(true);
	}

	public static void main(String[] args) {
		program = new Main();
	}

}
