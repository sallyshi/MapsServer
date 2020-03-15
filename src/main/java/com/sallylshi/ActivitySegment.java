package com.sallylshi;

import java.time.Duration;
import java.util.ArrayList;

public class ActivitySegment {
    enum Confidence {
        LOW, MEDIUM, HIGH;
    }

    Location startLocation;
    Location endLocation;
    Duration duration;
    long distance;
    Confidence confidence;
    ArrayList<Activity> activities;
    ArrayList<Waypoint> waypointPath;
    ArrayList<Point> simplifiedRawPath;

    ActivitySegment(Location startLocation, Location endLocation, Duration duration,
                    long distance, Confidence confidence,
                    ArrayList<Activity> activities, ArrayList<Waypoint> waypointPath,
                    ArrayList<Point> simplifiedRawPath) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.duration = duration;
        this.distance = distance;
        this.confidence = confidence;
        this.activities = activities;
        this.waypointPath = waypointPath;
        this.simplifiedRawPath = simplifiedRawPath;
    }
}
