package com.openclassrooms.tourguide.dto;

/**
 * Data Transfer Object (DTO) representing a user's location with latitude and longitude.
 * This class is used to store and transfer the location information
 * of a user within the context of the tour guide application.
 */
public class UserLocationDto {
    private Double longitude;

    private Double latitude;

    /**
     * Constructs a UserLocationDto object with the specified longitude and latitude.
     *
     * @param longitude The longitude of the user's location.
     * @param latitude The latitude of the user's location.
     */
    public UserLocationDto(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the user's location.
     *
     * @return The longitude of the user's location.
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the user's location.
     *
     * @param longitude The new longitude of the user's location.
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the latitude of the user's location.
     *
     * @return The latitude of the user's location.
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the user's location.
     *
     * @param latitude The new latitude of the user's location.
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

}
