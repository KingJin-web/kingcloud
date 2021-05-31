package com.king.kingcloud.controllers;

import com.king.kingcloud.util.HdfsUtil;
import com.king.kingcloud.vo.JsonModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @program: kingcloud
 * @description: hadoopHdfs
 * @author: King
 * @create: 2021-05-30 21:26
 */
@RestController
@Api(value = "Hdfs文件接口", tags = {"文件操作接口"})
public class HdfsController {
    @Autowired
    private HdfsUtil hdfsUtil;

    private JsonModel jm;

    @RequestMapping(value = "/getFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取用户下的文件目录", notes = "User")
    @ApiImplicitParam(name = "path", value = "目录", required = false)
    public JsonModel gstAllFile(HttpSession session,  String path) {
        jm = new JsonModel();
        String name = (String) session.getAttribute("name");
        System.out.println(name);
        if (name == null || name.equals("null")) {
            jm.setCode(0);
            jm.setMsg("您没有登录 请先登录!");
        } else {
            jm.setCode(1);
            jm.setObj(hdfsUtil.query(name, path));
        }

        return jm;
    }
}
