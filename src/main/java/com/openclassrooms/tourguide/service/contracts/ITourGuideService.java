package com.openclassrooms.tourguide.service.contracts;

import java.util.List;

import com.openclassrooms.tourguide.dto.AttractionNearUserDto;
import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.util.Tracker;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

public interface ITourGuideService {
	
	public Tracker getTracker();

	public List<User> getAllUsers();
	public User getUser(String userName);
	public VisitedLocation getUserLocation(User user);
	
	public List<Reward> getUserRewards(User user);
	public List<Provider> getTripDeals(User user);
	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation);

	public void addUser(User user);

	public VisitedLocation trackUserLocation(User user);

	public List<AttractionNearUserDto> nearbyAttractionsToDto(VisitedLocation visitedLocation, User user, List<Attraction> nearbyAttractions);

	public List<VisitedLocation> trackAllUsersLocation(List<User> users) throws Exception;


	}