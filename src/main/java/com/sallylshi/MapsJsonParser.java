package com.sallylshi;

import java.io.*;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import com.google.gson.stream.JsonReader;
import javafx.util.Pair;

public class MapsJsonParser {
    Location location;
    Duration duration;

    void read(JsonReader reader) {
        try {
            ArrayList<PlaceVisit> placeVisits = new ArrayList<>();
            ArrayList<ActivitySegment> activitySegments = new ArrayList<>();
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
                        activitySegments.add(parseActivitySegment(reader));
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
                stat.addBatch("insert into placevisit values " + p.generateSqlString() + ";");
            }
            stat.executeBatch();
            ResultSet rs = stat.executeQuery("select * from placevisit;");
            while (rs.next()) {
                System.out.println("latitude = " + rs.getLong("lat"));
                System.out.println("longitude = " + rs.getLong("long"));
                System.out.println("placeId = " + rs.getString("placeId"));
                System.out.println("address = " + rs.getString("address"));
                System.out.println("name = " + rs.getString("name"));
                System.out.println("deviceTag = " + rs.getLong("deviceTag"));
                System.out.println("durationMs = " + rs.getLong("durationMs"));
                System.out.println("startTimestampMs = " + rs.getLong("startTimestampMs"));
            }
            conn.close();
            System.out.println("Successfully executed batch for placevisit.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private ActivitySegment parseActivitySegment(JsonReader reader) {
        ActivitySegment activitySegment = null;
        try {
            Location startLocation = null;
            Location endLocation = null;
            Duration duration = null;
            long distance = 0;
            long startTimestampMs = 0;
            ActivitySegment.Confidence confidence = null;
            ActivitySegment.ActivityType activityType = null;
            ArrayList<Activity> activities = new ArrayList<>();
            ArrayList<Waypoint> waypointPath = new ArrayList<>();
            ArrayList<Point> simplifiedRawPath = new ArrayList<>();
            ArrayList<Location> transitPath = new ArrayList<>();

            reader.beginObject();
            while (reader.hasNext()) {
                String n = reader.nextName();
                switch (n) {
                    case "startLocation":
                        startLocation = parseLocation(reader);
                        break;
                    case "endLocation":
                        endLocation = parseLocation(reader);
                        break;
                    case "duration":
                        Pair<Duration, Long> pair = parseDurationAndTimestamp(reader);
                        duration = pair.getKey();
                        startTimestampMs = pair.getValue();
                        break;
                    case "distance": //meters
                        distance = reader.nextLong();
                        break;
                    case "confidence":
                        confidence = Enum.valueOf(ActivitySegment.Confidence.class, reader.nextString());
                        break;
                    case "activityType":
                        activityType = Enum.valueOf(ActivitySegment.ActivityType.class, reader.nextString());
                        break;
                    case "activities":
                        reader.beginArray();
                        while (reader.hasNext()) {
                            activities.add(parseActivity(reader));
                        }
                        reader.endArray();
                        break;
                    case "waypointPath":
                        reader.beginObject();
                        reader.nextName();
                        reader.beginArray();
                        while (reader.hasNext()) {
                            waypointPath.add(parseWaypoint(reader));
                        }
                        reader.endArray();
                        reader.endObject();
                        break;
                    case "simplifiedRawPath":
                        reader.beginObject();
                        reader.nextName();
                        reader.beginArray();
                        while (reader.hasNext()) {
                            simplifiedRawPath.add(parsePoint(reader));
                        }
                        reader.endArray();
                        reader.endObject();
                        break;
                    case "editConfirmationStatus":
                        reader.skipValue();
                        break;
                    case "transitPath":
                        reader.beginObject();
                        while(reader.hasNext()) {
                            String o = reader.nextName();
                            switch(o) {
                                case "transitStops":
                                reader.beginArray();
                                while (reader.hasNext()) {
                                    transitPath.add(parseLocation(reader));
                                }
                                reader.endArray();
                                break;
                                case "name":
                                case"hexRgbColor":
                                    reader.skipValue();
                                    break;
                                default:
                                    System.out.println("MapsJsonParser: Parsing transit path couldn't find name " + o + ". Went into " +
                                            "default.");
                                    throw new IllegalArgumentException("Parsing transit path couldn't find name " + o +
                                            ". Went into default.");
                            }
                        }
                        reader.endObject();
                        break;
                    default:
                        System.out.println("MapsJsonParser: Parsing Activity Segment couldn't find name " + n + ". Went into " +
                                "default.");
                        throw new IllegalArgumentException("Parsing Activity Segment couldn't find name " + n +
                                ". Went into default.");
                }
            }
            reader.endObject();
            activitySegment = new ActivitySegment(startLocation, endLocation, duration, distance, confidence, activityType, activities, waypointPath, simplifiedRawPath, startTimestampMs);
        } catch (IOException e) {
            System.out.println("MapsJsonParse: IOException at parseActivitySegment");
        }
        return activitySegment;
    }

    private Point parsePoint(JsonReader reader) {
        Point point = null;
        try {
            reader.beginObject();
            long latE7 = 0;
            long lngE7 = 0;
            long timestampMs = 0;
            int accuracyMeters = 0;
            while(reader.hasNext()) {
                String n = reader.nextName();
                switch (n) {
                    case "latE7":
                        latE7 = reader.nextLong();
                        break;
                    case "lngE7":
                        lngE7 = reader.nextLong();
                        break;
                    case "timestampMs":
                        timestampMs = reader.nextLong();
                        break;
                    case "accuracyMeters":
                        accuracyMeters = reader.nextInt();
                        break;
                    default:
                        System.out.println("MapsJsonParser: Parsing Point couldn't find name " + n + ". Went into " +
                                "default.");
                        throw new IllegalArgumentException("Parsing Point couldn't find name " + n +
                                ". Went into default.");
                }
            }
            reader.endObject();
            point = new Point(latE7, lngE7, timestampMs, accuracyMeters);
        } catch (IOException e) {
            System.out.println("MapsJsonParse: IOException at parsePoint");
        }
        return point;
    }

    private Waypoint parseWaypoint(JsonReader reader) {
        Waypoint waypoint = null;
        try {
            long latE7 = 0;
            long lngE7 = 0;
            reader.beginObject();

            while (reader.hasNext()) {
                String n = reader.nextName();
                switch (n) {
                    case "latE7":
                        latE7 = reader.nextLong();
                        break;
                    case "lngE7":
                        lngE7 = reader.nextLong();
                        break;
                    default:
                        System.out.println("MapsJsonParser: Parsing Waypoint couldn't find name " + n + ". Went into " +
                                "default.");
                        throw new IllegalArgumentException("Parsing Waypoint couldn't find name " + n +
                                ". Went into default.");
                }
            }
            reader.endObject();
            waypoint = new Waypoint(latE7, lngE7);
        } catch (IOException e) {
            System.out.println("MapsJsonParse: IOException at parsePoint");
        }
        return waypoint;
    }

    private Activity parseActivity(JsonReader reader) {
        ActivitySegment.ActivityType activityType = null;
        Activity activity = null;
        double probability = 0;
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String n = reader.nextName();
                switch (n) {
                    case "activityType":
                        activityType = Enum.valueOf(ActivitySegment.ActivityType.class, reader.nextString());
                        break;
                    case "probability":
                        probability = reader.nextDouble();
                        break;
                    default:
                        System.out.println("MapsJsonParser: Parsing Activity couldn't find name " + n + ". Went into " +
                                "default.");
                        throw new IllegalArgumentException("Parsing Activity couldn't find name " + n +
                                ". Went into default.");
                }
            }
            reader.endObject();
            activity = new Activity(activityType, probability);
        } catch (IOException e) {
            System.out.println("MapsJsonParse: IOException at parseActivities");
        }
        return activity;
    }

    private PlaceVisit parsePlaceVisit(JsonReader reader) {
        PlaceVisit placeVisit = null;
        try {
            PlaceVisit.PlaceConfidence placeConfidence = null;
            long centerLatE7 = 0;
            long centerLngE7 = 0;
            int visitConfidence = 0;
            long startTimestampMs = 0;
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
                        Pair<Duration, Long> pair = parseDurationAndTimestamp(reader);
                        duration = pair.getKey();
                        startTimestampMs = pair.getValue();
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

            placeVisit = new PlaceVisit(location, duration, placeConfidence, centerLatE7, centerLngE7
                    , visitConfidence, otherCandidateLocations, editConfirmationStatus, childVisits,
                    null, startTimestampMs);
        } catch (IOException e) {
            System.out.println("MapsJsonReader: IOException from parsePlaceVisit");
        }
        return placeVisit;
    }

    private Pair<Duration, Long> parseDurationAndTimestamp(JsonReader reader) throws IOException {
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
        return new Pair<>(Duration.between(Instant.ofEpochMilli(startTimestampMs),
                Instant.ofEpochMilli(endTimestampMs)), startTimestampMs);
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
                    name = reader.nextString().replaceAll("\\\"", "");
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
