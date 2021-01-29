package com.example.lib;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 解析apk后需要合并资源的操作
 * */
public class ResourcesMerge {

    private File muDir;
    private List<File> fatherDir;

    public class ApkResNotExistsException extends Exception{
        public ApkResNotExistsException(String s) {
            super(s);
        }
    }
    public ResourcesMerge(File muDir, List<File> fatherDir) throws Exception {
        if (!muDir.exists()){
            throw new ApkResNotExistsException("母包资源目录不存在["+muDir.getAbsolutePath()+"]");
        }
        for (File f:fatherDir){
            if (!f.exists()){
                throw new ApkResNotExistsException("模块资源目录不存在["+f.getAbsolutePath()+"]");
            }
        }
        this.muDir = muDir;
        this.fatherDir = fatherDir;
    }

    public void merge(){
        for (File father:fatherDir){
            merge(muDir,father);
        }
    }

    //资源合并
    private void merge(File moFile,File faFile){
        String[] listFileNames = faFile.list();
        for (String fileName:listFileNames){
            // System.out.println(count+++" "+faFile.getAbsolutePath()+"  -->  "+fileName);
            File mFile = new File(moFile.getAbsolutePath(),fileName);
            if (fileName.equals("original")){
                continue;
            }
            if (mFile.exists() && mFile.isFile()){
                if (fileName.equals("AndroidManifest.xml")){
                    //合并清单文件
                    try {
                        mergeAndroidManifestXml(mFile,new File(faFile.getAbsolutePath(), fileName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (fileName.endsWith(".xml")){
                    //合并其他 xml 文件

                }
                continue;//其他已存在的文件以母包为主，不进行覆盖
            }

            File fFile = new File(faFile.getAbsolutePath(), fileName);
            if (!mFile.exists()) {
                if (fFile.isDirectory()){
                    mFile.mkdirs();//创建不存在的目录层（存在多级时同时创建）
                }
            }

            if (mFile.isDirectory()) {
                //文件夹继续递归进入下一层目录合并
                merge(mFile,fFile);
            }else{
                System.out.println("----copy ---> "+fFile.getAbsolutePath());
                //母包中不存在的文件，直接从模块包中copy补全
                copy(mFile, fFile);
            }
        }
    }

    //copy文件
    private void copy(File outFile, File inFile) {
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(outFile));
            in = new BufferedInputStream(new FileInputStream(inFile));
            byte[] bytes = new byte[2048];
            int len = 0;
            while ((len = in.read(bytes)) != -1){
                out.write(bytes,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (out!=null) {
                    out.close();
                }
                if (in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface CompletedElementFilter{
        boolean isSame(Element mElement, Element fElement);
    }
    /**
     * 合并清单文件
     * @param mFile 母包清单文件
     * @param fFile 模块包清单文件
     * 因其文件特殊性，对于配置的节点合并策略单独做处理
     * */
    private void mergeAndroidManifestXml(File mFile, File fFile) throws Exception{
        SAXReader saxReader = new SAXReader();
        Document mDocument = saxReader.read(mFile);//解析母包AndroidManifest.xml
        Document fDocument = saxReader.read(fFile);//解析模块包AndroidManifest.xml
        Element mManifest = mDocument.getRootElement();
        Element fManifest = fDocument.getRootElement();
        //补全 <manifest></manifest> 节点
        completedElement(mManifest, fManifest,null);
        //补全 <application></application> 节点
        Element mApplication = mManifest.element("application");
        Element fApplication = fManifest.element("application");
        completedElement(mApplication, fApplication, new CompletedElementFilter() {
            @Override
            public boolean isSame(Element mElement, Element fElement) {
                //过滤处理，返回值判断节点时否相同
                String mElementValue = mElement.attribute("name").getValue();
                String fElementValue = fElement.attribute("name").getValue();
                return mElementValue.equals(fElementValue);//相同的 name 不写入
            }
        });
        //回写xml
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(mFile),format);
        xmlWriter.write(mDocument);
        xmlWriter.close();
    }
    //补齐母包中缺失的节点
    private void completedElement(Element mElement, Element fElement, CompletedElementFilter filter) {
        List<Element> mManifestChildNodes = mElement.elements();
        List<Element> fManifestChildNodes = fElement.elements();
        f:for (int x =0 ;x < fManifestChildNodes.size();x++){
            Element fMItem = fManifestChildNodes.get(x);
            String fMNodeName = fMItem.getName();
            for (int y =0 ;y < mManifestChildNodes.size();y++){
                Element mMItem = mManifestChildNodes.get(y);
                //启动的activity以母包为准不进行任何操作
                boolean isActionMain = false;
                boolean isCategoryLauncher = false;
                if (fMItem.getName().equals("activity")){
                    List<Element> activityChilds = fMItem.elements();
                    for (Element el:activityChilds){
                        if (el.getName().equals("intent-filter")){
                            List<Element> intentFilterChilds = el.elements();
                            for (Element e:intentFilterChilds){
                                if (e.getName().equals("action") && e.attribute("name").getValue().equals("android.intent.action.MAIN")){
                                    isActionMain = true;
                                }
                                if (e.getName().equals("category") && e.attribute("name").getValue().equals("android.intent.category.LAUNCHER")){
                                    isCategoryLauncher = true;
                                }
                            }
                        }
                    }
                }
                if (isCategoryLauncher && isActionMain){
                    /**
                     * <intent-filter>
                     *         <action android:name="android.intent.action.MAIN"/>
                     *         <category android:name="android.intent.category.LAUNCHER"/>
                     *       </intent-filter>
                     * */
                    continue f;//扫描module下一个节点校验
                }
                boolean isSame;
                if (filter!=null){
                    //是否补全
                    isSame = filter.isSame(mMItem,fMItem);
                }else{
                    //默认校验节点名称
                    String mMNodeName = mMItem.getName();
                    isSame = fMNodeName.equals(mMNodeName);
                }
                if (isSame){
                    //扫描到了相同的节点
                    continue f;//扫描module下一个节点校验
                }
            }
            System.out.println("-------------- AndroidManifest.xml add "+fMItem.getName()+" "+fMItem.attribute("name"));
            //走到这里说明模块包中该节点元素需要添加到母包清单中
            mElement.add((Element) fMItem.clone());//补全母包中没有的节点
        }
    }
}
