package com.mccraftaholics.warpportals.remote;

public class RemoteConstants {
    public static final long TICKS_PER_HOUR = 20 * 60 * 60;
    public static final long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

    public static final String BASE_API_URL() {
        return System.getenv("WARPPORTALS_DEBUG") == null ? "http://mc-craftaholics.com:4242/api" : "http://localhost:4242/api";
    }
}
