package com.sallylshi;

public class Location {
    enum SemanticType {
        TYPE_SEARCHED_ADDRESS, TYPE_HOME, TYPE_WORK;
    }
    long latitudeE7;
    long longitudeE7;
    String placeId;
    String address;
    String name;
    SourceInfo sourceInfo;
    double locationConfidence;
    SemanticType semanticType;

    public Location(long latitudeE7, long longitudeE7, String placeId, String address, String name, SourceInfo sourceInfo, double locationConfidence, SemanticType semanticType) {
        this.latitudeE7 = latitudeE7;
        this.longitudeE7 = longitudeE7;
        this.placeId = placeId;
        this.address = address;
        this.name = name;
        this.sourceInfo = sourceInfo;
        this.locationConfidence = locationConfidence;
        this.semanticType = semanticType;
    }
}
