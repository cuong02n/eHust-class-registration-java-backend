package vn.edu.hust.ehustclassregistrationjavabackend.utils;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.cglib.core.Local;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;

import java.time.LocalTime;
import java.util.*;

public class TimetableUtil {
    public static List<Class.Timetable> toTimetable(String timetableString) {
        TypeToken<List<Class.Timetable>> typeToken = new TypeToken<>() {
        };
        return GsonUtil.gsonExpose.fromJson(timetableString, typeToken);
    }

    public static String toString(List<Class.Timetable> timetables) {
        return GsonUtil.gsonExpose.toJson(timetables);
    }

    public static JsonElement toJsonElement(List<Class.Timetable> timetable) {
        return GsonUtil.gsonExpose.toJsonTree(timetable);
    }

    public static JsonElement toJsonElement(String timetableString) {
        return toJsonElement(toTimetable(timetableString));
    }

    public static boolean availableTimetableStudent(List<Class.Timetable> existings, List<Class.Timetable> newTimetables) {
        for (Class.Timetable newTime : newTimetables) {
            for (Class.Timetable exist : existings) {
                if (!newTime.getDayOfWeek().equals(exist.getDayOfWeek())) {
                    continue;
                }
                if (!conflictWeek(newTime.getWeek(), exist.getWeek())) {
                    continue;
                }
                if (!conflictHour(newTime.getFrom(), newTime.getTo(), exist.getFrom(), exist.getTo())) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    public static boolean conflictHour(String from1, String to1, String from2, String to2) {
        int timeStart1 = convertToNumberValue(from1);
        int timeEnd1 = convertToNumberValue(to1);
        int timeStart2 = convertToNumberValue(from2);
        int timeEnd2 = convertToNumberValue(to2);

        if (timeStart2 >= timeEnd1) {
            return false;
        }
        return timeStart1 < timeEnd2;

    }

    public static int convertToNumberValue(String hourMinute) {
        String[] a = hourMinute.trim().split(":");
        return Integer.parseInt(a[0]) * 60 + Integer.parseInt(a[1]);
    }

    public static boolean conflictWeek(String week1, String week2) {
        Set<Integer> week1s = parseWeek(week1);
        int size1 = week1s.size();
        Set<Integer> week2s = parseWeek(week2);
        int size2 = week2s.size();
        week1s.addAll(week2s);
        int newSize = week1s.size();
        return newSize != size1 + size2;
    }

    public static Set<Integer> parseWeek(String week) {
        Set<Integer> weeks = new HashSet<>();
        String[] w = week.split(",");
        for (int i = 0; i < w.length; i++) {
            w[i] = w[i].trim();
            if (w[i].contains("-")) {
                String[] intervals = w[i].split("-");
                int start = Integer.parseInt(intervals[0].trim());
                int end = Integer.parseInt(intervals[1].trim());
                for (int j = start; j <= end; j++) {
                    weeks.add(j);
                }
            } else {
                weeks.add(Integer.parseInt(w[i]));
            }
        }
        return weeks;
    }
}
