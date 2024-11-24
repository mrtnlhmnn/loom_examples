package de.accso.part3_structuredconcurrency.util;

import java.time.Clock;
import java.time.Duration;
import java.util.Random;

public final class TimeHelper {
    public static void printTimed(String text) {
        System.err.printf("[%s] [%s] %s%n", "" + Clock.systemUTC().millis(),
                Thread.currentThread().getName(),
                text);
    }

    public static void randomPause(int waitMinInMs, int waitMaxInMs) {
        Duration pauseInMs = Duration.ofMillis( new Random().nextLong(waitMinInMs, waitMaxInMs) );
        try {
            Thread.sleep( pauseInMs );
        }
        catch (InterruptedException ignored) {}
    }
}
