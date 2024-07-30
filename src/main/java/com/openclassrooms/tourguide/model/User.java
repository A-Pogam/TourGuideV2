package com.openclassrooms.tourguide.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

/**
 * Represents a user in the tour guide application.
 * This class contains information about the user, including personal details,
 * visited locations, rewards, preferences, and trip deals.
 */
public class User {

	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;

	/**
	 * The timestamp of the user's latest location update.
	 */
	private Date latestLocationTimestamp;

	/**
	 * A list of locations visited by the user.
	 */
	private List<VisitedLocation> visitedLocations = new ArrayList<>();

	/**
	 * A list of rewards earned by the user.
	 */
	private List<Reward> rewards = new ArrayList<>();

	/**
	 * The user's preferences for tour planning.
	 */
	private Preferences preferences = new Preferences();
	/**
	 * A list of trip deals available for the user.
	 */
	private List<Provider> tripDeals = new ArrayList<>();

	/**
	 * Constructs a User object with the specified user ID, name, phone number, and email address.
	 *
	 * @param userId The unique identifier for the user.
	 * @param userName The name of the user.
	 * @param phoneNumber The phone number of the user.
	 * @param emailAddress The email address of the user.
	 */
	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}

	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}

	public List<VisitedLocation> getVisitedLocations() {
		return visitedLocations;
	}

	public void addToVisitedLocations(VisitedLocation visitedLocation) {
		visitedLocations.add(visitedLocation);
	}

	public void clearVisitedLocations() {
		visitedLocations.clear();
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public Preferences getPreferences() {
		return preferences;
	}

	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
	}

	public List<Provider> getTripDeals() {
		return tripDeals;
	}

	public void setTripDeals(List<Provider> tripDeals) {
		this.tripDeals = tripDeals;
	}

	public VisitedLocation getLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}

	public void addReward(Reward reward) {
		if (rewards.stream().filter(r -> r.attraction.attractionName.equals(reward.attraction.attractionName)).count() == 0) {
			rewards.add(reward);
		}
	}
}