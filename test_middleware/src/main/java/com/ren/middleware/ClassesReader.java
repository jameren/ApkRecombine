package com.ren.middleware;

import android.content.Context;


import java.io.File;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;

/**
 * 类读取器<br/>
 * 读取某个包下所有类<br/>
 */
public final class ClassesReader {

    /**
     * 获取应用程序下的所有Dex文件
     *
     * @param context 上下文
     * @return Set<DexFile>
     */
    public static Set<DexFile> applicationDexFile(Context context) {
        return applicationDexFile(context.getPackageCodePath());
    }

    /**
     * 获取应用程序下的所有Dex文件
     *
     * @param packageCodePath 包路径
     * @return Set<DexFile>
     */
    public static Set<DexFile> applicationDexFile(String packageCodePath) {
        Set<DexFile> dexFiles = new HashSet<>();
        File dir = new File(packageCodePath).getParentFile();
        File[] files = dir.listFiles();
        for (File file : files) {
            try {
                String absolutePath = file.getAbsolutePath();
                if (!absolutePath.contains(".")) continue;
                String suffix = absolutePath.substring(absolutePath.lastIndexOf("."));
                if (!suffix.equals(".apk")) continue;
                DexFile dexFile = createDexFile(file.getAbsolutePath());
                if (dexFile == null) continue;
                dexFiles.add(dexFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dexFiles;
    }

    /**
     * 创建DexFile文件
     *
     * @param path 路径
     * @return DexFile
     */
    public static DexFile createDexFile(String path) {
        try {
            return new DexFile(path);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取类路径下的所有类
     *
     * @param packageName 包名
     * @param context     包路径
     * @return List<Class>
     */
    public static List<Class<?>> reader(String packageName, Context context) {
        return reader(packageName, context.getPackageCodePath());
    }

    public static <T> T scanFirstSon(Context context,Class<T> tClass) {
        T son = null;
        List<Class<T>> classes = new ArrayList<>();
        Set<DexFile> dexFiles = applicationDexFile(context.getPackageCodePath());
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (DexFile dexFile : dexFiles) {
            if (dexFile == null) continue;
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                try {
                    String currentClassPath = entries.nextElement();
                    if (currentClassPath == null || currentClassPath.isEmpty()) {
                        continue;
                    }
                    Class<?> entryClass = Class.forName(currentClassPath, true, classLoader);
                    if (entryClass == null) {
                        continue;
                    }
                    if (tClass.isAssignableFrom(entryClass)) {
                        if (!entryClass.isInterface() && !Modifier.isAbstract(entryClass.getModifiers())) {
                            classes.add((Class<T>) entryClass);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (classes.size()>0){
            try {
                Class<? extends T> classSon = classes.get(0);
                son = (T) classSon.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return son;
    }

    /**
     * 读取类路径下的所有类
     *
     * @param packageName     包名
     * @param packageCodePath 上下文
     * @return List<Class>
     */
    public static List<Class<?>> reader(String packageName, String packageCodePath) {
        List<Class<?>> classes = new ArrayList<>();
        Set<DexFile> dexFiles = applicationDexFile(packageCodePath);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (DexFile dexFile : dexFiles) {
            if (dexFile == null) continue;
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                try {
                    String currentClassPath = entries.nextElement();
                    if (currentClassPath == null || currentClassPath.isEmpty() || currentClassPath.indexOf(packageName) != 0)
                        continue;
                    Class<?> entryClass = Class.forName(currentClassPath, true, classLoader);
                    if (entryClass == null) continue;
                    classes.add(entryClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }

}