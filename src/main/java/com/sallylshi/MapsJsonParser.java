package com.sallylshi;

import java.io.*;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import com.google.gson.stream.JsonReader;

public class MapsJsonParser {
    Location location;
    Duration duration;
    String placeConfidence;
    String centerLatE7;
    String centerLngE7;
    double visitConfidence;
    ArrayList<Location> otherCandidateLocations;
    PlaceVisit.EditConfirmationStatus editConfirmationStatus;



    String read(JsonReader reader) {
        try {
            ArrayList<PlaceVisit> placeVisits = new ArrayList<>();
            reader.beginObject();
            reader.nextName();
            reader.beginArray();

            while (reader.hasNext()) {
                reader.beginObject();
                String n = reader.nextName();
                switch (n) {
                    case "placeVisit":
                        placeVisits.add(parsePlaceVisit(reader));
                        break;
                    case "activitySegment":
                        //Temporary
                        reader.skipValue();
                        break;
                    default:
                        System.out.println("MapsJsonReader: Parsing overall couldn't find name " + n + ". Went into " +
                                "default.");
                        break;
                }
                reader.endObject();
            }
            reader.endArray();
            reader.endObject();

            Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement stat = conn.createStatement();
            for (PlaceVisit p : placeVisits) {
                   // stat.addBatch("insert into placevisit values " + p.generateSqlString() + ";");
                System.out.println(
                        "PlaceVisit: " + p.location + p.duration + p.centerLngE7 + p.centerLatE7 +
                                "List size is: " + placeVisits.size());
            }
            stat.executeBatch();
            System.out.println("Successfully executed batch for placevisit.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        return "YAY";
    }

    private PlaceVisit parsePlaceVisit(JsonReader reader) throws IOException {
        PlaceVisit.PlaceConfidence placeConfidence = null;
        long centerLatE7 = 0;
        long centerLngE7 = 0;
        int visitConfidence = 0;
        ArrayList<Location> otherCandidateLocations = new ArrayList<>();
        ArrayList<PlaceVisit> childVisits = new ArrayList<>();
        PlaceVisit.EditConfirmationStatus editConfirmationStatus = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String n = reader.nextName();
            switch (n) {
                case "location":
                    location = parseLocation(reader);
                    break;
                case "duration":
                    duration = parseDuration(reader);
                    break;
                case "placeConfidence":
                    placeConfidence = Enum.valueOf(PlaceVisit.PlaceConfidence.class,
                            reader.nextString());
                    break;
                case "childVisits":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        childVisits.add(parsePlaceVisit(reader));
                    }
                    reader.endArray();
                    break;
                case "centerLatE7":
                    long lat = reader.nextLong();
                    centerLatE7 = lat;
                    break;
                case "centerLngE7":
                    centerLngE7 = reader.nextLong();
                    break;
                case "visitConfidence":
                    visitConfidence = reader.nextInt();
                    break;
                case "otherCandidateLocations":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        otherCandidateLocations.add(parseLocation(reader));
                    }
                    reader.endArray();
                    break;
                case "editConfirmationStatus":
                    Enum.valueOf(PlaceVisit.EditConfirmationStatus.class,
                            reader.nextString());
                    break;
                case "simplifiedRawPath":
                    //Temporary
                    reader.skipValue();
                    break;
                default:
                    System.out.println("MapsJsonParser: Parsing Visit couldn't find name " + n + ". Went into " +
                            "default.");
                    throw new IllegalArgumentException("Parsing Visit couldn't find name " + n +
                            ". Went into default.");
            }
        }
        reader.endObject();
        return new PlaceVisit(location, duration, placeConfidence, centerLatE7, centerLngE7
                , visitConfidence, otherCandidateLocations, editConfirmationStatus, childVisits,
                null);
    }

    private Duration parseDuration(JsonReader reader) throws IOException {
        reader.beginObject();
        long startTimestampMs = 0;
        long endTimestampMs = 0;
        while (reader.hasNext()) {
            String n = reader.nextName();
            switch (n) {
                case "startTimestampMs":
                    startTimestampMs = reader.nextLong();
                    break;
                case "endTimestampMs":
                    endTimestampMs = reader.nextLong();
                    break;
                default:
                    System.out.println("MapsJsonReader: Parsing Event couldn't find name. Went into default.");
                    break;
            }
        }
        reader.endObject();
        return Duration.between(Instant.ofEpochMilli(startTimestampMs),
                Instant.ofEpochMilli(endTimestampMs));
    }

    private Location parseLocation(JsonReader reader) throws IOException {
        reader.beginObject();

        long latitudeE7 = 0;
        long longitudeE7 = 0;
        String placeId = "";
        String address = "";
        String name = "";
        SourceInfo sourceInfo = null;
        double locationConfidence = 0;
        Location.SemanticType semanticType = null;

        while (reader.hasNext()) {
            String n = reader.nextName();
            switch (n) {
                case "latitudeE7":
                    latitudeE7 = reader.nextLong();
                    break;
                case "longitudeE7":
                    longitudeE7 = reader.nextLong();
                    break;
                case "placeId":
                    placeId = reader.nextString();
                    break;
                case "address":
                    address = reader.nextString();
                    break;
                case "name":
                    name = reader.nextString();
                    break;
                case "sourceInfo":
                    sourceInfo = parseSourceInfo(reader);
                    break;
                case "locationConfidence":
                    locationConfidence = reader.nextDouble();
                    break;
                case "semanticType":
                    semanticType = Enum.valueOf(Location.SemanticType.class,
                            reader.nextString());
                    break;
                default:
                    System.out.println("MapsJsonReader: Parsing Event couldn't find name " + n + ". Went into " +
                            "default.");
                    break;
            }
        }
        reader.endObject();
        return new Location(latitudeE7, longitudeE7, placeId, address, name, sourceInfo,
                locationConfidence, semanticType);
    }

    private SourceInfo parseSourceInfo(JsonReader reader) throws IOException {
        SourceInfo sourceInfo = null;
        reader.beginObject();
        reader.nextName();
        sourceInfo = new SourceInfo(reader.nextLong());
        reader.endObject();
        return sourceInfo;
    }
}
