package com.example.lib;

import com.android.apksigner.ApkSignerTool;

public class ApkSignTool {

    /**
     * apk签名(v1)
     * */
    public static void signApkV1(String unsignedApkPath,String keystorePath,String alias,String password,String singedApkPath){
        String sign = "cmd.exe /c jarsigner -verbose -keystore "+keystorePath+" -signedjar "+singedApkPath+" "+unsignedApkPath+" "+alias+" -storepass "+password;
        String s = CMDUtil.excuteCMDCommand(sign);
    }

    /**
     * apk签名(v2)
     apksigner sign -verbose --ks J:\zshy\test\kirinlife.jks --v1-signing-enabled true --v2-signing-enabled true --ks-pass pass:kirinlife123 --ks-key-alias kirinlife --key-pass pass:kirinlife123 --
     out 签名apk输出路径 需要签名的apk
     * */
    public static void signApkV1V2(String unsignedApkPath,String keystorePath,String alias,String password,String singedApkPath) throws Exception {
        String sign = "apksigner sign -verbose --ks "+keystorePath+" --v1-signing-enabled true --v2-signing-enabled true --ks-pass pass:"+password+" --ks-key-alias "+alias+" --key-pass pass:"+password+
                " --out "+singedApkPath+" "+unsignedApkPath;
        //String s = CMDUtil.excuteCMDCommand(sign);
        ApkSignerTool.main(new String[]{"sign","-verbose","--ks",keystorePath,"--v1-signing-enabled","true","--v2-signing-enabled","true"
                ,"--ks-pass","pass:"+password,"--ks-key-alias",alias,"--key-pass","pass:"+password,"--out",singedApkPath,unsignedApkPath});
    }
}
