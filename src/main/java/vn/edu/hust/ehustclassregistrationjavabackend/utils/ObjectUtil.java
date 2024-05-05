package vn.edu.hust.ehustclassregistrationjavabackend.utils;

import java.lang.reflect.Method;

public class ObjectUtil {
    public static void mergeEntityWithoutNullFieldAndId(Object obj, Object update) {
        if (!obj.getClass().isAssignableFrom(update.getClass())) {
            return;
        }

        Method[] methods = obj.getClass().getMethods();

        for (Method fromMethod : methods) {
            System.out.println(fromMethod.getName());
            if (fromMethod.getDeclaringClass().equals(obj.getClass())
                    && fromMethod.getName().startsWith("get") && !fromMethod.getName().startsWith("getId")) {

                String fromName = fromMethod.getName();
                String toName = fromName.replace("get", "set");

                try {
                    Method toMetod = obj.getClass().getMethod(toName, fromMethod.getReturnType());
                    Object value = fromMethod.invoke(update, (Object[]) null);
                    if (value != null) {
                        toMetod.invoke(obj, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}
