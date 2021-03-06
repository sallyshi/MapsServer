package com.sallylshi;

import javax.xml.transform.Source;
import java.time.Duration;
import java.util.ArrayList;

class PlaceVisit {
    enum EditConfirmationStatus {
        NOT_CONFIRMED,
        CONFIRMED;
    }

    enum PlaceConfidence {
        USER_CONFIRMED,
        LOW_CONFIDENCE,
        MEDIUM_CONFIDENCE,
        HIGH_CONFIDENCE;
    }

    Location location;
    Duration duration;
    long startTimestampMs;
    PlaceConfidence placeConfidence;
    long centerLatE7;
    long centerLngE7;
    double visitConfidence;
    ArrayList<Location> otherCandidateLocations;
    EditConfirmationStatus editConfirmationStatus;
    ArrayList<PlaceVisit> childVisits;
    ArrayList<Point> simplifiedRawPath;

    PlaceVisit(Location location, Duration duration, PlaceConfidence placeConfidence,
               long centerLatE7,
               long centerLngE7, double visitConfidence,
               ArrayList<Location> otherCandidateLocations,
               EditConfirmationStatus editConfirmationStatus, ArrayList<PlaceVisit> childVisits,
               ArrayList<Point> simplifiedRawPath, long startTimestampMs) {
        this.location = location;
        this.duration = duration;
        this.startTimestampMs = startTimestampMs;
        this.placeConfidence = placeConfidence;
        this.centerLatE7 = centerLatE7;
        this.centerLngE7 = centerLngE7;
        this.visitConfidence = visitConfidence;
        this.otherCandidateLocations = otherCandidateLocations;
        this.editConfirmationStatus = editConfirmationStatus;
        this.childVisits = childVisits;
        this.simplifiedRawPath = simplifiedRawPath;
    }

    public String generateSqlString() {
        long deviceTag = -1;

        if (location.sourceInfo != null) {
            deviceTag = location.sourceInfo.deviceTag;
        }
        return "("
                + location.latitudeE7
                + ", " + location.longitudeE7
                + ", " + "\"" + location.placeId + "\""
                + ", " + "\"" + location.address + "\""
                + ", " + "\"" + location.name + "\""
                + ", " + deviceTag
                + ", " + duration.toMillis()
                + ", " + startTimestampMs
                + ")";
    }
}
