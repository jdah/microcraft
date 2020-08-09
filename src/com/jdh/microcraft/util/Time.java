package com.jdh.microcraft.util;

public class Time {
    public static final long NS_PER_SECOND = 1000000000;
    public static final long NS_PER_MS = 1000000;

    public static final int TPS = 60;
    public static final long NS_PER_TICK = NS_PER_SECOND / TPS;

    public static long now() {
        return System.nanoTime();
    }
}
