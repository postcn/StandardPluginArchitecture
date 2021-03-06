package core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import export.Message;
import export.MessageHandler;
import export.Plugin;

public class PluginLoader implements Runnable{

	PluginHandler handler;
	MessageHandler messHandler;
	final File PLUGIN_FILE = new File("./Plugins"); //This is the directory Location that we are using.
	public static final String NAME = "Plugin Loader";
	private WatchService watcher;
	private Path dir;

	public PluginLoader(PluginHandler handler, MessageHandler messHandler) {
		this.handler = handler;
		this.messHandler = messHandler;
		
		if (!PLUGIN_FILE.exists()) {
			messHandler.sendSystemMessage(new Message(NAME, "Appears to be new run from current location. Creating Directory: Plugins"));
			messHandler.sendSystemMessage(new Message(NAME, "To Add Plugins: drop them into the newly created Plugins directory."));
			PLUGIN_FILE.mkdir();
		}
	}
	
	public void registerWatcher() throws IOException{
		this.dir = PLUGIN_FILE.toPath();
		this.watcher = FileSystems.getDefault().newWatchService();
		this.dir.register(watcher,StandardWatchEventKinds.ENTRY_CREATE);
	}
	
	public void loadPlugins() {
		File[] files = PLUGIN_FILE.listFiles();
		if (files == null) {
			messHandler.sendSystemMessage(new Message(NAME, "Critical Error: Plugins directory is missing from current folder"));
			return;
		}
		for (int i = 0; i < files.length; i++) {
			try {
				loadAndScanJar(files[i]);
			} catch (ClassNotFoundException | IOException e) {
				messHandler.sendSystemMessage(new Message(NAME, "Error occurred while processing file: " + files[i].getName()));
				messHandler.sendSystemMessage(new Message(NAME, "An Error Occurred while loading a possible jar file: " + e.getLocalizedMessage()));
			}
		}

	}

	public void loadAndScanJar(File file) throws ClassNotFoundException, ZipException, IOException {
		LinkedList<Class<?>> pluginClasses = new LinkedList<>();
		JarFile jarFile = new JarFile(file);
		Enumeration<?> jarEntries = jarFile.entries();
		URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() + "!/") };
		URLClassLoader classLoader = URLClassLoader.newInstance(urls);
		String suffix=".class";
		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
			if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(suffix)) {
				continue;
			}
			String className = jarEntry.getName().substring(0, jarEntry.getName().length() - suffix.length());
			className = className.replace('/', '.');
			Class<?> clazz = classLoader.loadClass(className);
			for (Class<?> inter : clazz.getInterfaces()) {
				if (inter.equals(Plugin.class)) {
					pluginClasses.add(clazz);
					break;
				}
			}
		}
		for (Class<?> c : pluginClasses) {
			try {
				handler.register((Plugin) c.newInstance());
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
		jarFile.close();
	}

	@Override
	public void run() {
		for(;;){
			WatchKey key;
			try {
		        key = this.watcher.take();
		    } catch (InterruptedException x) {
		        return;
		    }
	
		    for (WatchEvent<?> event: key.pollEvents()) {
		        WatchEvent.Kind<?> kind = event.kind();
	
		        // This key is registered only for ENTRY_CREATE events, but an OVERFLOW event can occur regardless if events are lost or discarded.
		        if (kind == StandardWatchEventKinds.OVERFLOW) {
		            continue;
		        }
	
		        // The filename is the context of the event.
		        @SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>)event;
		        Path filename = ev.context();
	
		        Path child = dir.resolve(filename);
				if (child.getFileName().toString().endsWith(".jar")) {
					messHandler.sendSystemMessage(new Message(NAME, "Dynamically adding jar file added after initial startup"));
					try {						
						loadAndScanJar(child.toFile());
						
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}else{
					messHandler.sendSystemMessage(new Message(NAME, "New File: ("+filename+") is not a jar file"));
				    continue;
				}
		    }
	
		    // Reset the key -- this step is critical if you want to receive further watch events.  If the key is no longer valid,
		    // the directory is inaccessible so exit the loop.
		    boolean valid = key.reset();
		    if (!valid) {
		        break;
		    }
        }
	}
}
