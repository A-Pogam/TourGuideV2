package com.openclassrooms.tourguide.dto;
/**
 * Data Transfer Object (DTO) representing the details of an attraction near a user.
 */
public class AttractionNearUserDto {

    private NearbyAttractionDto nearbyAttractionDto;

    private UserLocationDto userLocationDto;

    private Double distance;

    private int rewardPoints;

    /**
     * Constructs a new AttractionNearUserDto with the specified details.
     *
     * @param nearbyAttractionDto Details of the nearby attraction.
     * @param userLocationDto Details of the user's location.
     * @param distance Distance between the user and the attraction.
     * @param rewardPoints Reward points for visiting the attraction.
     */

    public AttractionNearUserDto(NearbyAttractionDto nearbyAttractionDto, UserLocationDto userLocationDto, Double distance, int rewardPoints) {
        this.nearbyAttractionDto = nearbyAttractionDto;
        this.userLocationDto = userLocationDto;
        this.distance = distance;
        this.rewardPoints = rewardPoints;
    }

    /**
     * Gets the details of the nearby attraction.
     *
     * @return The nearby attraction details.
     */
    public NearbyAttractionDto getNearbyAttractionDto() {
        return nearbyAttractionDto;
    }

    /**
     * Sets the details of the nearby attraction.
     *
     * @param nearbyAttractionDto The nearby attraction details to set.
     */
    public void setNearbyAttractionDto(NearbyAttractionDto nearbyAttractionDto) {
        this.nearbyAttractionDto = nearbyAttractionDto;
    }

    /**
     * Gets the details of the user's location.
     *
     * @return The user's location details.
     */
    public UserLocationDto getUserLocationDto() {
        return userLocationDto;
    }

    /**
     * Sets the details of the user's location.
     *
     * @param userLocationDto The user's location details to set.
     */
    public void setUserLocationDto(UserLocationDto userLocationDto) {
        this.userLocationDto = userLocationDto;
    }

    /**
     * Gets the distance between the user and the attraction.
     *
     * @return The distance in miles.
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * Sets the distance between the user and the attraction.
     *
     * @param distance The distance to set.
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * Gets the reward points for visiting the attraction.
     *
     * @return The reward points.
     */
    public int getRewardPoints() {
        return rewardPoints;
    }

    /**
     * Sets the reward points for visiting the attraction.
     *
     * @param rewardPoints The reward points to set.
     */
    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }




}
