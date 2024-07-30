package com.openclassrooms.tourguide.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openclassrooms.tourguide.constant.InternalUserQuantity;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.service.TourGuideService;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

/**
 * Utility class for initializing internal test users with random locations and visit histories.
 * This class generates a specified number of internal users with random location data and visit history for testing purposes.
 */
public class InternalUsersInitializer {

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

	/**
	 * Initializes a map of internal test users.
	 * Each user is assigned a unique username, phone number, email, and random location history.
	 *
	 * @return A map of internal users with their usernames as keys.
	 */
	public Map<String, User> initializeInternalUsers() {
		Map<String, User> internalUserMap = new HashMap<>();

		IntStream.range(0, InternalUserQuantity.getInternalUserQuantity()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalUserQuantity.getInternalUserQuantity() + " internal test users.");

		return internalUserMap;
	}

	/**
	 * Generates random location history for a user.
	 * Adds three random visited locations to the user's visit history.
	 *
	 * @param user The user for whom the location history is to be generated.
	 */
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	/**
	 * Generates a random longitude value.
	 *
	 * @return A random longitude between -180 and 180.
	 */

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * Generates a random latitude value.
	 *
	 * @return A random latitude between -85.05112878 and 85.05112878.
	 */

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * Generates a random time within the last 30 days.
	 *
	 * @return A random date within the last 30 days.
	 */
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
}