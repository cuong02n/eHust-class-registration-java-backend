package vn.edu.hust.ehustclassregistrationjavabackend.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
    public static final Gson gsonExpose = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    public static final Gson gson = new GsonBuilder().create();
}
