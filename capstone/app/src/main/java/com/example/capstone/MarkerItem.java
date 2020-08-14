package com.example.capstone;

public class MarkerItem {
    private double lat;
    private double lon;
    private String placeTitle;
    private int preferenceRatio;

    public MarkerItem(double lat, double lon, String placeTitle, int preferenceRatio) {
        this.lat = lat;
        this.lon = lon;
        this.placeTitle = placeTitle;
        this.preferenceRatio = preferenceRatio;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public void setPlaceTitle(String placeTitle) {
        this.placeTitle = placeTitle;
    }

    public int getPreferenceRatio() {
        return preferenceRatio;
    }

    public void setPreferenceRatio(int preferenceRatio) {
        this.preferenceRatio = preferenceRatio;
    }

}
