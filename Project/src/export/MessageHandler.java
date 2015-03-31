package export;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class MessageHandler {
	
	private JPanel renderingPanel;
	private JScrollPane scrollContainer;
	
	public MessageHandler(JPanel messagePanel, JScrollPane scrollContainer) {
		renderingPanel = messagePanel;
		this.scrollContainer = scrollContainer;
		scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		renderingPanel.setLayout(new BoxLayout(renderingPanel, BoxLayout.Y_AXIS));
	}
	
	public void sendMessage(Message m) {
		String toDisplay = String.format("<html><i>%s:</i> %s</html>", m.getSender(), m.getMessage());
		renderingPanel.add(new JLabel(toDisplay));
		renderingPanel.revalidate();
		renderingPanel.repaint();
		autoScroll();
	}
	
	public void sendSystemMessage(Message m) {
		String toDisplay = String.format("<html><font color='red'><i>%s:</i></font> %s</html>", m.getSender(), m.getMessage());
		renderingPanel.add(new JLabel(toDisplay));
		renderingPanel.revalidate();
		renderingPanel.repaint();
		autoScroll();
	}
	
	private void autoScroll() {
		scrollContainer.revalidate();
		SwingUtilities.invokeLater
		(
		  new Runnable()
		  {
		    public void run()
		    {
		    	scrollContainer.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
		    }
		  }
		);
		scrollContainer.repaint();
	}

}
