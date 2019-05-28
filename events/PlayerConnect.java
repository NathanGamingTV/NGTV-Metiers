package fr.ngtv.metiers.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.ngtv.metiers.Metiers;

public class PlayerConnect implements Listener {
	
	@EventHandler
	
	public void playerConnect(PlayerJoinEvent e) {
		
		Player p = e.getPlayer();
		
		UUID pU = p.getUniqueId();
		
		if (Metiers.getInstance().dbf.ifPlayerHasProfil(pU)) {
			
			Metiers.getInstance().dbf.loadProfil(pU);
			
		}
		
		else {
			
			Metiers.getInstance().dbf.createProfil(pU);
			
		}
		
	}

}
