package com.zunairah.planetarium.models;

public class Star extends CelestialObject {
    private double distance; // in light years

    /**
     * Full constructor for a Star.
     * @param id Unique identifier for the star.
     * @param name Common name of the star.
     * @param ra Right Ascension in degrees (passed to super as mockRA).
     * @param dec Declination in degrees (passed to super as mockDec).
     * @param magnitude Apparent magnitude of the star (passed to super as mockMagnitude).
     * @param spectralType Spectral classification (passed to super as mockSpectralType).
     * @param distance Distance in light years.
     */
    public Star(String id, String name, double ra, double dec, float magnitude, String spectralType, double distance) {
        super(id, name, Type.STAR, magnitude, ra, dec, spectralType);
        this.distance = distance;
    }

    /**
     * Simplified constructor for a Star. Uses name as ID.
     * @param name Common name of the star (also used as ID).
     * @param ra Right Ascension in degrees.
     * @param dec Declination in degrees.
     * @param magnitude Apparent magnitude of the star.
     */
    public Star(String name, double ra, double dec, float magnitude) {
        // mockSpectralType will be null if not provided in this simplified constructor.
        super(name, name, Type.STAR, magnitude, ra, dec, null);
        this.distance = 0; // Default distance
    }

    /**
     * Gets the spectral type from the superclass's mockSpectralType field.
     */
    public String getSpectralType() {
        return getMockSpectralType();
    }

    /**
     * Sets the spectral type in the superclass's mockSpectralType field.
     */
    public void setSpectralType(String spectralType) {
        setMockSpectralType(spectralType);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}