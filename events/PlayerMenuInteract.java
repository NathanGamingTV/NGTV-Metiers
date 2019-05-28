package fr.ngtv.metiers.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.ngtv.metiers.Metiers;

public class PlayerMenuInteract implements Listener {
	
	@EventHandler
	
	public void playerMenuInteract(InventoryClickEvent e) {
		
		Player p = (Player)e.getWhoClicked();
		
		UUID pU = p.getUniqueId();
		
		if (Metiers.getInstance().invMenu.contains(pU)) {
			
			e.setCancelled(true);
			
		}
		
	}

}
