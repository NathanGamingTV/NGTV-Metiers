package fr.ngtv.metiers.events;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.ngtv.metiers.Metiers;

public class EventsManager {

	public void registerEvents() {
		
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new PlayerConnect(), Metiers.getInstance());
		
		pm.registerEvents(new PlayerDisconnect(), Metiers.getInstance());
		
		pm.registerEvents(new PlayerBreakBlock(), Metiers.getInstance());
		
		pm.registerEvents(new PlayerMenuInteract(), Metiers.getInstance());
		
		pm.registerEvents(new PlayerCloseMenu(), Metiers.getInstance());
		
	}
	
	

}
