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

/**
 * The TourGuideController class handles HTTP requests related to the TourGuide service.
 */
@RestController
public class TourGuideController {

	@Autowired
	private ITourGuideService iTourGuideService;

	/**
	 * Handles the root request and returns a greeting message.
	 *
	 * @return A greeting message.
	 */
	@GetMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}

	/**
	 * Retrieves a user based on the provided username.
	 *
	 * @param userName The username of the user to retrieve.
	 * @return The User object.
	 */
	@GetMapping("/user")
	public User getUser(@RequestParam String userName) {
		return iTourGuideService.getUser(userName);
	}

	/**
	 * Retrieves the location of a user based on the provided username.
	 *
	 * @param userName The username of the user.
	 * @return The VisitedLocation object of the user.
	 */
	@GetMapping("/getLocation")
	public VisitedLocation getLocation(@RequestParam String userName) {
		User user = iTourGuideService.getUser(userName);
		return iTourGuideService.getUserLocation(user);
	}

	/**
	 * Retrieves the nearest tourist attractions to a user based on the provided username.
	 * The returned information includes the attraction name, attraction latitude/longitude,
	 * user location latitude/longitude, distance between the user and the attraction, and
	 * the reward points for visiting each attraction.
	 *
	 * @param userName The username of the user.
	 * @return A list of AttractionNearUserDto objects.
	 */
	@GetMapping("/getNearbyAttractions")
	public List<AttractionNearUserDto> getNearbyAttractions(@RequestParam String userName) {
		User user = iTourGuideService.getUser(userName);
		VisitedLocation visitedLocation = iTourGuideService.getUserLocation(user);
		List<Attraction> nearbyAttractions = iTourGuideService.getNearByAttractions(visitedLocation);
		
		return iTourGuideService.nearbyAttractionsToDto(visitedLocation, user, nearbyAttractions);
	}

	/**
	 * Retrieves the rewards for a user based on the provided username.
	 *
	 * @param userName The username of the user.
	 * @return A list of Reward objects for the user.
	 */
	@GetMapping("/getRewards")
	public List<Reward> getRewards(@RequestParam String userName) {
		User user = iTourGuideService.getUser(userName);
		return iTourGuideService.getUserRewards(user);
	}

	/**
	 * Retrieves trip deals for a user based on the provided username.
	 *
	 * @param userName The username of the user.
	 * @return A list of Provider objects representing trip deals for the user.
	 */
	@GetMapping("/getTripDeals")
	public List<Provider> getTripDeals(@RequestParam String userName) {
		User user = iTourGuideService.getUser(userName);
		return iTourGuideService.getTripDeals(user);
	}
}