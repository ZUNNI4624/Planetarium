package com.zunairah.planetarium.models;

import java.util.ArrayList;
import java.util.List;

public class Constellation extends CelestialObject {
    private List<int[]> lines; // Pairs of indices to stars in the 'stars' list
    private List<Star> stars;  // List of Star objects forming the constellation
    private String mythology;

    /**
     * Full constructor for a Constellation.
     * @param id Unique identifier.
     * @param name Name of the constellation.
     * @param ra Right Ascension of a reference point (degrees) (passed to super as mockRA).
     * @param dec Declination of a reference point (degrees) (passed to super as mockDec).
     * @param lines List of line connections (pairs of star indices).
     * @param stars List of stars in the constellation.
     * @param mythology Mythological information.
     */
    public Constellation(String id, String name, double ra, double dec,
                         List<int[]> lines, List<Star> stars, String mythology) {
        // mockMagnitude is 0f. mockSpectralType can be the name or null.
        super(id, name, Type.CONSTELLATION, 0f, ra, dec, name); // Using name for mockSpectralType
        this.lines = (lines != null) ? lines : new ArrayList<>();
        this.stars = (stars != null) ? stars : new ArrayList<>();
        this.mythology = mythology;
    }

    /**
     * Simplified constructor for a Constellation. Uses name as ID.
     * @param name Name of the constellation (also used as ID).
     * @param ra Right Ascension of a reference point (degrees).
     * @param dec Declination of a reference point (degrees).
     */
    public Constellation(String name, double ra, double dec) {
        super(name, name, Type.CONSTELLATION, 0f, ra, dec, name); // ID is name, mockSpectralType is name
        this.lines = new ArrayList<>();
        this.stars = new ArrayList<>();
        this.mythology = null;
    }

    // Getters and Setters
    public List<int[]> getLines() {
        return lines;
    }

    public void setLines(List<int[]> lines) {
        this.lines = lines;
    }

    public List<Star> getStars() {
        return stars;
    }

    public void setStars(List<Star> stars) {
        this.stars = stars;
    }

    public String getMythology() {
        return mythology;
    }

    public void setMythology(String mythology) {
        this.mythology = mythology;
    }
}