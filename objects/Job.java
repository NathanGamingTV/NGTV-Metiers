package fr.ngtv.metiers.objects;

import org.bukkit.boss.BossBar;

import fr.ngtv.metiers.Metiers;

public class Job {
	
	public String name;
	
	public int level;
	
	public double xpTotal, xpCurrent, basicSeuil;

	public void addXp(double addXp) {
		
		xpTotal = xpTotal + addXp;
		
		xpCurrent = xpCurrent + addXp;
		
		while (xpCurrent >= getSeuilXpNextLevel()) {
			
			double nextLevelSeuil = getSeuilXpNextLevel();
			
			xpCurrent = xpCurrent - nextLevelSeuil;
			
			level++;
			
		}
		
	}
	
	public double getSeuilXpNextLevel() {
		
		double currentSeuil = basicSeuil;
		
		for (int i = 0; i < level; i++) {
			
			currentSeuil = currentSeuil + (currentSeuil * 0.1);
			
		}
		
		return currentSeuil;
		
	}

}

