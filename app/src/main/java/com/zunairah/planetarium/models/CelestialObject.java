package com.zunairah.planetarium.models;

public class CelestialObject {
    public enum Type {
        STAR,
        PLANET,
        SUN,
        MOON,
        CONSTELLATION // Added for Constellation type
    }

    private String id;
    private String name;
    private Type type;
    private float mockMagnitude;
    private double mockRA;  // Right Ascension in degrees
    private double mockDec; // Declination in degrees
    private String mockSpectralType; // For stars: O, B, A, F, G, K, M; For planets: color hex
    private int animationAlpha = 255; // For fade in/out animations

    public CelestialObject(String id, String name, Type type, float mockMagnitude,
                           double mockRA, double mockDec, String mockSpectralType) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.mockMagnitude = mockMagnitude;
        this.mockRA = mockRA;
        this.mockDec = mockDec;
        this.mockSpectralType = mockSpectralType;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public float getMockMagnitude() {
        return mockMagnitude;
    }

    public void setMockMagnitude(float mockMagnitude) {
        this.mockMagnitude = mockMagnitude;
    }

    public double getMockRA() {
        return mockRA;
    }

    public void setMockRA(double mockRA) {
        this.mockRA = mockRA;
    }

    public double getMockDec() {
        return mockDec;
    }

    public void setMockDec(double mockDec) {
        this.mockDec = mockDec;
    }

    public String getMockSpectralType() {
        return mockSpectralType;
    }

    public void setMockSpectralType(String mockSpectralType) {
        this.mockSpectralType = mockSpectralType;
    }

    public int getAnimationAlpha() {
        return animationAlpha;
    }

    public void setAnimationAlpha(int animationAlpha) {
        this.animationAlpha = Math.max(0, Math.min(255, animationAlpha));
    }
}