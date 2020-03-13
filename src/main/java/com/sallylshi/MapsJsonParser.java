package com.sallylshi;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.stream.JsonParser;

public class MapsJsonParser {
    Location location;
    Duration duration;
    String placeConfidence;
    String centerLatE7;
    String centerLngE7;
    double visitConfidence;
    ArrayList<Location> otherCandidateLocations;
    PlaceVisit.EditConfirmationStatus editConfirmationStatus;

    public static void main(String[] args) {
        MapsJsonParser mapsJsonParser = new MapsJsonParser();

        mapsJsonParser.processFile();
       // JsonParser parser = Json.createParser(new InputStreamReader(mapsJsonParser.processFile()));
       // mapsJsonParser.read(parser);
    }

    public FileInputStream processFile() {
        try {
            return new FileInputStream("/Users/sallyshi/IdeaProjects/MapsServer/src/resources/2020_JANUARY.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    String read(JsonParser reader) {
        System.out.println("read function started");

        ArrayList<PlaceVisit> placeVisits = new ArrayList<>();

        while (reader.hasNext()) {
            String n = reader.getString();
            System.out.println("read name is now " + n);
            switch (n) {
                case "placeVisit":
                    System.out.println("PlaceVIsit");
                    //placeVisits.add(parsePlaceVisit(reader));
                    break;
                case "activitySegment":
                    //Temporary
                    reader.skipObject();
                    break;
                default:
                    System.out.println("MapsJsonParser: Parsing overall couldn't find name " + n + ". Went into " +
                            "default.");
                    break;
            }
        }


//        for (PlaceVisit p : placeVisits) {
//            System.out.println(
//                    "PlaceVisit: " + p.location + p.duration + p.centerLngE7 + p.centerLatE7 +
//                            "List size is: " + placeVisits.size());
//        }

        return "YAY";
//    }
//
//    private PlaceVisit parsePlaceVisit(JsonParser reader) throws IOException {
//        PlaceVisit.PlaceConfidence placeConfidence = null;
//        long centerLatE7 = 0;
//        long centerLngE7 = 0;
//        int visitConfidence = 0;
//        ArrayList<Location> otherCandidateLocations = new ArrayList<>();
//        ArrayList<PlaceVisit> childVisits = new ArrayList<>();
//        PlaceVisit.EditConfirmationStatus editConfirmationStatus = null;
//        reader.beginObject();
//        while (reader.hasNext()) {
//            String n = reader.nextName();
//            System.out.println("SALLY name is " + n);
//            switch (n) {
//                case "location":
//                    parseLocation(reader);
//                    break;
//                case "duration":
//                    parseDuration(reader);
//                    break;
//                case "placeConfidence":
//                    placeConfidence = Enum.valueOf(PlaceVisit.PlaceConfidence.class,
//                            reader.nextString());
//                    break;
//                case "childVisits":
//                    reader.beginArray();
//                    while (reader.hasNext()) {
//                        childVisits.add(parsePlaceVisit(reader));
//                    }
//                    reader.endArray();
//                    break;
//                case "centerLatE7":
//                    long lat = reader.nextLong();
//                    System.out.println("centerLatE227 lattitude is " + lat);
//                    centerLatE7 = lat;
//                    break;
//                case "centerLngE7":
//                    centerLngE7 = reader.nextLong();
//                    break;
//                case "visitConfidence":
//                    visitConfidence = reader.nextInt();
//                    break;
//                case "otherCandidateLocations":
//                    reader.beginArray();
//                    while (reader.hasNext()) {
//                        otherCandidateLocations.add(parseLocation(reader));
//                    }
//                    reader.endArray();
//                    break;
//                case "editConfirmationStatus":
//                    Enum.valueOf(PlaceVisit.EditConfirmationStatus.class,
//                            reader.nextString());
//                    break;
//                case "simplifiedRawPath":
//                    //Temporary
//                    reader.skipValue();
//                    break;
//                default:
//                    System.out.println("MapsJsonParser: Parsing Visit couldn't find name " + n + ". Went into " +
//                            "default.");
//                    throw new IllegalArgumentException("Parsing Visit couldn't find name " + n +
//                            ". Went into default.");
//            }
//        }
//        reader.endObject();
//        return new PlaceVisit(location, duration, placeConfidence, centerLatE7, centerLngE7
//                , visitConfidence, otherCandidateLocations, editConfirmationStatus, childVisits,
//                null);
//    }
//
//    private Duration parseDuration(JsonParser reader) throws IOException {
//        reader.beginObject();
//        long startTimestampMs = 0;
//        long endTimestampMs = 0;
//        while (reader.hasNext()) {
//            String n = reader.nextName();
//            switch (n) {
//                case "startTimestampMs":
//                    startTimestampMs = reader.nextLong();
//                    break;
//                case "endTimestampMs":
//                    endTimestampMs = reader.nextLong();
//                    break;
//                default:
//                    System.out.println("MapsJsonParser: Parsing Event couldn't find name. Went into default.");
//                    break;
//            }
//        }
//        reader.endObject();
//        return Duration.between(Instant.ofEpochMilli(startTimestampMs),
//                Instant.ofEpochMilli(endTimestampMs));
//    }
//
//    private Location parseLocation(JsonParser reader) throws IOException {
//        reader.beginObject();
//
//        long latitudeE7 = 0;
//        long longitudeE7 = 0;
//        String placeId = "";
//        String address = "";
//        String name = "";
//        SourceInfo sourceInfo = null;
//        double locationConfidence = 0;
//        Location.SemanticType semanticType = null;
//
//        while (reader.hasNext()) {
//            String n = reader.nextName();
//            switch (n) {
//                case "latitudeE7":
//                    latitudeE7 = reader.nextLong();
//                    break;
//                case "longitudeE7":
//                    longitudeE7 = reader.nextLong();
//                    break;
//                case "placeId":
//                    placeId = reader.nextString();
//                    break;
//                case "address":
//                    address = reader.nextString();
//                    break;
//                case "name":
//                    name = reader.nextString();
//                    break;
//                case "sourceInfo":
//                    sourceInfo = parseSourceInfo(reader);
//                    break;
//                case "locationConfidence":
//                    locationConfidence = reader.nextDouble();
//                    break;
//                case "semanticType":
//                    semanticType = Enum.valueOf(Location.SemanticType.class,
//                            reader.nextString());
//                    break;
//                default:
//                    System.out.println("MapsJsonParser: Parsing Event couldn't find name " + n + ". Went into " +
//                            "default.");
//                    break;
//            }
//        }
//        reader.endObject();
//        return new Location(latitudeE7, longitudeE7, placeId, address, name, sourceInfo,
//                locationConfidence, semanticType);
//    }
//
//    private SourceInfo parseSourceInfo(JsonParser reader) throws IOException {
//        SourceInfo sourceInfo = null;
//        reader.beginObject();
//        reader.nextName();
//        sourceInfo = new SourceInfo(reader.nextLong());
//        reader.endObject();
//        return sourceInfo;
//    }
    }
}
