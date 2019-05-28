package fr.ngtv.metiers.objects;

import java.util.ArrayList;
import java.util.UUID;

public class Profil {
	
	public UUID owner;
	
	public double money;
	
	public ArrayList<Job> jobList = new ArrayList<>();

	public Job getJobByName(String jobName) {
		
		for (int i = 0; i < jobList.size(); i++) {
			
			Job j = jobList.get(i);
			
			if (j.name.equals(jobName)) {
				
				return j;
				
			}
			
		}
		
		return null;
		
	}

	public void addMoney(double addMoney) {
		
		money = money + addMoney;
		
	}

	public void replaceJobByName(Job j) {
		
		for (int i = 0; i < jobList.size(); i++) {
			
			Job cJ = jobList.get(i);
			
			if (cJ.name.equals(j.name)) {
				
				jobList.set(i, j);
				
			}
			
		}
		
	}

}
