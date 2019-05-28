package fr.ngtv.metiers.events;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.ngtv.metiers.Metiers;

public class PlayerBreakBlock implements Listener {
	
	@EventHandler
	
	public void playerBreakBlock(BlockBreakEvent e) {
		
		Player p = e.getPlayer();
		
		UUID pU = p.getUniqueId();
		
		Block b = e.getBlock();
		
		Material m = b.getType();
		
		if (p.getGameMode() == GameMode.SURVIVAL) {
			
			// Metier mineur
			
			if (m.equals(Material.STONE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 10.0, "miner");
			
			if (m.equals(Material.GRANITE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 15.0, "miner");
			
			if (m.equals(Material.ANDESITE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 15.0, "miner");
			
			if (m.equals(Material.DIORITE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 15.0, "miner");
			
			if (m.equals(Material.COAL_ORE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 30.0, "miner");
			
			if (m.equals(Material.IRON_ORE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 65.0, "miner");
			
			if (m.equals(Material.LEGACY_REDSTONE_ORE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 110.0, "miner");
			
			if (m.equals(Material.LAPIS_ORE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 140.0, "miner");
			
			if (m.equals(Material.GOLD_ORE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 200.0, "miner");
			
			if (m.equals(Material.DIAMOND_ORE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 500.0, "miner");
			
			if (m.equals(Material.EMERALD_ORE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 2500.0, "miner");
			
			if (m.equals(Material.COBBLESTONE)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 900000.0, "miner");
			
			// Metier bucheron
			
			if (m.equals(Material.ACACIA_LOG)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 35.0, "woodCutter");
			
			if (m.equals(Material.BIRCH_LOG)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 35.0, "woodCutter");
			
			if (m.equals(Material.DARK_OAK_LOG)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 35.0, "woodCutter");
			
			if (m.equals(Material.JUNGLE_LOG)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 35.0, "woodCutter");
			
			if (m.equals(Material.OAK_LOG)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 35.0, "woodCutter");
			
			if (m.equals(Material.SPRUCE_LOG)) Metiers.getInstance().dbf.addPointsForPlayer(pU, 35.0, "woodCutter");
			
		}
		
	}

}

