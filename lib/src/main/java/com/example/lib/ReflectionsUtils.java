package com.example.lib;

import org.reflections.Reflections;

import java.util.Set;

public class ReflectionsUtils {

    public static <T> T scanFirstSon(String path, Class<T> tClass) {
        T son = null;
        Reflections reflections = new Reflections();
        Set<Class<? extends T>> sons = reflections.getSubTypesOf(tClass);
        if (sons.iterator().hasNext()) {
            try {
                Class<? extends T> homePageClass = sons.iterator().next();
                son = (T) homePageClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return son;
    }
}
