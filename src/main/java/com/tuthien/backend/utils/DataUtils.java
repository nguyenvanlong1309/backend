package com.tuthien.backend.utils;

public class DataUtils {

    public static Integer safeToInt(Object data) {
        return safeToDouble(data).intValue();
    }

    public static Double safeToDouble(Object data) {
        return safeToDouble(data, 0.0);
    }

    private static Double safeToDouble(Object data, Double defaultValue) {
        try {
            return Double.valueOf(data.toString());
        } catch (Exception exception) {
            return defaultValue;
        }
    }
}
