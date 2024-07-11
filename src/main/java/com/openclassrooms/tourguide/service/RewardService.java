package com.openclassrooms.tourguide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.springframework.stereotype.Service;

import com.openclassrooms.tourguide.constant.MilesRatio;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.service.contracts.IRewardService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;

@Service
public class RewardService implements IRewardService {

	// proximity in miles
	private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;

	public RewardService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}

	@Override
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	@Override
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	@Override
	public double getDistance(Location loc1, Location loc2) {
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);

		double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = MilesRatio.STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}

	@Override
	public int getRewards(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	@Override
	public User calculateRewards(User user) {
		CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>( user.getVisitedLocations());
		List<Attraction> attractions = gpsUtil.getAttractions();

		for (VisitedLocation visitedLocation : userLocations) {
			for (Attraction attraction : attractions) {
				if (user.getRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if (nearAttraction(visitedLocation, attraction)) {
						user.addReward(new Reward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}
		return user;
	}

	@Override
	public List<User> calculateAllUsersRewards(List<User> users) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(100);
		List<User> usersRewards = new ArrayList<>();
		for (User user : users) {
			executor.execute(() -> {
				User user1 = calculateRewards(user);
				usersRewards.add(user1);
			});
		}
		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		return usersRewards;
	}

		@Override
		public boolean isWithinAttractionProximity (Attraction attraction, Location location){
			return getDistance(attraction, location) > attractionProximityRange ? false : true;
		}

		private boolean nearAttraction (VisitedLocation visitedLocation, Attraction attraction){
			return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
		}

		private int getRewardPoints (Attraction attraction, User user){
			return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
		}
	}