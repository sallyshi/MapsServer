package com.sallylshi;

import java.time.Duration;
import java.util.ArrayList;

public class ActivitySegment {
    enum ActivityType {
        UNKNOWN_ACTIVITY_TYPE,
        WALKING,
        CYCLING,
        IN_VEHICLE,
        IN_PASSENGER_VEHICLE,
        IN_TAXI,
        MOTORCYCLING,
        FLYING,
        IN_BUS,
        IN_TRAIN,
        IN_SUBWAY,
        IN_TRAM,
        IN_FERRY,
        IN_CABLECAR,
        HIKING,
        KAYAKING,
        KITESURFING,
        ROWING,
        RUNNING,
        SAILING,
        SKATING,
        SKIING,
        IN_FUNICULAR,
        BOATING,
        SKATEBOARDING,
        IN_WHEELCHAIR,
        WALKING_NORDIC,
        SLEDDING,
        SNOWBOARDING,
        SNOWSHOEING,
        SNOWMOBILE,
        SURFING,
        SWIMMING,
        STILL,
        HORSEBACK_RIDING,
        CATCHING_POKEMON,
        IN_GONDOLA_LIFT;
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
