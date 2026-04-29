package com.coreboard.api.util;

public class FilterUtils {

    private FilterUtils() {}

    public static String like(String value) {
        if (value == null || value.isBlank()) return null;
        return "%" + value.trim().toLowerCase() + "%";
    }

    public static String startsWith(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim().toLowerCase() + "%";
    }

    public static String exact(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim().toLowerCase();
    }


}