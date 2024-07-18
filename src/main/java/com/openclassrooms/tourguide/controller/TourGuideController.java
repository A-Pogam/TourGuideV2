package com.openclassrooms.tourguide.controller;

import java.util.List;

import com.openclassrooms.tourguide.dto.AttractionNearUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.service.contracts.ITourGuideService;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	private ITourGuideService iTourGuideService;

	@GetMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}
	
	@GetMapping("/user")
	public User getUser(@RequestParam String userName) {
		return iTourGuideService.getUser(userName);
	}

	@GetMapping("/getLocation")
	public VisitedLocation getLocation(@RequestParam String userName) {
		User user = iTourGuideService.getUser(userName);
		return iTourGuideService.getUserLocation(user);
	}

	// TODO: Change this method to no longer return a List of Attractions.
	// Instead: Get the closest five tourist attractions to the user - no matter how
	// far away they are.
	// Return a new JSON object that contains:
		// Name of Tourist attraction,
		// Tourist attractions lat/long,
		// The user's location lat/long,
		// The distance in miles between the user's location and each of the
		// attractions.
		// The reward points for visiting each Attraction.
	// Note: Attraction reward points can be gathered from RewardsCentral
	@GetMapping("/getNearbyAttractions")
	public List<AttractionNearUserDto> getNearbyAttractions(@RequestParam String userName) {
		User user = iTourGuideService.getUser(userName);
		VisitedLocation visitedLocation = iTourGuideService.getUserLocation(user);
		List<Attraction> nearbyAttractions = iTourGuideService.getNearByAttractions(visitedLocation);
		
		return iTourGuideService.nearbyAttractionsToDto(visitedLocation, user, nearbyAttractions);
	}

	@GetMapping("/getRewards")
	public List<Reward> getRewards(@RequestParam String userName) {
		User user = iTourGuideService.getUser(userName);
		return iTourGuideService.getUserRewards(user);
	}

	@GetMapping("/getTripDeals")
	public List<Provider> getTripDeals(@RequestParam String userName) {
		User user = iTourGuideService.getUser(userName);
		return iTourGuideService.getTripDeals(user);
	}
}