package core;

import javax.swing.JPanel;

public interface Plugin {
	
	public void Load(JPanel renderPanel, MessageHandler messageHandler);
	public void Run();
	public void Stop();
	public String getIdentifier();

}
