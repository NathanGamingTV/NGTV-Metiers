package fr.ngtv.metiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ngtv.metiers.cmd.CmdArgent;
import fr.ngtv.metiers.cmd.CmdLaunch;
import fr.ngtv.metiers.cmd.CmdMetiers;
import fr.ngtv.metiers.dbf.DbAndFunctions;
import fr.ngtv.metiers.events.EventsManager;
import fr.ngtv.metiers.objects.JobStructure;
import fr.ngtv.metiers.objects.Profil;

public class Metiers extends JavaPlugin {
	
	public static Metiers instance;
	
	public static Metiers getInstance() {
		
		return instance;
		
	}
	
	public DbAndFunctions dbf;
	
	public void onEnable() {
		
		instance = this;
		
		super.onEnable();
		
		dbf = new DbAndFunctions();
		
		dbf.connectToDatabase();
		
		dbf.loadJobs();
		
		new EventsManager().registerEvents();
		
		dbf.loadProfilForAllPlayersOnline();
		
		getCommand("launch").setExecutor(new CmdLaunch());
		
		getCommand("metiers").setExecutor(new CmdMetiers());
		
		getCommand("argent").setExecutor(new CmdArgent());
		
	}
	
	public void onDisable() {
		
		super.onDisable();
		
		dbf.saveProfilForAllPlayersOnline();
		
	}
	
