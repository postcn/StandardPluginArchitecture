package export;

import javax.swing.JPanel;

import core.MessageHandler;

public interface Plugin {
	
	public void Load(JPanel renderPanel, MessageHandler messageHandler);
	public void Run();
	public void Stop();
	public String getIdentifier();

}
