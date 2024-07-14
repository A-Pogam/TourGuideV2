package com.openclassrooms.tourguide.service.contracts;

import com.openclassrooms.tourguide.model.User;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;

import java.util.List;

public interface IRewardService {

	public void setProximityBuffer(int proximityBuffer);
	public void setDefaultProximityBuffer();
	public double getDistance(Location loc1, Location loc2);
	
	public User calculateRewards(User user);
	public boolean isWithinAttractionProximity(Attraction attraction, Location location);

	public int getRewards (Attraction attraction, User user);

	public List<User> calculateAllUsersRewards(List<User> users) throws Exception;



}