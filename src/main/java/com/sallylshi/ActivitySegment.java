package com.sallylshi;

import java.time.Duration;
import java.util.ArrayList;

public class ActivitySegment {
    enum ActivityType {
        WALKING,
        IN_SUBWAY,
        IN_BUS,
        STILL,
        IN_PASSENGER_VEHICLE,
        IN_TRAIN,
        IN_TRAM,
        CYCLING,
        RUNNING,
        MOTORCYCLING,
        IN_VEHICLE,
        FLYING,
        IN_FERRY,
        SKIING,
        SAILING;
    }

    enum Confidence {
        LOW, MEDIUM, HIGH;
    }

    Location startLocation;
    Location endLocation;
    Duration duration;
    long distance;
    ActivityType activityType;
    Confidence confidence;
    ArrayList<Activity> activities;
    ArrayList<Waypoint> waypointPath;
    ArrayList<Point> simplifiedRawPath;

    ActivitySegment(Location startLocation, Location endLocation, Duration duration,
                    long distance, ActivityType activityType, Confidence confidence,
                    ArrayList<Activity> activities, ArrayList<Waypoint> waypointPath,
                    ArrayList<Point> simplifiedRawPath) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.duration = duration;
        this.distance = distance;
        this.activityType = activityType;
        this.confidence = confidence;
        this.activities = activities;
        this.waypointPath = waypointPath;
        this.simplifiedRawPath = simplifiedRawPath;
    }
}
