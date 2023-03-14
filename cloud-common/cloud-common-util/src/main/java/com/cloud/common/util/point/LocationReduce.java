package com.cloud.common.util.point;

import lombok.Data;

@Data
public class LocationReduce implements Comparable<LocationReduce> {

    private String lon;

    private String lat;

    private Long time;

    private String direction;

    private double speed;

    private double distance;

    public LocationReduce() {

    }

    public LocationReduce(String lon, String lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public String toString() {
        return "[" + this.getLon() + "," + this.getLat() + "]";
    }

    @Override
    public int compareTo(LocationReduce o) {
        if (this.getTime() < o.getTime()) {
            return -1;
        } else if (this.getTime() > o.getTime()) {
            return 1;
        } else {
            return 0;
        }
    }
}
