package com.sallylshi;

public class Point {
    long latE7;
    long lngE7;
    long timestampMs;
    int accuracyMeters;

    Point(long latE7, long lngE7, long timestampMs, int accuracyMeters) {
        this.latE7 = latE7;
        this.lngE7 = lngE7;
        this.timestampMs = timestampMs;
        this.accuracyMeters = accuracyMeters;
    }
}