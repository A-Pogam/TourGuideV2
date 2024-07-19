package com.openclassrooms.tourguide.dto;

public class AttractionNearUserDto {

    private NearbyAttractionDto nearbyAttractionDto;

    private UserLocationDto userLocationDto;

    private Double distance;

    private int rewardPoints;

    public AttractionNearUserDto(NearbyAttractionDto nearbyAttractionDto, UserLocationDto userLocationDto, Double distance, int rewardPoints) {
        this.nearbyAttractionDto = nearbyAttractionDto;
        this.userLocationDto = userLocationDto;
        this.distance = distance;
        this.rewardPoints = rewardPoints;
    }

    public NearbyAttractionDto getNearbyAttractionDto() {
        return nearbyAttractionDto;
    }

    public void setNearbyAttractionDto(NearbyAttractionDto nearbyAttractionDto) {
        this.nearbyAttractionDto = nearbyAttractionDto;
    }

    public UserLocationDto getUserLocationDto() {
        return userLocationDto;
    }

    public void setUserLocationDto(UserLocationDto userLocationDto) {
        this.userLocationDto = userLocationDto;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }




}
