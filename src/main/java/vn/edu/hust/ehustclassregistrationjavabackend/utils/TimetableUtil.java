package vn.edu.hust.ehustclassregistrationjavabackend.utils;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;

import java.util.List;

public class TimetableUtil {
    public static List<Class.Timetable> toTimetable(String timetableString){
        TypeToken<List<Class.Timetable>> typeToken = new TypeToken<>(){};
        return GsonUtil.gsonExpose.fromJson(timetableString, typeToken);
    }
    public static String toString(List<Class.Timetable> timetables){
        return GsonUtil.gsonExpose.toJson(timetables);
    }
    public static JsonElement toJsonElement(List<Class.Timetable> timetable){
        return GsonUtil.gsonExpose.toJsonTree(timetable);
    }
    public static JsonElement toJsonElement(String timetableString){
        return toJsonElement(toTimetable(timetableString));
    }
}
