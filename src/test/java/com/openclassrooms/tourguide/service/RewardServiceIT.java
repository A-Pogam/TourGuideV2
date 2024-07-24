package com.openclassrooms.tourguide.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.openclassrooms.tourguide.constant.InternalUserQuantity;
import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.service.contracts.IRewardService;
import com.openclassrooms.tourguide.service.contracts.ITourGuideService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;

public class RewardServiceIT {

	private GpsUtil gpsUtil = new GpsUtil();
	private IRewardService iRewardService = new RewardService(gpsUtil, new RewardCentral());
	private ITourGuideService iTourGuideService = new TourGuideService(gpsUtil, iRewardService);
	
	@Test
	public void userGetRewards() {
		InternalUserQuantity.setInternalUserQuantity(0);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		
		iTourGuideService.trackUserLocation(user);
		List<Reward> userRewards = user.getRewards();
		
		iTourGuideService.getTracker().stopTracking();
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(iRewardService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAllAttractions() {
		iRewardService.setProximityBuffer(Integer.MAX_VALUE);

		InternalUserQuantity.setInternalUserQuantity(1);

		iRewardService.calculateRewards(iTourGuideService.getAllUsers().get(0));
		List<Reward> userRewards = iTourGuideService.getUserRewards(iTourGuideService.getAllUsers().get(0));
		
		iTourGuideService.getTracker().stopTracking();
		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}
}