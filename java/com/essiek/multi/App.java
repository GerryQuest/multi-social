package com.essiek.multi;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
/*import java.io.IOException;
import java.util.HashMap;
import java.util.Map;*/

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.UIManager;
/*import javax.swing.plaf.synth.SynthLookAndFeel;*/










import org.cef.OS;










/*import com.couchbase.lite.Context;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.JavaContext;
import com.couchbase.lite.Manager;
import com.couchbase.lite.util.Log;*/
import com.essiek.multi.gui.*; 
import com.essiek.multi.users.UserManager;

/*import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaClassyLookAndFeel;*/

/*import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import com.jtattoo.plaf.smart.SmartLookAndFeel;
import com.jtattoo.plaf.fast.FastLookAndFeel;
import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;*/
import com.github.mustachejava.DefaultMustacheFactory;
/*import com.github.mustachejava.Mustache;*/
import com.github.mustachejava.MustacheFactory;

public class App {
//	private Manager manager;
	//private static Context mContext;
//	private Database database;
	
    public static void main(String[] args) {
    	/*SynthLookAndFeel synth = new SynthLookAndFeel();*/

    	Color highlight = new Color(255, 153, 0);
    	UIManager.put("MenuItem.selectionBackground", highlight);
    	UIManager.put("MenuBar.selectionBackground", highlight);
    	UIManager.put("Menu.selectionBackground", highlight);
    	
    	Color bg = new Color(69, 69, 69);
    	UIManager.put("OptionPane.background", bg);
    	 UIManager.put("Panel.background", bg);
    	 UIManager.put("Panel.foreground", highlight);
    	
    	
    	/*try 
        {
          UIManager.setLookAndFeel(new FastLookAndFeel());
    		synth.load(CustomPainter.class.getResourceAsStream("synth.xml"), CustomPainter.class);
    		UIManager.setLookAndFeel(synth);
        } 
        catch (Exception e) 
        {
          e.printStackTrace();
        }*/
    	
    	/*File file = new File("src/main/resources/index.html");*/
//    	File file = new File(App.class.getClassLoader().getResource("index.html").getFile());
//    	File file = new File(App.class.getClassLoader().getResource("resources/index.html").getFile());
    	String jarPath;
    	File parent;
    	try {
			jarPath = App.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			parent = new File (new File (jarPath).getParent() + 
	    			"/resources/index.html");
//			System.out.println(file.exists());
	    	System.out.println(System.getProperty("user.dir"));
	    	/*System.out.println("Path " + file.getPath());*/
	    	System.out.println(parent.getPath());
	    	MustacheFactory mf = new DefaultMustacheFactory();
	    	TheFrame mainFrame = new TheFrame(parent.getPath(), OS.isLinux(), false);
//	    	TheFrame mainFrame = new TheFrame(file.getPath(), OS.isLinux(), false);
	    	UserManager manager = new UserManager();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	/*App app = new App();
    	app.create();*/
    	
    }
    
    
    public void create() {
    	/*System.out.println(getFilesDir());*/
    	/*try {
    		manager = new Manager(new JavaContext("data"), Manager.DEFAULT_OPTIONS);
    		Manager.enableLogging("CBLite", Log.WARN);
    		database = manager.getDatabase("multisol-database");
    		Map<String, Object> properties = new HashMap<String, Object>();
    		properties.put("type", "list");
    		properties.put("title", "NEED A Title");
    		properties.put("created_at", "8PM");
    		properties.put("title", "Little, Big");
    		properties.put("author", "John Crowley");
    		properties.put("published", 1982);
    		
    		Document document = database.createDocument();
    		document.putProperties(properties);
    		
    		
    		// Get Database and print title property
    		Document doc = database.getDocument(document.getId());
    		System.out.println(doc.getProperty("title"));
    		
    		
    		
    	} catch (IOException | CouchbaseLiteException e) {
    		//Log.e("CBLite", "Cannot create manager instance", e);
    		e.printStackTrace();
    		System.out.println(e);
    		return;
    	}
    	*/
    }
}
