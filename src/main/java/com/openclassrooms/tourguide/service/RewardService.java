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

/**
 * Service for managing and calculating rewards for users based on their visited locations
 * and proximity to attractions. This service interacts with external GPS and reward systems
 * to provide users with the appropriate rewards.
 */
@Service
public class RewardService implements IRewardService {

	/**
	 * Default proximity buffer in miles.
	 */
	private int defaultProximityBuffer = 10;

	/**
	 * Current proximity buffer in miles for determining if a location is near an attraction.
	 */
	private int proximityBuffer = defaultProximityBuffer;

	/**
	 * Range in miles within which an attraction is considered.
	 */
	private int attractionProximityRange = 200;

	/**
	 * The GPS utility for retrieving location data.
	 */
	private final GpsUtil gpsUtil;

	/**
	 * The reward central system for managing reward points.
	 */
	private final RewardCentral rewardsCentral;

	/**
	 * Constructs a RewardService with the specified GPS utility and reward central system.
	 *
	 * @param gpsUtil The GPS utility for retrieving location data.
	 * @param rewardCentral The reward central system for managing reward points.
	 */
	public RewardService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}

	/**
	 * Sets the proximity buffer in miles for determining if a location is near an attraction.
	 *
	 * @param proximityBuffer The new proximity buffer in miles.
	 */
	@Override
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	/**
	 * Resets the proximity buffer to the default value.
	 */
	@Override
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	/**
	 * Calculates the distance between two locations in miles.
	 *
	 * @param loc1 The first location.
	 * @param loc2 The second location.
	 * @return The distance between the two locations in miles.
	 */
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

	/**
	 * Gets the reward points for an attraction based on the user.
	 *
	 * @param attraction The attraction.
	 * @param user The user.
	 * @return The number of reward points for the attraction.
	 */
	@Override
	public int getRewards(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	/**
	 * Calculates and assigns rewards to a user based on their visited locations and proximity to attractions.
	 *
	 * @param user The user to calculate rewards for.
	 * @return The user with updated rewards.
	 */
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

	/**
	 * Calculates rewards for all users in parallel and returns a list of users with updated rewards.
	 *
	 * @param users The list of users to calculate rewards for.
	 * @return A list of users with updated rewards.
	 * @throws Exception If an error occurs during parallel execution.
	 */
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
	/**
	 * Checks if a location is within the proximity range of an attraction.
	 *
	 * @param attraction The attraction.
	 * @param location The location to check.
	 * @return True if the location is within the attraction's proximity range; false otherwise.
	 */
	@Override
	public boolean isWithinAttractionProximity (Attraction attraction, Location location){
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}

	/**
	 * Checks if a visited location is near an attraction based on the proximity buffer.
	 *
	 * @param visitedLocation The visited location.
	 * @param attraction The attraction.
	 * @return True if the visited location is near the attraction; false otherwise.
	 */
	private boolean nearAttraction (VisitedLocation visitedLocation, Attraction attraction){
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}

	/**
	 * Gets the reward points for an attraction based on the user.
	 *
	 * @param attraction The attraction.
	 * @param user The user.
	 * @return The reward points for the attraction.
	 */
	private int getRewardPoints (Attraction attraction, User user){
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
}
