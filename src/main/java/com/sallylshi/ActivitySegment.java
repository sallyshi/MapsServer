package com.sallylshi;

import java.time.Duration;
import java.util.ArrayList;

public class ActivitySegment {
    enum ActivityType {
        UNKNOWN_ACTIVITY_TYPE,
        HIKING,
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
        UNKNOWN_CONFIDENCE, LOW, MEDIUM, HIGH;
    }

    Location startLocation;
    Location endLocation;
    Duration duration;
    long distance;
    long startTimestampMs;
    Confidence confidence;
    ActivityType activityType;
    ArrayList<Activity> activities;
    ArrayList<Waypoint> waypointPath;
    ArrayList<Point> simplifiedRawPath;

    ActivitySegment(Location startLocation, Location endLocation, Duration duration,
                    long distance, Confidence confidence, ActivityType activityType,
                    ArrayList<Activity> activities, ArrayList<Waypoint> waypointPath,
                    ArrayList<Point> simplifiedRawPath, long startTimestampMs) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.duration = duration;
        this.distance = distance;
        this.startTimestampMs = startTimestampMs;
        this.confidence = confidence;
        this.activityType = activityType;
        this.activities = activities;
        this.waypointPath = waypointPath;
        this.simplifiedRawPath = simplifiedRawPath;
    }
}
