package com.openclassrooms.tourguide.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.openclassrooms.tourguide.constant.InternalUserQuantity;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.service.contracts.IRewardService;
import com.openclassrooms.tourguide.service.contracts.ITourGuideService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tripPricer.Provider;

public class TourGuideServiceIT {

	GpsUtil gpsUtil = new GpsUtil();
	IRewardService iRewardsService = new RewardService(gpsUtil, new RewardCentral());
	ITourGuideService iTourGuideService = new TourGuideService(gpsUtil, iRewardsService);

	@Test
	public void getUserLocation() {
		InternalUserQuantity.setInternalUserQuantity(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = iTourGuideService.trackUserLocation(user);

		iTourGuideService.getTracker().stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}

	@Test
	public void addUser() {
		InternalUserQuantity.setInternalUserQuantity(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		iTourGuideService.addUser(user);
		iTourGuideService.addUser(user2);

		User retrivedUser = iTourGuideService.getUser(user.getUserName());
		User retrivedUser2 = iTourGuideService.getUser(user2.getUserName());

		iTourGuideService.getTracker().stopTracking();

		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}

	@Test
	public void getAllUsers() {
		InternalUserQuantity.setInternalUserQuantity(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		iTourGuideService.addUser(user);
		iTourGuideService.addUser(user2);

		List<User> allUsers = iTourGuideService.getAllUsers();

		iTourGuideService.getTracker().stopTracking();
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

	@Test
	public void trackUser() {
		InternalUserQuantity.setInternalUserQuantity(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = iTourGuideService.trackUserLocation(user);

		iTourGuideService.getTracker().stopTracking();
		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	@Test
	public void getNearbyAttractions() {
		InternalUserQuantity.setInternalUserQuantity(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = iTourGuideService.trackUserLocation(user);

		List<Attraction> attractions = iTourGuideService.getNearByAttractions(visitedLocation);

		iTourGuideService.getTracker().stopTracking();
		assertEquals(5, attractions.size());
	}

	public void getTripDeals() {
		InternalUserQuantity.setInternalUserQuantity(0);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = iTourGuideService.getTripDeals(user);

		iTourGuideService.getTracker().stopTracking();
		assertEquals(10, providers.size());
	}
}