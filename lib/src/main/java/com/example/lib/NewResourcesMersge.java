package com.example.lib;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NewResourcesMersge extends ResourcesMerge {
    public NewResourcesMersge(File muDir, List<File> fatherDir) throws Exception {
        super(muDir, fatherDir);
    }

    private int currentMerge = -1;

    @Override
    public void merge() throws Exception {
        for (File muFile : muDir.listFiles()) {
            if (muFile.getName().startsWith("smali")) {
                muSmaliList.add(muFile);
            }
        }
        for (File father : fatherDir) {
            currentMerge++;
            filter(father);
        }
    }

    //分类
    private void filter(File father) throws Exception {
        File[] files = father.listFiles();
        for (File rootFile : files) {
            if (rootFile.getName().equals("original")) {

            } else if (rootFile.getName().equals("AndroidManifest.xml")) {
                //todo 合并清单文件
                mergeAndroidManifestXml(new File(muDir.getAbsolutePath(),rootFile.getName()),rootFile);
            } else if (rootFile.getName().startsWith("smali")) {
                //todo 合并smali文件
                mergeSmali(rootFile);
            } else {
                //todo 合并其他文件
                mergeOther(new File(muDir.getAbsolutePath(),rootFile.getName()),rootFile);
            }

        }
    }

    private void mergeOther(File outFile,File otherFile) throws IOException, DocumentException {
        if (otherFile.isFile()){
            //文件
            if (!outFile.exists()) {
                //不存在的文件直接copy
                copy(outFile, otherFile);
            }else{
                if (outFile.getName().endsWith(".xml")
                        && outFile.getParentFile().getName().startsWith("values")
                        && !outFile.getName().equals("public.xml")){
                  //values文件夹下的xml文件的合并
                    mergeXml(outFile,otherFile);
                }
            }
        }else{
            if (!outFile.exists()){
                outFile.mkdirs();//创建目录
            }
            //文件夹
            File[] files = otherFile.listFiles();
            for (File f:files){
                mergeOther(new File(outFile.getAbsolutePath(),f.getName()),f);
            }
        }
    }

    //合并xml文件
    private void mergeXml(File outFile, File otherFile) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();
        Document mDocument = saxReader.read(outFile);//解析母包AndroidManifest.xml
        Document fDocument = saxReader.read(otherFile);//解析模块包AndroidManifest.xml
        Element outRoot = mDocument.getRootElement();
        Element inRoot = fDocument.getRootElement();
        List<Element> inElements = inRoot.elements();
        List<Element> outElements = outRoot.elements();
        in:for (Element inElement:inElements){
            String inName = inElement.attribute("name").getValue();
            for (Element outElement:outElements){
                String outName = outElement.attribute("name").getValue();
                 if (inName.equals(outName)){
                     //相同的节点配置,以母包为准
                     continue in;
                 }
            }
            //走到这里说明母包中没有相同的节点
            outElements.add((Element) inElement.clone());
        }
        //回写xml
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(outFile),format);
        xmlWriter.write(mDocument);
        xmlWriter.close();
    }

    private List<File> muSmaliList = new ArrayList<>();

    //合并smali文件
    private void mergeSmali(File smaliFile) {
        File muSmaliFile = new File(muDir, "smali_classes" +( muSmaliList.size()+1));
        if (!muSmaliFile.exists()) {
            muSmaliFile.mkdirs();//在母包目录中创建与module中对应的smali文件夹
        }
        //拷贝smali
        copySmaliDir(muSmaliFile, smaliFile);
        muSmaliList.add(muSmaliFile);
    }

    //拷贝smali文件夹下的内容
    private void copySmaliDir(File muSmaliFile, File smaliFile) {
        File[] smaliRootFileList = smaliFile.listFiles();
        for (File smaliRootFile : smaliRootFileList) {
            File muFile = new File(muSmaliFile.getAbsolutePath(), smaliRootFile.getName());
            if (smaliRootFile.isDirectory()) {
                if (!muFile.exists()) {
                    muFile.mkdirs();
                }
                //进入下级目录
                copySmaliDir(muFile, smaliRootFile);
            } else {
                //是文件,需要校验母包中其他smali文件夹下是否有重复的文件
                String name = smaliRootFile.getName();
                if (!name.startsWith("R$") && !name.equals("R.smali")) {
                    if (!checkSmaliRepeat(smaliRootFile)) {
                        //执行文件拷贝
                        System.out.println("-----copy ---> " + muFile.getAbsolutePath());
                        copy(muFile, smaliRootFile);
                    }
                }
            }
        }
    }
    //copy .Smali文件.需要在读取过程中识别是否有资源id的应用,将其记录下来
    protected void copySmali(File outFile, File inFile) {
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new BufferedWriter(new FileWriter(outFile));
            reader = new BufferedReader(new FileReader(inFile));
            String line = null;
            while ((line = reader.readLine()) != null){
                writer.write(line);
                //todo 识别是否存在资源id的引用
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (writer!=null) {
                    writer.close();
                }
                if (reader != null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //扫描是否有重复的smali文件
    private boolean checkSmaliRepeat(File fSmali) {
        System.out.println("----checkSmaliRepeat----");
        File currentMergeFather = fatherDir.get(currentMerge);
        List<File> fileNmes = new LinkedList<>();
        File parentFile = fSmali;
        while (!parentFile.getAbsolutePath().equals(currentMergeFather.getAbsolutePath())) {
            fileNmes.add(0, parentFile);
            parentFile = parentFile.getParentFile();
        }
        fileNmes.remove(0);

        int currentLevel = 0;//当前目录层级
        File file = fileNmes.get(currentLevel);
        for (File muFile : muSmaliList) {
            File[] smaliRootList = muFile.listFiles();
           loverList:while (smaliRootList != null && smaliRootList.length > 0) {
                for (File rootFile : smaliRootList) {
                    if (file.getName().equals(rootFile.getName())) {
                        if (file.isDirectory()) {
                            if (rootFile.isDirectory()) {
                                smaliRootList = rootFile.listFiles();
                                file = fileNmes.get(++currentLevel);
                                continue loverList; //进入下级目录比对
                            }
                        } else {
                            System.out.println("----checkSmaliRepeat--   -------------> "+true+"  = "+rootFile.getName());
                            return true;//有重复
                        }
                    }
                }
                smaliRootList = null;
            }
        }
        System.out.println("----checkSmaliRepeat--   "+false);
        return false;
    }

}
