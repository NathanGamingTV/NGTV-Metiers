package fr.ngtv.metiers.dbf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ngtv.metiers.Metiers;
import fr.ngtv.metiers.objects.ItemForMetiersMenu;
import fr.ngtv.metiers.objects.Job;
import fr.ngtv.metiers.objects.JobStructure;
import fr.ngtv.metiers.objects.Profil;

public class DbAndFunctions {
	
	public Connection cnt;

	public void connectToDatabase() {
		
		try {
			
			cnt = DriverManager.getConnection("jdbc:mysql://localhost/serveurngtv", "root", "LUMANA045");
			
		} 
		
		catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
	}

	public void loadJobs() {
		
		try {
			
			PreparedStatement q = cnt.prepareStatement("SELECT * FROM metiers_structure");
			
			q.executeQuery();
			
			ResultSet rs = q.executeQuery();
			
			while (rs.next()) {
				
				JobStructure jS = new JobStructure();
				
				jS.name = rs.getString("name");
				
				jS.seuil = rs.getInt("basicseuil");
				
				jS.item = rs.getString("item");
				
				jS.emplacement = rs.getInt("emplacement");
				
				Metiers.getInstance().jobsStructure.add(jS);
				
			}
			
			q.close();
			
		} 
		
		catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
	}

	public boolean ifPlayerHasProfil(UUID pU) {
		
		try {
			
			PreparedStatement q = cnt.prepareStatement("SELECT id FROM metiers WHERE playerUUID = ?");
			
			q.setString(1, pU.toString());
			
			q.executeQuery();
			
			ResultSet rs = q.executeQuery();
			
			boolean b = rs.next();
			
			q.close();
			
			return b;
			
		} 
		
		catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return false;
		
	}

	public void loadProfil(UUID pU) {
		
		Profil p = new Profil();
		
		p.owner = pU;
		
		for (int i = 0; i < Metiers.getInstance().jobsStructure.size(); i++) {
			
			JobStructure jS = Metiers.getInstance().jobsStructure.get(i);
			
			try {
				
				PreparedStatement q = cnt.prepareStatement("SELECT * FROM metiers WHERE playerUUID = ? AND jobName = ?");
				
				q.setString(1, pU.toString());
				
				q.setString(2, jS.name);
				
				q.executeQuery();
				
				ResultSet rs = q.executeQuery();
				
				while (rs.next()) {
					
					Job cJ = new Job();
					
					cJ.name = rs.getString("jobName");
					
					cJ.level = rs.getInt("level");
					
					cJ.xpCurrent = rs.getDouble("xpCurrent");
					
					cJ.xpTotal = rs.getDouble("xpTotal");
					
					cJ.basicSeuil = getBasicSeuilForJobByName(cJ.name);
					
					p.jobList.add(cJ);
					
				}
				
				q.close();
				
			} 
			
			catch (SQLException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		p = loadMoneyForThisProfil(p);
		
		Metiers.getInstance().playerAndHisProfil.put(pU, p);
		
		Bukkit.getPlayer(pU).chat("/launch");
		
	}

	private Profil loadMoneyForThisProfil(Profil p) {
		
		try {
			
			PreparedStatement q = cnt.prepareStatement("SELECT argent FROM metiers_argent WHERE playerUUID = ?");
			
			q.setString(1, p.owner.toString());
			
			q.executeQuery();
			
			ResultSet rs = q.executeQuery();
			
			while (rs.next()) {
				
				p.money = rs.getDouble("argent");
				
			}
			
			q.close();
			
			return p;
			
		} 
		
		catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return null;
		
	}

	private double getBasicSeuilForJobByName(String jobName) {
		
		for (int i = 0; i < Metiers.getInstance().jobsStructure.size(); i++) {
			
			JobStructure jS = Metiers.getInstance().jobsStructure.get(i);
			
			if (jS.name.equals(jobName)) {
				
				return jS.seuil;
				
			}
			
		}
		
		return 0;
		
	}

	public void createProfil(UUID pU) {
		
		Profil p = new Profil();
		
		for (int i = 0; i < Metiers.getInstance().jobsStructure.size(); i++) {
			
			try {
				
				PreparedStatement q = cnt.prepareStatement("INSERT INTO metiers (playerUUID, jobName) VALUES(?, ?)");
				
				q.setString(1, pU.toString());
				
				q.setString(2, Metiers.getInstance().jobsStructure.get(i).name);
				
				q.execute();
				
				q.close();
				
				Job j = new Job();
				
				j.level = 1;
				
				j.xpTotal = 0.0;
				
				j.xpCurrent = 0.0;
				
				j.name = Metiers.getInstance().jobsStructure.get(i).name;
				
				j.basicSeuil = Metiers.getInstance().jobsStructure.get(i).seuil;
				
				p.jobList.add(j);
				
			} 
			
			catch (SQLException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		try {
			
			PreparedStatement a = cnt.prepareStatement("INSERT INTO metiers_argent (playerUUID) VALUES (?)");
			
			a.setString(1, pU.toString());
			
			a.execute();
			
			a.close();
			
		} 
		
		catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
		p.money = 0.0;
		
		Metiers.getInstance().playerAndHisProfil.put(pU, p);
		
		Bukkit.getPlayer(pU).chat("/launch");
		
	}

	public void addPointsForPlayer(UUID pU, double points, String jobName) {
		
		Profil p = Metiers.getInstance().playerAndHisProfil.get(pU);
		
		Job j = p.getJobByName(jobName);
		
		Job main = p.getJobByName("Main");
		
		double addMoney = (points / 10.0) + ((j.level / 100.0) * (points / 10.0));
		
		double addXp = points;
		
		j.addXp(addXp);
		
		main.addXp(addXp);
		
		p.addMoney(addMoney);
		
		p.replaceJobByName(main);
		
		p.replaceJobByName(j);
		
		createBossBarForPlayer(pU, p, jobName);
		
		Metiers.getInstance().playerAndHisProfil.replace(pU, p);
		
	}
	
	private void createBossBarForPlayer(UUID pU, Profil p, String jobName) {
		
		Job j = p.getJobByName(jobName);
		
		BossBar bb = createBossBarByJob(j);
		
		if (Metiers.getInstance().bossBar.containsKey(pU)) {
			
			BossBar lastbb = Metiers.getInstance().bossBar.get(pU);
			
			lastbb.setTitle(bb.getTitle());
			
			lastbb.setProgress(bb.getProgress());
			
			Metiers.getInstance().bossBar.replace(pU, lastbb);
			
			BukkitRunnable br = new BukkitRunnable() {
				
				int timer = 10;

				@Override
				
				public void run() {
					
					if (timer == 0) {
						
						cancel();
						
						lastbb.removePlayer(Bukkit.getPlayer(pU));
						
						Metiers.getInstance().bossBar.remove(pU);
						
					}
					
					timer--;
					
				}
				
			};
			
			br.runTaskTimer(Metiers.getInstance(), 0, 20);
			
		}
		
		else {
			
			bb.addPlayer(Bukkit.getPlayer(pU));
			
			Metiers.getInstance().bossBar.put(pU, bb);
			
			BukkitRunnable br = new BukkitRunnable() {
				
				int timer = 10;

				@Override
				
				public void run() {
					
					if (timer == 0) {
						
						cancel();
						
						bb.removePlayer(Bukkit.getPlayer(pU));
						
						Metiers.getInstance().bossBar.remove(pU);
						
					}
					
					timer--;
					
				}
				
			};
			
			br.runTaskTimer(Metiers.getInstance(), 0, 20);
			
		}
		
	}

	private BossBar createBossBarByJob(Job j) {
		
		BossBar bb = null;
		
		if (j.name.equals("miner")) bb = createBossBarForMiner(j);
		
		// if (j.name.equals("woodCutter")) bb = createBossBarForWoodCutter(j);
		
		// if (j.name.equals("fisher")) bb = createBossBarForFisher(j);
		
		// if (j.name.equals("farmer")) bb = createBossBarForFarmer(j);
		
		bb.setProgress(j.xpCurrent / j.getSeuilXpNextLevel());
		
		return bb;
		
	}

	private BossBar createBossBarForMiner(Job j) {
		
		BossBar bb = Bukkit.createBossBar("Métier mineur niveau " + j.level, BarColor.GREEN, BarStyle.SEGMENTED_6, new BarFlag[0]);
		
		return bb;
		
	}

	public void saveProfilPlayer(UUID pU) {
		
		Profil p = Metiers.getInstance().playerAndHisProfil.get(pU);
		
		for (int i = 0; i < Metiers.getInstance().jobsStructure.size(); i++) {
			
			String currentJobName = Metiers.getInstance().jobsStructure.get(i).name;
			
			Job j = p.getJobByName(currentJobName);
			
			try {
				
				PreparedStatement q = cnt.prepareStatement("UPDATE metiers SET xpTotal = ?, xpCurrent = ?, level = ? WHERE playerUUID = ? AND jobName = ?");
			
				q.setDouble(1, j.xpTotal);
				
				q.setDouble(2, j.xpCurrent);
				
				q.setInt(3, j.level);
				
				q.setString(4, pU.toString());
				
				q.setString(5, j.name);
				
				q.executeUpdate();
				
				q.close();
				
			} 
			
			catch (SQLException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		try {
			
			PreparedStatement a = cnt.prepareStatement("UPDATE metiers_argent SET argent = ? WHERE playerUUID = ?");
			
			a.setDouble(1, p.money);
			
			a.setString(2, pU.toString());
			
			a.executeUpdate();
			
			a.close();
			
		} 
		
		catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
	}

	public void saveProfilForAllPlayersOnline() {
		
		ArrayList<Player> l = new ArrayList<>();
		
		l.addAll(Bukkit.getOnlinePlayers());
				
		for (int i = 0; i < l.size(); i++) {
			
			UUID pU = l.get(i).getUniqueId();
			
			saveProfilPlayer(pU);
			
		}
		
	}

	public void loadProfilForAllPlayersOnline() {
		
		ArrayList<Player> l = new ArrayList<>();
		
		l.addAll(Bukkit.getOnlinePlayers());
		
		for (int i = 0; i < l.size(); i++) {
			
			Player p = l.get(i);
			
			UUID pU = p.getUniqueId();
			
			loadProfil(pU);
			
		}
		
		BukkitRunnable bR = new BukkitRunnable() {
			
			int timer = 10;

			@Override
			
			public void run() {
				
				if (timer == 0) {
					
					cancel();
					
					for (int i = 0; i < l.size(); i++) {
						
						Player p = l.get(i);
						
						p.chat("/launch");
						
					}
					
				}
				
				timer--;
				
			}
			
		};
		
		bR.runTaskTimer(Metiers.getInstance(), 0, 20);
		
	}

	public void openMenuMetiers(Player p) {
		
		UUID pU = p.getUniqueId();
		
		Inventory inv = Bukkit.createInventory(null, 9, "§1[Metiers] Menu");
		
		ArrayList<ItemForMetiersMenu> l = getItemsForMetiersMenu(pU);
		
		for (int i = 0; i < l.size(); i++) {
			
			int emplacement = l.get(i).emplacement;
			
			ItemStack it = l.get(i).it;
			
			inv.setItem(emplacement, it);
			
		}
		
		if (! Metiers.getInstance().invMenu.contains(pU)) Metiers.getInstance().invMenu.add(pU);
		
		p.openInventory(inv);
		
	}

	private ArrayList<ItemForMetiersMenu> getItemsForMetiersMenu(UUID pU) {
		
		ArrayList<ItemForMetiersMenu> l = new ArrayList<>();
		
		Profil p = Metiers.getInstance().playerAndHisProfil.get(pU);
		
		for (int i = 0; i < Metiers.getInstance().jobsStructure.size(); i++) {
			
			JobStructure jS = Metiers.getInstance().jobsStructure.get(i);
			
			Job j = p.getJobByName(jS.name);
			
			ItemForMetiersMenu truc = createItemForMetiersMenuWithJob(j, jS, pU);
			
			l.add(truc);
			
		}
		
		return l;
		
	}

	private ItemForMetiersMenu createItemForMetiersMenuWithJob(Job j, JobStructure jS, UUID pU) {
		
		ItemStack it = getItem(j, jS, pU);
		
		ItemForMetiersMenu truc = new ItemForMetiersMenu();
		
		truc.it = it;
		
		truc.emplacement = jS.emplacement;
		
		return truc;
		
	}

	private ItemStack getItem(Job j, JobStructure jS, UUID pU) {
		
		if (jS.item.equals("headPlayer")) {
			
			ItemStack it = new ItemStack(Material.PLAYER_HEAD, 1);
			
			SkullMeta itSM = (SkullMeta)it.getItemMeta();
			
			itSM.setOwner(Bukkit.getPlayer(pU).getName());
			
			itSM.setDisplayName("§1" + Bukkit.getPlayer(pU).getName());
			
			itSM.setLore(Arrays.asList("Niveau de joueur : " + j.level, "Seuil d'xp : " + (int) j.xpCurrent + " / " + (int) j.getSeuilXpNextLevel()));
			
			it.setItemMeta(itSM);
			
			return it;
			
		}
		
		else {
			
			Material m = null;
			
			if (jS.item.equals("pickaxe")) m = getPickaxeByLevelOfJob(j.level);
			
			if (jS.item.equals("axe")) m = getAxeByLevelOfJob(j.level);
			
			if (jS.item.equals("hoe")) m = getHoeByLevelOfJob(j.level);
			
			if (jS.item.equals("fisher_rod")) m = Material.FISHING_ROD;
			
			ItemStack it = new ItemStack(m, 1);
			
			ItemMeta itM = it.getItemMeta();
			
			itM.setDisplayName(getDisplayName(jS.item));
			
			itM.setLore(getLore(j));
			
			it.setItemMeta(itM);
			
			return it;
			
		}
		
	}

	private List<String> getLore(Job j) {
		
		return Arrays.asList("Niveau : " + j.level, "Seuil d'xp : " + (int)j.xpCurrent + " / " + (int)j.getSeuilXpNextLevel());
		
	}

	private String getDisplayName(String it) {
		
		String s = null;
		
		if (it.equals("pickaxe")) s = "§8Mineur";
			
		if (it.equals("headPlayer")) s = "§5Joueur";
				
		if (it.equals("axe")) s = "§2Bûcheron";
					
		if (it.equals("hoe")) s = "§aFermier";
						
		if (it.equals("fisher_rod")) s = "§bPêcheur";
			
		return s;
		
	}

	private Material getHoeByLevelOfJob(int level) {
		
		Material m = null;
		
		if (level <= 100) m = Material.DIAMOND_HOE;
		
		if (level <= 80) m = Material.GOLDEN_HOE;
		
		if (level <= 60) m = Material.IRON_HOE;
		
		if (level <= 40) m = Material.STONE_HOE;
		
		if (level <= 20) m = Material.WOODEN_HOE;
		
		return m;
		
	}

	private Material getAxeByLevelOfJob(int level) {
	
		Material m = null;
		
		if (level <= 100) m = Material.DIAMOND_AXE;
		
		if (level <= 80) m = Material.GOLDEN_AXE;
		
		if (level <= 60) m = Material.IRON_AXE;
		
		if (level <= 40) m = Material.STONE_AXE;
		
		if (level <= 20) m = Material.WOODEN_AXE;
		
		return m;
		
	}

	private Material getPickaxeByLevelOfJob(int level) {
		
		Material m = null;
		
		if (level > 100) m = Material.DIAMOND_PICKAXE;
		
		if (level <= 100) m = Material.DIAMOND_PICKAXE;
		
		if (level <= 80) m = Material.GOLDEN_PICKAXE;
		
		if (level <= 60) m = Material.IRON_PICKAXE;
		
		if (level <= 40) m = Material.STONE_PICKAXE;
		
		if (level <= 20) m = Material.WOODEN_PICKAXE;
		
		return m;
		
	}

}

