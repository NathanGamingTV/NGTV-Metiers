package fr.ngtv.metiers.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import fr.ngtv.metiers.Metiers;

public class PlayerCloseMenu implements Listener {
	
	@EventHandler
	
	public void playerCloseInventory(InventoryCloseEvent e) {
		
		Player p = (Player)e.getPlayer();
		
		UUID pU = p.getUniqueId();
		
		if (Metiers.getInstance().invMenu.contains(pU)) Metiers.getInstance().invMenu.remove(pU);
		
	}

}
