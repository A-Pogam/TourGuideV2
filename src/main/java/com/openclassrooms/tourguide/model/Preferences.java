package com.openclassrooms.tourguide.model;


/**
 * Represents the preferences for a tour, including attraction proximity, trip duration,
 * ticket quantity, and the number of adults and children.
 * This class is used to store user preferences that influence the tour planning.
 */
public class Preferences {

	/**
	 * The maximum distance from attractions (in meters) that is considered acceptable.
	 * Initialized to the maximum value of an integer, representing no limit by default.
	 */
	private int attractionProximity = Integer.MAX_VALUE;

	/**
	 * The duration of the trip (in hours).
	 * Initialized to 1 hour by default.
	 */
	private int tripDuration = 1;

	/**
	 * The number of tickets required for the trip.
	 * Initialized to 1 ticket by default.
	 */
	private int ticketQuantity = 1;

	/**
	 * The number of adults participating in the trip.
	 * Initialized to 1 adult by default.
	 */
	private int numberOfAdults = 1;

	/**
	 * The number of children participating in the trip.
	 * Initialized to 0 children by default.
	 */
	private int numberOfChildren = 0;

	public Preferences() {

	}

	/**
	 * Sets the maximum distance from attractions (in meters) that is considered acceptable.
	 *
	 * @param attractionProximity The maximum acceptable distance from attractions.
	 */
	public void setAttractionProximity(int attractionProximity) {
		this.attractionProximity = attractionProximity;
	}

	/**
	 * Gets the maximum distance from attractions (in meters) that is considered acceptable.
	 *
	 * @return The maximum acceptable distance from attractions.
	 */
	public int getAttractionProximity() {
		return attractionProximity;
	}

	/**
	 * Gets the duration of the trip (in hours).
	 *
	 * @return The duration of the trip.
	 */
	public int getTripDuration() {
		return tripDuration;
	}

	/**
	 * Sets the duration of the trip (in hours).
	 *
	 * @param tripDuration The duration of the trip.
	 */
	public void setTripDuration(int tripDuration) {
		this.tripDuration = tripDuration;
	}

	/**
	 * Gets the number of tickets required for the trip.
	 *
	 * @return The number of tickets required.
	 */
	public int getTicketQuantity() {
		return ticketQuantity;
	}

	/**
	 * Sets the number of tickets required for the trip.
	 *
	 * @param ticketQuantity The number of tickets required.
	 */
	public void setTicketQuantity(int ticketQuantity) {
		this.ticketQuantity = ticketQuantity;
	}


	/**
	 * Gets the number of adults participating in the trip.
	 *
	 * @return The number of adults.
	 */
	public int getNumberOfAdults() {
		return numberOfAdults;
	}

	/**
	 * Sets the number of adults participating in the trip.
	 *
	 * @param numberOfAdults The number of adults.
	 */
	public void setNumberOfAdults(int numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	/**
	 * Gets the number of children participating in the trip.
	 *
	 * @return The number of children.
	 */
	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	/**
	 * Sets the number of children participating in the trip.
	 *
	 * @param numberOfChildren The number of children.
	 */
	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}
}