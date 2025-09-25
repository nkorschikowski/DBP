package com.uni;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.uni.entities.Kategorie;

import hu.webarticum.treeprinter.SimpleTreeNode;

public class Tablefier {

    public static void printTable(List<?> objects, List<String> fieldNames) throws Exception {
        if (objects == null || objects.isEmpty()) {
            System.out.println("(no results)");
            return;
        }

        Class<?> clazz = objects.get(0).getClass();
        // System.out.println("CLASS: " + clazz); //DEBUG
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
                    // System.out.println("Started: " + getterName); // DEBUG
                    if(value instanceof Number 
                    || value instanceof String 
                    || value instanceof Boolean 
                    || value instanceof Character 
                    || value instanceof java.util.Date
                    || value.getClass().isPrimitive()){
                        // System.out.println("standard case: " + value.getClass());// DEBUG 
                        // System.out.println("getter: " + getter); // DEBUG 
                        row[i] = (value == null) ? "null" : value.toString();
                    } else {
                        Object realvalue = digdeeper(value,field);
                        // System.out.println("class case: "+ realvalue.getClass()); // DEBUG 
                        // System.out.println("realvalue = " + realvalue); // DEBUG 

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
    
     public void printTree(Kategorie wurzel){
        String label = wurzel.get_name() != null ? wurzel.get_name() : "<unnamed>";
        SimpleTreeNode rootNode = new SimpleTreeNode(label);
            
        
        for (Kategorie children : wurzel.get_Unterkategorien()){
            label = wurzel.get_name() != null ? wurzel.get_name() : "<unnamed>";
            SimpleTreeNode node = new SimpleTreeNode(label);
           

        }
        
    }

    public SimpleTreeNode convertToTreeNode(Kategorie kategorie) {
        String name = kategorie.get_name() != null ? kategorie.get_name() : "<unnamed>";
        SimpleTreeNode node = new SimpleTreeNode(name);
        for (Kategorie child : kategorie.get_Unterkategorien()) {
            node.addChild(convertToTreeNode(child));
        }
        return node;
    }
}
