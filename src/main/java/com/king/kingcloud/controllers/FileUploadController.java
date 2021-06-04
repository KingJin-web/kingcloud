package com.king.kingcloud.controllers;

import com.king.kingcloud.util.FileUtil;
import com.king.kingcloud.util.HdfsUtil;
import com.king.kingcloud.vo.JsonModel;
import org.apache.hadoop.fs.Path;
import org.codehaus.jettison.json.JSONString;
import org.mortbay.util.ajax.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Arrays;


/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-06-04 00:30
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Value(value = "${file.UploadPath}")
    private String UploadPath;
    @Autowired
    private HdfsUtil hdfsUtil;

    private JsonModel jm;

    @Autowired
    HttpSession session;

    public String getUploadPath() {
        return UploadPath;
    }

    /**
     * 实现文件上传
     */
    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    @ResponseBody
    public String fileUpload(@RequestParam("fileName") MultipartFile file) {
        if (file.isEmpty()) {
            return "false";
        }
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);


        File dest = new File(UploadPath + "/" + fileName);
        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            return "true";
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return "false";
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonModel file(@RequestParam("file") MultipartFile file, String uploadPath) {
        jm = new JsonModel();
        if (file.isEmpty()) {
            jm.setCode(0);
            jm.setMsg("文件为空");
            return jm;
        }
        String name = (String) session.getAttribute("name");
        File path = new File(UploadPath + file.getOriginalFilename());
        try {
            file.transferTo(path);
            Path path1 = new Path(path.getPath());
            hdfsUtil.upload(path1, name, uploadPath);
            jm.setCode(1);
            jm.setMsg("上传成功");

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            jm.setCode(0);
            jm.setMsg("上传失败");
        }
        return jm;
    }

    /**
     * 上传文件夹
     *
     * @param files
     * @return
     */
    @RequestMapping(value = "/uploadDir", method = RequestMethod.POST)
    public String uploadFolder(MultipartFile[] files) {
        System.out.println(Arrays.toString(files));
        if (FileUtil.saveMultiFile(UploadPath, files)) {
            return "ok";
        } else {
            return "no";
        }

    }

}
