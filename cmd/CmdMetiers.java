package fr.ngtv.metiers.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ngtv.metiers.Metiers;

public class CmdMetiers implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if (sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if (args.length == 0) {
				
				Metiers.getInstance().dbf.openMenuMetiers(p);
				
			}
			
		}
		
		return false;
		
	}

}
