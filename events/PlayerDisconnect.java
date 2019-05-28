package fr.ngtv.metiers.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.ngtv.metiers.Metiers;

public class PlayerDisconnect implements Listener {
	
	@EventHandler
	
	public void playerDisconnect(PlayerQuitEvent e) {
		
		Player p = e.getPlayer();
		
		UUID pU = p.getUniqueId();
		
		Metiers.getInstance().dbf.saveProfilPlayer(pU);
		
	}

}
