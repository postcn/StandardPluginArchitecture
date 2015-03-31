package core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import export.Plugin;
import sun.reflect.Reflection;

public class PluginLoader {
	
	//TODO: Implement this class.
	PluginHandler handler;
    final File PLUGINFILE = new File ("../Plugins");

	public PluginLoader(PluginHandler handler) {
		this.handler=handler;
	}
	public void loadPlugins(){
   
       File[] files = PLUGINFILE.listFiles();
       for(int i=0;i<files.length;i++){
    	   System.out.println(files[i].getName());
       }
       
       try {
		loadAndScanJar(files[0]);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Map<String, List<Class<?>>> loadAndScanJar(File jarFile) throws ClassNotFoundException, ZipException, IOException {		
	    // Load the jar file into the JVM
	    // You can remove this if the jar file already loaded.
//	    super.addURL(jarFile.toURI().toURL());

	    Map<String, List<Class<?>>> classes = new HashMap<String, List<Class<?>>>();

//	    List<Class<?>> interfaces = new ArrayList<Class<?>>();
//	    List<Class<?>> clazzes = new ArrayList<Class<?>>();
//	    List<Class<?>> enums = new ArrayList<Class<?>>();
//	    List<Class<?>> annotations = new ArrayList<Class<?>>();

//	    classes.put("interfaces", interfaces);
//	    classes.put("classes", clazzes);
//	    classes.put("annotations", annotations);
//	    classes.put("enums", enums);

	    // Count the classes loaded
	    int count = 0;

	    // Your jar file
	    JarFile jar = new JarFile(jarFile);
		
	    // Getting the files into the jar
	    Enumeration<? extends JarEntry> enumeration = jar.entries();

	    // Iterates into the files in the jar file
	    while (enumeration.hasMoreElements()) {
	        ZipEntry zipEntry = enumeration.nextElement();

	        // Is this a class?
	        if (zipEntry.getName().endsWith(".class")) {
	        	getstuff(zipEntry);
//	            // Relative path of file into the jar.
//	            String className = zipEntry.getName();
//
//	            // Complete class name
//	            className = className.replace(".class", "").replace("/", ".");
//	            System.out.println("classname: " + className);
//	            // Load class definition from JVM
////	            final Class<?> myLoadedClass = Class.forName(className);
////	            Reflection reflections = new Reflection(ClasspathHelper.forPackage("your.root.package"), new SubTypesScanner());
////	            Set<Class<? extends Plugin>> implementingTypes = ((Object) reflections).getSubTypesOf(Plugin.class);
//	            
////	            ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
////	            Class<?> clazz  = myClassLoader.loadClass(className);
//	            
////	            System.out.println("implements: " + clazz.getGenericInterfaces());
////	            Class<?> clazz = this.loadClass(className);
////
////	            try {
////	                // Verify the type of the "class"
////	                if (clazz.isInterface()) {
////	                    interfaces.add(clazz);
////	                } else if (clazz.isAnnotation()) {
////	                    annotations.add(clazz);
////	                } else if (clazz.isEnum()) {
////	                    enums.add(clazz);
////	                } else {
////	                    clazzes.add(clazz);
////	                }
////
////	                count++;
////	            } catch (ClassCastException e) {
////
////	            }
	        }
	    }

	    System.out.println("Total: " + count);

	    return classes;
	}
	public void getstuff(ZipEntry jarEntry){
//		if (jarEntry.getName().endsWith(".class")) {
//
//            String classname = jarEntry.getName().replaceAll("/", "\\.");
//
//            classname = classname.substring(0, classname.length() - 6);
////            if (!classname.contains("$")) {
//            ClassLoader myLoader = ClassLoader.getSystemClassLoader();
//                try {
////        			System.out.println("hello: "+classname);
//
//                    final Class<?> myLoadedClass = Class.forName(classname,true, myLoader);
//                    
//                    System.out.println("stuff: "+myLoadedClass.isInstance(Plugin.class));
//                    if (myLoadedClass.isAssignableFrom(Plugin.class)) {
//                        System.out.println("I GOT ONE: " + classname);
//                    }
//                } catch (final ClassNotFoundException e) {
//                	System.out.println(e);
//
//                } 
//        }
	}
	
}
