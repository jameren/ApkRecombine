package com.example.lib;

import com.android.apksig.SigningCertificateLineage;
import com.android.apksigner.ApkSignerTool;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import brut.androlib.Androlib;
import brut.androlib.AndrolibException;
import brut.androlib.ApkDecoder;
import brut.androlib.ApkOptions;
import brut.common.BrutException;

public class MyClass {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String dir = "J:\\zshy\\test";//打包目录空间
        try {
            //1：将母包与模块包进行解析合并重组打包
            ApkRecombine apkRecombine = new ApkRecombine(dir + "/app-debug.apk");
            ArrayList<String> modules = new ArrayList<>();
            modules.add(dir + "/homelibrary-debug.apk");
            modules.add(dir + "/loginlibrary-debug.apk");
            String unsignedApkPath = apkRecombine.startRecombine(modules);
            //2: 给apk签名
            ApkSignTool.signApk(
                    unsignedApkPath//需要签名的apk路径
                    ,dir+"/kirinlife.keystore"//keystore路径
                    ,"kirinlife"//别名
                    ,"kirinlife123"//密码
                    ,dir+"/sonNew.apk"//新的apk路径（已签名）
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(" ---->  "+(endTime-startTime));
    }

    private static void decodeApk(String apkName,String outPutDir) {
        ApkDecoder decoder = new ApkDecoder();
        try {
            decoder.setOutDir(new File("H:/zshy/app-debug"));
            decoder.setApkFile(new File("H:/zshy/app-debug.apk"));
            decoder.decode();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                decoder.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}