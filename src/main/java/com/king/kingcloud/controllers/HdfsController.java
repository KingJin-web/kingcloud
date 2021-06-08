package com.king.kingcloud.controllers;

import com.king.kingcloud.bean.HdfsFileStatus;
import com.king.kingcloud.util.*;
import com.king.kingcloud.vo.JsonLayui;
import com.king.kingcloud.vo.JsonModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
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

    @Autowired
    private RedisUtil redisUtil;
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
    public JsonModel gstFile(HttpSession session, String path) {
        jm = new JsonModel();
        //String name = (String) session.getAttribute("name");
        String name = redisUtil.getValue(session.getId(), "name");
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
     * 获取用户目录下或指定目录下的文件
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/getAllFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取用户下的所有文件", notes = "User")
    public JsonModel gstAllFile(HttpSession session) {
        jm = new JsonModel();

        String name = redisUtil.getValue(session.getId(), "name");
        if (name == null || name.equals("null")) {
            jm.setCode(0);
            jm.setMsg("您没有登录 请先登录!");
        } else {
            List<HdfsFileStatus> list = hdfsUtil.queryAll(name);
            jm.setCode(1);
            jm.setObj(list);
        }
        return jm;
    }

    /**
     * 获取用户目录下或指定目录下的文件
     *
     * @param session
     * @param type
     * @return
     */
    @RequestMapping(value = "/getAllFileType", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取用户下指定文件类型文件", notes = "file")
    @ApiImplicitParam(name = "type", value = "type", required = true)
    public JsonLayui gstAllFileType(HttpSession session, int type, int page, int limit) {
        JsonLayui js = new JsonLayui();

        String name = redisUtil.getValue(session.getId(), "name");
        if (name == null || name.equals("null")) {
            js.setCode(1);
            js.setMsg("您没有登录 请先登录!");
        } else {
            List<HdfsFileStatus> list = hdfsUtil.queryAllType(name, type);
            int size = list.size();
            int a = (page - 1) * limit; //开始行数
            int b = page * limit; //结束行数
            if (b > size) {
                b = size;
            }
            list = list.subList(a, b);
            js.setCode(0);
            js.setData(list);
            js.setCount(size);
        }
        return js;
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
        //String name = (String) session.getAttribute("name");
        String name = redisUtil.getValue(session.getId(), "name");
        System.out.println(dirPath);
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

    /**
     * 修改文件名
     *
     * @param session
     * @param path    路径
     * @param oldName 文件名
     * @param newName 新的文件名
     * @return
     */
    @RequestMapping(value = "/changeFileName", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "重命名文件", notes = "重命名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "path", required = false),
            @ApiImplicitParam(name = "oldName", value = "oldName", required = true),
            @ApiImplicitParam(name = "newName", value = "newName", required = true)
    })
    public JsonModel changeFileName(HttpSession session, String path, String oldName, String newName) {
        jm = new JsonModel();
        if (path == null || oldName == null || newName == null) {
            jm.setCode(0);
            jm.setMsg("WORRY");
        }
        //String name = (String) session.getAttribute("name");
        String name = redisUtil.getValue(session.getId(), "name");
        System.out.println(name + " " + path + " " + oldName + " " + newName);
        if (hdfsUtil.changeFileName(name, path, oldName, newName)) {
            jm.setCode(1);
            jm.setMsg("修改成功!");
        } else {
            jm.setCode(0);
            jm.setMsg("修改失败!");
        }
        return jm;
    }

    @RequestMapping(value = "/delFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "删除文件", notes = "删除")
    @ApiImplicitParam(name = "path", value = " path", required = true)
    public JsonModel delFile(HttpSession session, String path) {
        jm = new JsonModel();
        // String name = (String) session.getAttribute("name");
        String name = redisUtil.getValue(session.getId(), "name");
        if (hdfsUtil.delete(name, path)) {
            jm.setCode(1);
            jm.setMsg("删除成功!");
        } else {
            jm.setCode(0);
            jm.setMsg("删除失败!");
        }

        return jm;
    }

    @RequestMapping(value = "/downFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "下载文件", notes = "下载")
    @ApiImplicitParam(name = "path", value = " path", required = true)
    public ResponseEntity<InputStreamResource> downFile(HttpSession session, @RequestParam("path") String path) {
        System.out.println(path);
        jm = new JsonModel();

        String name = redisUtil.getValue(session.getId(), "name");
        ResponseEntity<InputStreamResource> res = null;
        try {
            res = hdfsUtil.downFile(name, path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }


    @RequestMapping(value = "/lookPhoto", method = RequestMethod.GET)
    @ApiOperation(value = "查看图片", notes = "查看")
    @ApiImplicitParam(name = "path", value = " path", required = true)
    public JsonModel lookPhoto(String path, HttpServletResponse resp, HttpSession session) throws IOException {
        jm = new JsonModel();
        if (EmptyUtil.isEmpty(path)) {
            jm.setCode(0);
            jm.setMsg("ERROR");
        } else {
            String name = redisUtil.getValue(session.getId(), "name");
            path = "/" + name + path;
            hdfsUtil.outputImage(resp, path);
            jm.setCode(1);
            jm.setMsg("OK");
        }
        return jm;
    }


    /**
     * 查看文档
     *
     * @param path
     * @param resp
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/lookDoc", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "查看文本文件", notes = "查看")
    @ApiImplicitParam(name = "path", value = " path", required = true)
    public JsonLayui lookDoc(String path, HttpServletResponse resp, HttpSession session) throws IOException {
        JsonLayui jsonLayui = new JsonLayui();
        if (EmptyUtil.isEmpty(path)) {
            jsonLayui.setCode(0);
            jsonLayui.setMsg("ERROR");
        } else {
            String name = redisUtil.getValue(session.getId(), "name");
            path = "/" + name + path;
            jsonLayui.setCode(1);
            jsonLayui.setData(hdfsUtil.lookDoc(path));
            jsonLayui.setMsg("OK");
        }
        return jsonLayui;
    }

    /**
     * 下载文件夹
     *
     * @param path
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/downDir", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downDir(String path, HttpSession session, HttpServletRequest request) {
        String name = redisUtil.getValue(session.getId(), "name");
        ResponseEntity<InputStreamResource> result = null;
        try {
            String filename = hdfsUtil.getFileName(new Path(path));
            path = "/" + name + path;
            result = FileUtil.downDir(path, filename, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取 hadoop 服务器信息
     *
     * @return
     */
    @RequestMapping(value = "/LookHdfs", method = RequestMethod.GET)
    public JsonLayui LookHdfs() {
        JsonLayui js = new JsonLayui();


        return js;
    }
}
