package com.openclassrooms.tourguide.service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.openclassrooms.tourguide.dto.AttractionNearUserDto;
import com.openclassrooms.tourguide.dto.NearbyAttractionDto;
import com.openclassrooms.tourguide.dto.UserLocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.tourguide.constant.ApiKey;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.service.contracts.IRewardService;
import com.openclassrooms.tourguide.service.contracts.ITourGuideService;
import com.openclassrooms.tourguide.util.InternalUsersInitializer;
import com.openclassrooms.tourguide.util.Tracker;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;
import tripPricer.TripPricer;

/**
 * Service for managing and tracking user locations, rewards, and trip deals.
 * This service provides functionality for retrieving user information, calculating rewards,
 * fetching nearby attractions, and handling trip deals.
 */
@Service
public class TourGuideService implements ITourGuideService {

	@Autowired
	private IRewardService iRewardService;

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

	private final GpsUtil gpsUtil;
	private final TripPricer tripPricer = new TripPricer();
	private final InternalUsersInitializer internalUsers = new InternalUsersInitializer();

	private Map<String, User> internalUserMap;
	private boolean testMode = true;

	public final Tracker tracker;

	/**
	 * Constructs a TourGuideService with the specified GPS utility and reward service.
	 *
	 * @param gpsUtil The GPS utility for retrieving location data.
	 * @param iRewardService The reward service for managing user rewards.
	 */
	public TourGuideService(GpsUtil gpsUtil, IRewardService iRewardService) {
		this.gpsUtil = gpsUtil;
		this.iRewardService = iRewardService;

		Locale.setDefault(Locale.US);

		if (testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			internalUserMap = internalUsers.initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}

	/**
	 * Gets the tracker for monitoring user locations.
	 *
	 * @return The tracker instance.
	 */
	@Override
	public Tracker getTracker() {
		return tracker;
	}

	/**
	 * Retrieves a list of all users.
	 *
	 * @return A list of all users.
	 */
	@Override
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * Retrieves a user by their username.
	 *
	 * @param userName The username of the user.
	 * @return The user associated with the given username, or null if not found.
	 */
	@Override
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	/**
	 * Retrieves the current location of a user.
	 * If the user has no visited locations, it tracks the user's location first.
	 *
	 * @param user The user whose location is to be retrieved.
	 * @return The user's current visited location.
	 */
	@Override
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
				: trackUserLocation(user);
		return visitedLocation;
	}

	/**
	 * Retrieves the list of rewards for a user.
	 *
	 * @param user The user whose rewards are to be retrieved.
	 * @return The list of rewards for the user.
	 */
	@Override
	public List<Reward> getUserRewards(User user) {
		return user.getRewards();
	}

	/**
	 * Retrieves a list of trip deals for a user based on their rewards and preferences.
	 *
	 * @param user The user whose trip deals are to be retrieved.
	 * @return The list of trip deals for the user.
	 */
	@Override
	public List<Provider> getTripDeals(User user) {
		int cumulativeRewardPoints = user.getRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(ApiKey.TRIP_PRICER_API_KEY, user.getUserId(),
				user.getPreferences().getNumberOfAdults(), user.getPreferences().getNumberOfChildren(),
				user.getPreferences().getTripDuration(), cumulativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	/**
	 * Retrieves a list of nearby attractions for a given visited location.
	 *
	 * @param visitedLocation The location to find nearby attractions for.
	 * @return A list of nearby attractions, limited to the top 5.
	 */
	@Override
	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		TreeMap<Double, Attraction> attractionByDistance = new TreeMap<>();
		for (Attraction attraction : gpsUtil.getAttractions()) {
			Double distance = iRewardService.getDistance(visitedLocation.location, attraction);
			attractionByDistance.put(distance, attraction);
		}
		for (Map.Entry<Double, Attraction> entry : attractionByDistance.entrySet()) {
			nearbyAttractions.add(entry.getValue());
		}
		return nearbyAttractions.stream().limit(5).collect(Collectors.toList());
	}

	/**
	 * Converts a list of nearby attractions and user location into DTOs for easier processing or display.
	 *
	 * @param visitedLocation The user's visited location.
	 * @param user The user for whom the attractions are being processed.
	 * @param nearbyAttractions The list of nearby attractions.
	 * @return A list of DTOs containing information about nearby attractions and the user's location.
	 */
	@Override
	public List<AttractionNearUserDto> nearbyAttractionsToDto(VisitedLocation visitedLocation, User user, List<Attraction> nearbyAttractions) {
		UserLocationDto userLocationDto = new UserLocationDto(visitedLocation.location.longitude, visitedLocation.location.latitude);
		List<AttractionNearUserDto> formattedNearbyAttractions = new ArrayList<>();

		for (Attraction attraction : nearbyAttractions) {
			NearbyAttractionDto nearbyAttractionDto = new NearbyAttractionDto(attraction.attractionName, attraction.longitude, attraction.latitude);
			AttractionNearUserDto attractionNearUserDto = new AttractionNearUserDto(nearbyAttractionDto, userLocationDto, iRewardService.getDistance(visitedLocation.location, attraction), iRewardService.getRewards(attraction, user));
			formattedNearbyAttractions.add(attractionNearUserDto);
		}
		return formattedNearbyAttractions;
	}

	/**
	 * Adds a new user to the internal user map.
	 *
	 * @param user The user to be added.
	 */
	@Override
	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	/**
	 * Tracks a user's location and updates their visited locations and rewards.
	 *
	 * @param user The user whose location is to be tracked.
	 * @return The user's newly visited location.
	 */
	@Override
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		iRewardService.calculateRewards(user);
		return visitedLocation;
	}

	/**
	 * Tracks the locations of all users in parallel.
	 *
	 * @param users The list of users to track.
	 * @return A list of visited locations for all users.
	 * @throws Exception If an error occurs during parallel execution.
	 */
	@Override
	public List<VisitedLocation> trackAllUsersLocation(List<User> users) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(100);
		List<VisitedLocation> usersLocation = new ArrayList<>();
		for (User user : users) {
			executor.execute(() -> {
				VisitedLocation visitedLocation = trackUserLocation(user);
				synchronized (usersLocation) {
					usersLocation.add(visitedLocation);
				}
			});
		}
		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		return usersLocation;
	}

	/**
	 * Adds a shutdown hook to stop tracking when the JVM shuts down.
	 */
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				tracker.stopTracking();
			}
		});
	}
}
