package com.uni;

import java.lang.reflect.*;
import java.util.*;

public class Tablefier {

    public static void printTable(List<?> objects, List<String> fieldNames) throws Exception {
        if (objects == null || objects.isEmpty()) {
            System.out.println("(no results)");
            return;
        }

        Class<?> clazz = objects.get(0).getClass();
        System.out.println("DEGCLASS: " + clazz); //DEBUG
        int cols = fieldNames.size();

        // 1. Collect values as strings
        List<String[]> rows = new ArrayList<>();

        // Header
        rows.add(fieldNames.toArray(new String[0]));

        // Data
        for (Object obj : objects) {
            String[] row = new String[cols];
            for (int i = 0; i < cols; i++) {
                String field = fieldNames.get(i);

                // Try getter first (JavaBean convention)
                String getterName = "get_" + field;
                try {
                    Method getter = clazz.getMethod(getterName);
                    Object value = getter.invoke(obj);
                    row[i] = (value == null) ? "null" : value.toString();
                } catch (NoSuchMethodException e) {
                    row[i] = "(no getter)";
                }
            }
            rows.add(row);
        }

        // 2. Compute column widths
        int[] widths = new int[cols];
        for (String[] row : rows) {
            for (int i = 0; i < cols; i++) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }

        // 3. Print rows with padding
        for (String[] row : rows) {
            for (int i = 0; i < cols; i++) {
                System.out.printf("%-" + widths[i] + "s  ", row[i]);
            }
            System.out.println();
        }
    }
}