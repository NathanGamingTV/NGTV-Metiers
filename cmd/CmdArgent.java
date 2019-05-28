package fr.ngtv.metiers.cmd;

import java.text.DecimalFormat;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ngtv.metiers.Metiers;

public class CmdArgent implements CommandExecutor {

	@Override
	
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if (sender instanceof Player) {
			
			Player p = (Player)sender;
			
			UUID pU = p.getUniqueId();
			
			double m = Metiers.getInstance().playerAndHisProfil.get(pU).money;
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			p.sendMessage("[Metiers] Tu possèdes actuellement " + df.format(m) + " $ !");
			
		}
		
		return false;
		
	}

}
