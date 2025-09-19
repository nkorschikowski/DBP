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
                    System.out.println("Started: " + getterName);
                    if(value instanceof Number 
                    || value instanceof String 
                    || value instanceof Boolean 
                    || value instanceof Character 
                    || value instanceof java.util.Date
                    || value.getClass().isPrimitive()){
                        System.out.println("standard case: " + value.getClass());
                        System.out.println("getter: " + getter);
                        row[i] = (value == null) ? "null" : value.toString();
                    } else {
                        // System.out.println("class case: "+ value.getClass());
                        // clazz = value.getClass();
                        // System.out.println("Clazz: " + clazz);
                        // getter = clazz.getMethod(getterName);
                        // System.out.println("getter: " + getter);
                        // value = getter.invoke(value); // bischen wirr die Line ... :
                        // System.out.print("Ergebnis: " + value);
                        // // Value soll eine Primitive Variable (z.Bsp: int oder Integer)sei. 
                        // // Ist man in dieser Line gelandet WAR value aber ein Entity object welches hier als Parameter gegeben wird um eben 
                        // // daruas die tatsächliche Variable zu bekommen
                        Object realvalue = digdeeper(value,field);
                        System.out.println("class case: "+ realvalue.getClass());
                        System.out.println("realvalue = " + realvalue);

                        // Value soll eine Primitive Variable (z.Bsp: int oder Integer)sei. 
                        // Ist man in dieser Line gelandet WAR value aber ein Entity object welches hier als Parameter gegeben wird um eben 
                        // daruas die tatsächliche Variable zu bekommen
                        row[i] = (value == null) ? "null" : realvalue.toString();
                    }
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

    public static Object digdeeper(Object enitity, String field){
        try {
            Class<?> entityClass = enitity.getClass();
            Method getter = entityClass.getMethod("get_" + field);
            Object value = getter.invoke(enitity);
            return value;
        } catch (NoSuchMethodException nsme) {
            System.out.println("Method named " + "get_" + field  + " does not exist!");
                } catch (IllegalAccessException iae) {
            System.out.println("Illegal Access Exception");
        } catch (InvocationTargetException ite) {
            System.out.println("Invocation Target Exception");
        }
        return null;
    }
}