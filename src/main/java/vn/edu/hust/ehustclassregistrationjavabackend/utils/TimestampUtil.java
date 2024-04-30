package vn.edu.hust.ehustclassregistrationjavabackend.utils;

import java.sql.Timestamp;

public class TimestampUtil {
    public static Timestamp getNow() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setNanos((int) System.nanoTime() % 1000000000);
        return timestamp;
    }
}
