//package com.king.kingcloud.test;
//
//import com.king.kingcloud.KingCloudApplication;
//import com.king.kingcloud.controllers.FileUploadController;
//import org.junit.Test;
//import org.junit.platform.commons.logging.Logger;
//import org.junit.platform.commons.logging.LoggerFactory;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//
//
///**
// * @program: kingcloud
// * @description:
// * @author: King
// * @create: 2021-06-04 00:45
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class FileTest {
//
//    @Autowired
//    private FileUploadController fileUploadController;
//
//    @Test
//    public void a(){
//        System.out.println(fileUploadController.getUploadPath());;
//    }
//    private static final Logger logger = LoggerFactory.getLogger(KingCloudApplication.class);
//    public static void main(String[] args) {
//
//        try {
//            new FileTest().showURL();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void showURL() throws IOException {
//
//        // 第一种：获取类加载的根路径   D:\git\daotie\daotie\target\classes
//        File f = new File(this.getClass().getResource("/").getPath());
//        System.out.println("path1: "+f);
//
//        // 获取当前类的所在工程路径; 如果不加“/”  获取当前类的加载目录  D:\git\daotie\daotie\target\classes\my
//        File f2 = new File(this.getClass().getResource("").getPath());
//        System.out.println("path1: "+f2);
//
//        // 第二种：获取项目路径    D:\git\daotie\daotie
//        File directory = new File("");// 参数为空
//        String courseFile = directory.getCanonicalPath();
//        System.out.println("path2: "+courseFile);
//
//
//        // 第三种：  file:/D:/git/daotie/daotie/target/classes/
//        URL xmlpath = this.getClass().getClassLoader().getResource("");
//        System.out.println("path3: "+xmlpath);
//
//        // 第四种： D:\git\daotie\daotie
//        System.out.println("path4:" +System.getProperty("user.dir"));
//        /*
//         * 结果： C:\Documents and Settings\Administrator\workspace\projectName
//         * 获取当前工程路径
//         */
//
//        // 第五种：  获取所有的类路径 包括jar包的路径
//        System.out.println("path5: "+System.getProperty("java.class.path").split(";")[0]);
//        // 第六种：  获取项目路径  D:/git/daotie/daotie.target/classes/
//        //System.out.println("path6: "+Thread.currentThread().getContentClassLoader().getResource("").getPath());         //第七种  表示到项目的根目录下, 要是想到目录下的子文件夹,修改"/"即可         String path7 = request.getSession().getServletContext().getRealPath("/"));         System.out.pringln("path7: "+path7);
//    }
//}
