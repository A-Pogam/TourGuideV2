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
import org.springframework.scheduling.config.Task;
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
import org.w3c.dom.Attr;
import tripPricer.Provider;
import tripPricer.TripPricer;

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
	
	@Override
	public Tracker getTracker() {
		return tracker;
	}
	
	@Override
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	@Override
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	@Override
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
				: trackUserLocation(user);
		return visitedLocation;
	}

	@Override
	public List<Reward> getUserRewards(User user) {
		return user.getRewards();
	}

	@Override
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(ApiKey.TRIP_PRICER_API_KEY, user.getUserId(),
				user.getPreferences().getNumberOfAdults(), user.getPreferences().getNumberOfChildren(),
				user.getPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	@Override
	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		TreeMap<Double, Attraction> attractionByDistance = new TreeMap<>();
		for (Attraction attraction : gpsUtil.getAttractions()) {
			Double distance = iRewardService.getDistance(visitedLocation.location, attraction);
			attractionByDistance.put(distance, attraction);
		}
		for (Map.Entry<Double, Attraction> entry: attractionByDistance.entrySet()) {
			nearbyAttractions.add(entry.getValue());
		}
		return nearbyAttractions.stream().limit(5).collect(Collectors.toList());
	}

	@Override
	public List<AttractionNearUserDto> nearbyAttractionsToDto(VisitedLocation visitedLocation, User user, List<Attraction> nearbyAttractions) {
		UserLocationDto userLocationDto = new UserLocationDto(visitedLocation.location.longitude, visitedLocation.location.latitude);
		List<AttractionNearUserDto> formatedNearbyAttractions = new ArrayList<>();

		for (Attraction attraction:nearbyAttractions) {
			NearbyAttractionDto nearbyAttractionDto = new NearbyAttractionDto(attraction.attractionName, attraction.longitude, attraction.latitude);
			AttractionNearUserDto attractionNearUserDto = new AttractionNearUserDto(nearbyAttractionDto, userLocationDto, iRewardService.getDistance(visitedLocation.location, attraction), iRewardService.getRewards(attraction, user));
			formatedNearbyAttractions.add(attractionNearUserDto);
		}
		return formatedNearbyAttractions;
	}

	@Override
	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	@Override
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		iRewardService.calculateRewards(user);
		return visitedLocation;
	}

	@Override
	public List<VisitedLocation> trackAllUsersLocation(List<User> users) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(100);
		List<VisitedLocation> usersLocation = new ArrayList<>();
		for (User user : users) {
			executor.execute(()->{
				VisitedLocation visitedLocation = trackUserLocation(user);
				usersLocation.add(visitedLocation);
			});
		}
		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		return usersLocation;
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				tracker.stopTracking();
			}
		});
	}


}