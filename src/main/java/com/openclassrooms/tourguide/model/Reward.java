package com.openclassrooms.tourguide.model;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

/**
 * Represents a reward given to a user for visiting an attraction.
 * This class contains details about the visited location, the attraction, and the reward points awarded.
 */
public class Reward {

	public final VisitedLocation visitedLocation;
	public final Attraction attraction;
	private int rewardPoints;

	/**
	 * Constructs a Reward object with the specified visited location, attraction, and reward points.
	 *
	 * @param visitedLocation The visited location associated with the reward.
	 * @param attraction The attraction visited that earned the reward.
	 * @param rewardPoints The number of reward points awarded.
	 */
	public Reward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
		this.rewardPoints = rewardPoints;
	}

	/**
	 * Constructs a Reward object with the specified visited location and attraction, and sets reward points to zero.
	 *
	 * @param visitedLocation The visited location associated with the reward.
	 * @param attraction The attraction visited that earned the reward.
	 */
	public Reward(VisitedLocation visitedLocation, Attraction attraction) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
	}

	/**
	 * Gets the number of reward points awarded.
	 *
	 * @return The number of reward points.
	 */
	public int getRewardPoints() {
		return rewardPoints;
	}

	/**
	 * Sets the number of reward points awarded.
	 *
	 * @param rewardPoints The number of reward points to set.
	 */
	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
}