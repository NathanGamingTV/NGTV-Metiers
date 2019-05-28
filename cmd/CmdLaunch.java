package fr.ngtv.metiers.cmd;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ngtv.metiers.Metiers;

public class CmdLaunch implements CommandExecutor {

	@Override
	
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if (sender instanceof Player) {
			
			Player p = (Player)sender;
			
			UUID pU = p.getUniqueId();
			
			if (! Metiers.getInstance().isLaunch.contains(pU)) {
				
				Metiers.getInstance().isLaunch.add(pU);
				
				BukkitRunnable bR = new BukkitRunnable() {
					
					int timer = 10;

					@Override
					
					public void run() {
						
						timer--;
						
						if (timer == 0) {
							
							if (Bukkit.getPlayer(pU) != null && Bukkit.getPlayer(pU).isOnline()) {
								
								Metiers.getInstance().dbf.saveProfilPlayer(pU);
								
								timer = 10;
								
							}
							
							else {
								
								cancel();
								
								Metiers.getInstance().isLaunch.remove(pU);
								
							}
							
						}
						
					}
					
				};
				
				bR.runTaskTimer(Metiers.getInstance(), 0, 20);
				
			}
			
			else {
				
				p.sendMessage("[Metiers] Commande impossible !");
				
			}
			
		}
		
		return false;
		
	}

}

