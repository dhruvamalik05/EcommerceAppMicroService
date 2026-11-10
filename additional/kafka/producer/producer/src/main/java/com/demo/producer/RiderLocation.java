package com.demo.producer;

import org.springframework.context.annotation.Configuration;


public class RiderLocation {
    private String riderId;
    private double latitude;
    private double longitude;

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getRiderId() {return riderId; }

    public RiderLocation(String riderId, double latitude, double longitude) {
        this.riderId = riderId;
        this.latitude = latitude;
        this.longitude = longitude;
    }




}
