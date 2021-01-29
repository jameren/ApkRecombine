package com.example.lib;

public class ApkSignTool {

    /**
     * apk签名
     * */
    public static void signApk(String unsignedApkPath,String keystorePath,String alias,String password,String singedApkPath){
        String sign = "cmd.exe /c jarsigner -verbose -keystore "+keystorePath+" -signedjar "+singedApkPath+" "+unsignedApkPath+" "+alias+" -storepass "+password;
        String s = CMDUtil.excuteCMDCommand(sign);
    }
}
