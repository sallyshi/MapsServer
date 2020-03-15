package com.sallylshi;

public class Activity {
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
    ActivityType activityType;
    double probability;

    public Activity(ActivityType activityType, double probability) {
        this.activityType = activityType;
        this.probability = probability;
    }
}
