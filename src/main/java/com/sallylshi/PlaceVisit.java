package com.sallylshi;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;

class PlaceVisit {
    enum EditConfirmationStatus {
        NOT_CONFIRMED,
        CONFIRMED;
    }

    enum PlaceConfidence {
        LOW_CONFIDENCE,
        MEDIUM_CONFIDENCE,
        HIGH_CONFIDENCE;
    }

    Location location;
    Duration duration;
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
               ArrayList<Point> simplifiedRawPath) {
        this.location = location;
        this.duration = duration;
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
        return ("(" + location.latitudeE7
                + ", " + location.latitudeE7
                + ", " + location.placeId
                + ", " +  location.address
                + ", " + location.name
                + ", " + location.sourceInfo.deviceTag
                + ", " +  duration.toMillis() + ")");
    }

    public void writeToDatabase(long latitude, long longitude, String placeId, String address, String name, int deviceTag, long durationMs) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            PreparedStatement prep = conn.prepareStatement(
                    "insert into placevisit values (?, ?, ?, ?, ?, ?, ?);");
            prep.setLong(1, latitude);
            prep.setLong(2, longitude);
            prep.setString(3, placeId);
            prep.setString(4, address);
            prep.setString(5, name);
            prep.setInt(6, deviceTag);
            prep.setLong(7, durationMs);
            prep.executeUpdate();

            System.out.println("executed update placevisit");
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select * from placevisit;");
            while (rs.next()) {
                System.out.println("userId = " + rs.getLong("lat"));
                System.out.println("message = " + rs.getLong("long"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
