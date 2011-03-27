package com.ementalo.signprotect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import com.ementalo.helpers.PropertiesFile;

public class SignProtect extends JavaPlugin {

	private SignProtectBlockListener listener =null;
	private SignProtectPlayerListener plistener = null;
	private SignProtectData spData = null;
	static Logger log = Logger.getLogger("Minecraft");
	private String name = "SignProtect";
	private String version = "0.5";
	static String[] groupList;
	static String dataType;
	static String SQLdriver = "com.mysql.jdbc.Driver";
	static String SQLusername = "root";
	static String SQLpassword = "root";
	static String SQLdb = "jdbc:mysql://localhost:3306/minecraft";
	static String strBaseFolder = "plugins" + "/"+ "SignProtect";
	static String sqlIte = "jdbc:sqlite:"+ strBaseFolder +  "/"+ "SignProtect.db";
	static boolean protectBlockUnderneath;
	static String signAdmin;
    static File baseFolder = new File(strBaseFolder);
    static String propertiesName = File.separator + "SignProtect.properties";
	


	public SignProtect()
	{

	}
	
	// TODO Auto-generated constructor stub

	public void onEnable() {

		log.log(Level.INFO, "[" + name + "]" + " [" + version +"]" + " enabled");
		loadSettings(baseFolder);
		listener = new SignProtectBlockListener(this);
		plistener = new SignProtectPlayerListener(this);
		spData = new SignProtectData();
		spData.CreateSqlTable();
		
		registerEvents();
	}

	public void ondisable() {
		log.log(Level.INFO, "SignProtect disabled");
	}

	public static void loadSettings(File pFolder) {

		
		if (!pFolder.exists()) {
			pFolder.mkdirs();
		}
		if (!new File(pFolder , "SignProtect.properties").exists()) {
			FileWriter writer = null;
			try {
				writer = new FileWriter(pFolder + propertiesName );
				writer.write("========================================\r\n");
				writer.write("          SignProtect Properties        \r\n");
				writer.write("========================================\r\n");
				writer.write("\r\n");
			} catch (Exception e) {
				log.log(Level.SEVERE,
						"Exception while creating SignProtect.properties", e);
				try {
					if (writer != null)
						writer.close();
				} catch (IOException ex) {
					log.log(Level.SEVERE,
							"Exception while closing writer for SignProtect.properties",
							ex);
				}
			} finally {
				try {
					if (writer != null)
						writer.close();
				} catch (IOException e) {
					log.log(Level.SEVERE,
							"Exception while closing writer for SignProtect.properties",
							e);
				}
			}
		}
		PropertiesFile properties = new PropertiesFile(pFolder.getPath()
				+ propertiesName);
		try {
			SQLusername = properties.getString("SQLuser", "root");
			SQLpassword = properties.getString("SQLpass", "root");
			SQLdb = properties.getString("SQLdb",
					"jdbc:mysql://localhost:3306/minecraft");
			protectBlockUnderneath = properties.getBoolean(
					"protectBlockUnderneath", true);
			dataType = properties.getString("dataType", "sqlite");
			signAdmin = properties.getString("signAdmin", "name,name,name");
			groupList = signAdmin.toLowerCase().split(",");

		} catch (Exception e) {
			log.log(Level.SEVERE,
					"Exception while reading from SignProtect.properties", e);
		}

	}

	public void registerEvents() {

		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK,
				this.listener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_RIGHTCLICKED,
				this.listener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_ITEM,
				this.plistener, Event.Priority.Normal, this);

	}
	public static boolean canUseCommand(Player player) {
		{

			if (Arrays.asList(groupList).contains(
					player.getName().toLowerCase())) {
				return true;
			} else {
				return false;
			}
		}
	}
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}
}
