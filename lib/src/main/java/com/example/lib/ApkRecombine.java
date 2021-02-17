package com.example.lib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import brut.androlib.Androlib;
import brut.androlib.ApkOptions;
import brut.common.BrutException;

/**
 * apk重组生成新的子包
 * */
public class ApkRecombine {
    private String muApkPath;
    /**
     * @param muApkPath 母包apk路径
     * */
    public ApkRecombine(String muApkPath) {
        this.muApkPath = muApkPath;
    }

    public String startRecombine(List<String> moduleApkPath) throws Exception{
        //1：解析apk
        //1-1：反编译解析母包
        File muDir = new File(muApkPath.substring(0, muApkPath.lastIndexOf(".apk")));
        if (muDir.exists()){
            muDir.delete();
        }
        brut.apktool.Main.main(new String[]{"apktool","d",muApkPath,"-o",muDir.getAbsolutePath()});
        //1-2：反编译解析module包
        ArrayList<File> fatherFiles = new ArrayList<>();
        for (String fatherPath:moduleApkPath){
            File fDir = new File(fatherPath.substring(0, fatherPath.lastIndexOf(".apk")));
            if (fDir.exists()){
                fDir.delete();
            }
            brut.apktool.Main.main(new String[]{"apktool","d",fatherPath,"-o",fDir.getAbsolutePath()});
            fatherFiles.add(fDir);
        }

        //2:合并资源
        ResourcesMerge resourcesMerge = new NewResourcesMersge(muDir, fatherFiles);
        resourcesMerge.merge();

        //3:将合并后的资源回编译成apk
        ApkOptions apkOptions = new ApkOptions();
        File unsignedApk = new File(muDir.getParent() + "/" + muDir.getName() + "_unsigned.apk");
        apkOptions.useAapt2 = true;
        new Androlib(apkOptions).build(muDir,unsignedApk);

        // brut.apktool.Main.main(new String[]{"apktool","b","--use-aapt2",muDir.getAbsolutePath(),"-o",unsignedApk.getAbsolutePath()});
        //4:再次反编译已经合并重组的apk
       /* String unsignedPath = unsignedApk.getAbsolutePath();
        File unsignedDir = new File(unsignedPath.substring(0, unsignedPath.lastIndexOf(".apk")));
        if (unsignedDir.exists()){
            unsignedDir.delete();
        }
        brut.apktool.Main.main(new String[]{"apktool","d",unsignedPath,"-o",unsignedDir.getAbsolutePath()});*/
        //读取aapt生成的id资源配置文件 public.xml.并合并代码.smali文件


        return unsignedApk.getAbsolutePath();//返回生成好的apk路径
    }


}
