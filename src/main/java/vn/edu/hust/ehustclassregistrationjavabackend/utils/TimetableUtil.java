package vn.edu.hust.ehustclassregistrationjavabackend.utils;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import vn.edu.hust.ehustclassregistrationjavabackend.config.MessageException;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;

import java.util.*;

@SuppressWarnings("DanglingJavadoc")
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

    public static void checkValidTimetableClass(List<Class> registedClasses) {

        Map<Class.Timetable, String> mapTimetableAndClassId = new HashMap<>();
        /**
         * Duyệt các class
         */
        for (Class cl : registedClasses) {
            /**
             * Map các thời khóa biểu trong class
             */
            List<Class.Timetable> timetables = TimetableUtil.toTimetable(cl.getTimetable());
            for (Class.Timetable timetable : timetables) {
                mapTimetableAndClassId.put(timetable, cl.getClassPK().getId());
            }
        }
        /**
         * Bắt đầu kiểm tra tkb: 2 vòng for check trùng lặp
         */
        for (Map.Entry<Class.Timetable, String> timetable1 : mapTimetableAndClassId.entrySet()) {
            for (Map.Entry<Class.Timetable, String> timetable2 : mapTimetableAndClassId.entrySet()) {
                if (timetable1.getValue().equals(timetable2.getValue())) {
                    continue;
                }
                /**
                 * Khác thứ -> ok luôn
                 */
                if(!timetable2.getKey().getDayOfWeek().equals(timetable1.getKey().getDayOfWeek())){
                    continue;
                }
                /**
                 * Không trùng tuần -> ok luôn
                 */
                if(!conflictWeek(timetable1.getKey().getWeek(),timetable2.getKey().getWeek())){
                    continue;
                }
                /**
                 * Không trùng giờ -> ok luôn
                 */
                if (!conflictHour(timetable1.getKey().getFrom(), timetable2.getKey().getTo(), timetable2.getKey().getFrom(), timetable2.getKey().getTo())) {
                    continue;
                }
                /**
                 * Trùng thứ, trùng tuần, trùng giờ -> ko hợp lệ
                 */
                throw new MessageException("Trùng lịch học: "+timetable1.getValue()+", "+timetable2.getValue());
            }
        }
    }

    public static boolean conflictHour(String from1, String to1, String from2, String to2) {
        int timeStart1 = convertToNumberValue(from1);
        int timeEnd1 = convertToNumberValue(to1);
        int timeStart2 = convertToNumberValue(from2);
        int timeEnd2 = convertToNumberValue(to2);

//        if (timeStart2 >= timeEnd1) {
//            return false;
//        }
        return timeStart2 < timeEnd1 && timeStart1 < timeEnd2;

    }

    private static int convertToNumberValue(String hourMinute) {
        String[] a = hourMinute.trim().split(":");
        String hour = hourMinute.substring(0, 2);
        String minute = hourMinute.substring(2);
        return Integer.parseInt(hour) * 60 + Integer.parseInt(minute);
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
