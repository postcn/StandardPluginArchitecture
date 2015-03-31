package core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import export.Plugin;

public class PluginLoader {

	// TODO: Make this class listen for jar files dropped in during runtime.
	PluginHandler handler;
	final File PLUGIN_FILE = new File("../Plugins"); //This is the directory Location that we are using.

	public PluginLoader(PluginHandler handler) {
		this.handler = handler;
	}

	public void loadPlugins() {

		File[] files = PLUGIN_FILE.listFiles();
		for (int i = 0; i < files.length; i++) {
			try {
				loadAndScanJar(files[i]);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void loadAndScanJar(File file) throws ClassNotFoundException,
			ZipException, IOException {
		LinkedList<Class<?>> pluginClasses = new LinkedList<>();
		JarFile jarFile = new JarFile(file);
		Enumeration<?> e = jarFile.entries();
		URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() + "!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		while (e.hasMoreElements()) {
			JarEntry je = (JarEntry) e.nextElement();
			if (je.isDirectory() || !je.getName().endsWith(".class")) {
				continue;
			}
			// -6 because of .class
			String className = je.getName().substring(0,
					je.getName().length() - 6);
			className = className.replace('/', '.');
			Class<?> c = cl.loadClass(className);
			for (Class<?> inter : c.getInterfaces()) {
				if (inter.equals(Plugin.class)) {
					pluginClasses.add(c);
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

}
