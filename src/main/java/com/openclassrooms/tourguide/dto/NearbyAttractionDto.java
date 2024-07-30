package com.openclassrooms.tourguide.dto;

/**
 * Data Transfer Object (DTO) representing the details of a nearby attraction.
 */
public class NearbyAttractionDto {

    private String name;

    private Double longitude;

    private Double latitude;

    /**
     * Constructs a new NearbyAttractionDto with the specified details.
     *
     * @param name The name of the attraction.
     * @param longitude The longitude of the attraction.
     * @param latitude The latitude of the attraction.
     */
    public NearbyAttractionDto(String name, Double longitude, Double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Gets the name of the attraction.
     *
     * @return The name of the attraction.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the attraction.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the longitude of the attraction.
     *
     * @return The longitude of the attraction.
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the attraction.
     *
     * @param longitude The longitude to set.
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the latitude of the attraction.
     *
     * @return The latitude of the attraction.
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the attraction.
     *
     * @param latitude The latitude to set.
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }



}
