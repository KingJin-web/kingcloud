package com.king.kingcloud.controllers;

import com.king.kingcloud.util.HdfsUtil;
import com.king.kingcloud.vo.JsonModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @program: kingcloud
 * @description: hadoopHdfs
 * @author: King
 * @create: 2021-05-30 21:26
 */
public class HdfsController {
    @Autowired
    private HdfsUtil hdfsUtil;

    @RequestMapping(value = "/getUser", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取用户下的文件目录", notes = "User")
    public JsonModel gstAllFile(String path){

        return null;
    }
}
