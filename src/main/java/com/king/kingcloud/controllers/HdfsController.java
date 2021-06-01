package com.king.kingcloud.controllers;

import com.king.kingcloud.bean.HdfsFileStatus;
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
import java.util.List;

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

    /**
     * 获取用户目录下或指定目录下的文件
     *
     * @param session
     * @param path
     * @return
     */
    @RequestMapping(value = "/getFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取用户下的文件目录", notes = "User")
    @ApiImplicitParam(name = "path", value = "目录", required = false)
    public JsonModel gstAllFile(HttpSession session, String path) {
        jm = new JsonModel();
        String name = (String) session.getAttribute("name");

        if (name == null || name.equals("null")) {
            jm.setCode(0);
            jm.setMsg("您没有登录 请先登录!");
        } else {
            List<HdfsFileStatus> list = hdfsUtil.query(name, path);
            if (list.size() < 1) {
                HdfsFileStatus hdfsFileStatus = new HdfsFileStatus();
                hdfsFileStatus.setName("这是一个空文件夹");
                hdfsFileStatus.setIsDirectory(false);
                hdfsFileStatus.setFileSize(null);
                hdfsFileStatus.setAccess_time(" ");
                list.add(hdfsFileStatus);
            }
            jm.setCode(1);
            jm.setObj(list);
        }
        return jm;
    }

    /**
     * 新建文件夹
     *
     * @param session
     * @param dirPath 带路径的文件名
     * @return
     */
    @RequestMapping(value = "/newDir", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "新建文件夹", notes = "新建")
    @ApiImplicitParam(name = "dirPath", value = "dirPath", required = true)
    public JsonModel newDir(HttpSession session, String dirPath) {
        jm = new JsonModel();
        String name = (String) session.getAttribute("name");

        if (hdfsUtil.mkdir(name, dirPath)) {
            jm.setCode(1);
            jm.setMsg("新建成功!");
        } else {
            jm.setCode(0);
            jm.setMsg("新建失败!");
        }

        return jm;
    }

    @RequestMapping(value = "/newFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "新建文件", notes = "新建")
    @ApiImplicitParam(name = "FilePath", value = " FilePath", required = true)
    public JsonModel newFile(HttpSession session, String FilePath) {
        jm = new JsonModel();


        return jm;
    }
}
