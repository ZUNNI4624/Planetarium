package com.zunairah.planetarium.models;

public class Planet extends CelestialObject {
    private double diameter; // in km
    private double orbitalDistanceAU; // in AU (renamed from 'distance' for clarity)

    /**
     * Full constructor for a Planet.
     * @param id Unique identifier.
     * @param name Name of the planet.
     * @param ra Right Ascension in degrees (passed to super as mockRA).
     * @param dec Declination in degrees (passed to super as mockDec).
     * @param magnitude Apparent magnitude (passed to super as mockMagnitude).
     * @param colorHex Color representation (e.g., "#FFCC00") (passed to super as mockSpectralType).
     * @param diameter Diameter in km.
     * @param orbitalDistanceAU Average orbital distance in AU.
     */
    public Planet(String id, String name, double ra, double dec, float magnitude, String colorHex,
                  double diameter, double orbitalDistanceAU) {
        super(id, name, Type.PLANET, magnitude, ra, dec, colorHex);
        this.diameter = diameter;
        this.orbitalDistanceAU = orbitalDistanceAU;
    }

    /**
     * Simplified constructor for a Planet. Uses name as ID.
     * @param name Name of the planet (also used as ID).
     * @param ra Right Ascension in degrees.
     * @param dec Declination in degrees.
     * @param magnitude Apparent magnitude.
     */
    public Planet(String name, double ra, double dec, float magnitude) {
        // mockSpectralType (for colorHex) will be null if not provided.
        super(name, name, Type.PLANET, magnitude, ra, dec, null);
        this.diameter = 0; // Default diameter
        this.orbitalDistanceAU = 0; // Default orbital distance
    }

    /**
     * Gets the color hex from the superclass's mockSpectralType field.
     */
    public String getColorHex() {
        return getMockSpectralType();
    }

    /**
     * Sets the color hex in the superclass's mockSpectralType field.
     */
    public void setColorHex(String colorHex) {
        setMockSpectralType(colorHex);
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public double getOrbitalDistanceAU() {
        return orbitalDistanceAU;
    }

    public void setOrbitalDistanceAU(double orbitalDistanceAU) {
        this.orbitalDistanceAU = orbitalDistanceAU;
    }
}