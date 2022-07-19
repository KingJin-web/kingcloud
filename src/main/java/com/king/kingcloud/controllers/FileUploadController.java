package com.king.kingcloud.controllers;

import com.king.kingcloud.service.HdfsServiceImpl;
import com.king.kingcloud.util.UserUtils;
import com.king.kingcloud.vo.JsonModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-06-04 00:30
 */
@RestController
@RequestMapping("/file")
@Api(value = "文件上传接口", tags = {"文件操作接口"})
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);


    private HdfsServiceImpl hdfsService;

    @Autowired
    public void setHdfsService(HdfsServiceImpl hdfsService) {
        this.hdfsService = hdfsService;
    }
/**
     * 上传文件
     *
     * @param file
     * @return
     */
    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ApiOperation(value = "上传文件到hdfs", notes = "上传")
    public JsonModel file(@RequestParam("file") MultipartFile file, String uploadPath) {

        if (file.isEmpty()) {
            return JsonModel.error("文件为空");
        }

        try {
            String name = UserUtils.getUser().getName();
            hdfsService.upload(file, "/" + name + "/" + uploadPath);
            return JsonModel.success("上传成功");

        } catch (Exception e) {
            e.printStackTrace();
            return JsonModel.error("上传失败");
        }

    }


}
