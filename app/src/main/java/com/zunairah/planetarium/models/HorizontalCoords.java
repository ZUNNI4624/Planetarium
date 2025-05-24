package com.zunairah.planetarium.models;

public class HorizontalCoords {
    private double altitudeDeg;
    private double azimuthDeg;

    public HorizontalCoords(double altitudeDeg, double azimuthDeg) {
        this.altitudeDeg = altitudeDeg;
        this.azimuthDeg = azimuthDeg;
    }

    public double getAltitudeDeg() {
        return altitudeDeg;
    }

    public void setAltitudeDeg(double altitudeDeg) {
        this.altitudeDeg = altitudeDeg;
    }

    public double getAzimuthDeg() {
        return azimuthDeg;
    }

    public void setAzimuthDeg(double azimuthDeg) {
        this.azimuthDeg = azimuthDeg;
    }
}
