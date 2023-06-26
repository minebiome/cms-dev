package com.wangyang.util;

import com.alibaba.fastjson.JSONObject;
import com.wangyang.pojo.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


@Slf4j
public class ZipHelper {
    public static void zipUncompress(File srcFile,String destDirPath)  {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            //创建压缩文件对象
            ZipFile zipFile = new ZipFile(srcFile);
            //开始解压
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    srcFile.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDirPath + "/" + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    is = zipFile.getInputStream(entry);
                    fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
                    fos.close();
                    is.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is!=null){
                    is.close();
                }
                if(fos!=null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readFiles(String inputFile) throws Exception {
        File srcFile = new File(inputFile);
        if (srcFile.isDirectory()) {
            File next[] = srcFile.listFiles();
            for (int i = 0; i < next.length; i++) {
                System.out.println(next[i].getName());
                if(!next[i].isDirectory()){
                    BufferedReader br = new BufferedReader(new FileReader(next[i]));
                    List<String> arr1 = new ArrayList<>();
                    String contentLine ;
                    while ((contentLine = br.readLine()) != null) {
                        JSONObject js = JSONObject.parseObject(contentLine);
                        arr1.add(contentLine);
                    }
                    System.out.println(arr1);
                }

            }
        }
    }

    public static  List<FileDTO> listPath(String strPath){
        Path path = Paths.get(strPath);
        List<FileDTO> list = new ArrayList<>();
        findFileList(path.toFile(),list);
        return list;
    }

    public static void findFileList(File dir, List<FileDTO> fileNames) {
        if (!dir.exists() || !dir.isDirectory()) {// 判断是否存在目录
            return;
        }
        String[] files = dir.list();// 读取目录下的所有目录文件信息
        for (int i = 0; i < files.length; i++) {// 循环，添加文件名或回调自身
            File file = new File(dir, files[i]);
            if (file.isFile()) {// 如果文件
                FileDTO fileDTO = new FileDTO();
                fileDTO.setAbsolutePath(dir + File.separator + file.getName());
                fileDTO.setFileName(file.getName());
                fileNames.add(fileDTO);// 添加文件全路径名
            } else {// 如果是目录
                findFileList(file, fileNames);// 回调自身继续查询
            }
        }
    }

    public static boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }

        return dirFile.delete();
    }

    public static Properties getProperties(File file){
        //加载配置文件
        try (FileInputStream in = new FileInputStream(file)){
            Properties pro = new Properties();
            pro.load(in);
            return pro;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
