package com.king.kingcloud.controllers;

import com.king.kingcloud.entity.HdfsFileStatus;
import com.king.kingcloud.service.HdfsServiceImpl;
import com.king.kingcloud.util.*;
import com.king.kingcloud.vo.JsonLayui;
import com.king.kingcloud.vo.JsonModel;
import com.king.kingcloud.vo.ResultObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @program: kingcloud
 * @description: hadoopHdfs
 * @author: King
 * @create: 2021-05-30 21:26
 */

@RestController
@PreAuthorize("hasRole('USER')")
@Api(value = "Hdfs文件接口", tags = {"文件操作接口"})
public class HdfsController {

    private static final Logger logger = LoggerFactory.getLogger(HdfsController.class);
    private HdfsServiceImpl hdfsService;

    @Autowired
    public void setHdfsService(HdfsServiceImpl hdfsService) {
        this.hdfsService = hdfsService;
    }

    private JsonModel jm;

    /**
     * 获取用户目录下或指定目录下的文件
     *
     * @param path
     * @return
     */
    @RequestMapping(value = "/getFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取用户下的文件目录", notes = "User")
    @ApiImplicitParam(name = "path", value = "目录", required = false)
    public JsonModel gstFile(String path) {

        try {
            String name = UserUtils.getUser().getName();
            List<HdfsFileStatus> list = hdfsService.query(name, path);
            if (list.size() < 1) {
                HdfsFileStatus hdfsFileStatus = new HdfsFileStatus();
                hdfsFileStatus.setName("这是一个空文件夹");
                hdfsFileStatus.setIsDirectory(false);
                hdfsFileStatus.setFileSize(null);
                hdfsFileStatus.setAccess_time(" ");
                list.add(hdfsFileStatus);
            }
            return JsonModel.success(list);
        } catch (Exception e) {
            return JsonModel.error(e.getMessage());
        }


    }

    /**
     * 获取用户目录下或指定目录下的文件
     *

     * @return
     */
    @RequestMapping(value = "/getAllFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取用户下的所有文件", notes = "User")
    public JsonModel gstAllFile() {
        try {
            String name = UserUtils.getUser().getName();
            List<HdfsFileStatus> list = hdfsService.queryAll(name);
            return JsonModel.success(list);
        } catch (MyException e) {
            return JsonModel.error(e.getMessage());
        } catch (Exception e) {
            return JsonModel.error("获取失败!");
        }

    }

    /**
     * 获取用户目录下或指定目录下的文件
     *

     * @param type
     * @return
     */
    @RequestMapping(value = "/getAllFileType", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取用户下指定文件类型文件", notes = "file")
    @ApiImplicitParam(name = "type", value = "type", required = true)
    public ResultObj gstAllFileType(int type, int page, int limit) {
        try {
            String name = UserUtils.getUser().getName();
            List<HdfsFileStatus> list = hdfsService.queryAllType(name, type);
            int size = list.size();
            int a = (page - 1) * limit; //开始行数
            int b = page * limit; //结束行数
            if (b > size) {
                b = size;
            }
            list = list.subList(a, b);
            return ResultObj.layui(size, list,"");
        } catch (MyException e) {
            return ResultObj.layui(0, null, e.getMessage());
        } catch (Exception e) {
            return ResultObj.layui(0, null, "获取失败!");
        }
    }

    /**
     * 新建文件夹
     *
     * @param dirPath 带路径的文件名
     * @return
     */
    @RequestMapping(value = "/newDir", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "新建文件夹", notes = "新建")
    @ApiImplicitParam(name = "dirPath", value = "dirPath", required = true)
    public JsonModel newDir(String dirPath) {
        try {
            String name = UserUtils.getUser().getName();
            if (hdfsService.mkdir(name, dirPath)) {
                return JsonModel.success("新建成功!");
            } else {
                return JsonModel.error("新建失败!");
            }
        } catch (MyException e) {
            return JsonModel.error(e.getMessage());
        } catch (Exception e) {
            return JsonModel.error("获取失败!");
        }
    }


    @RequestMapping(value = "/newFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "新建文件", notes = "新建")
    @ApiImplicitParam(name = "FilePath", value = " FilePath", required = true)
    public JsonModel newFile(HttpSession session, String FilePath) {
        return JsonModel.success("//TODO");
    }

    /**
     * 修改文件名
     *
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
    public JsonModel changeFileName(String path, String oldName, String newName) {
        try {
            String name = UserUtils.getUser().getName();
            logger.info("path:" + path + " oldName:" + oldName + " newName:" + newName);
            if (hdfsService.changeFileName(name, path, oldName, newName)) {
                return JsonModel.success("修改成功!");
            }
        } catch (MyException e) {
            return JsonModel.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonModel.error("修改失败!");
        }
        return JsonModel.error("修改失败!");
    }


    @RequestMapping(value = "/delFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "删除文件", notes = "删除")
    @ApiImplicitParam(name = "path", value = " path", required = true)
    public JsonModel delFile(HttpSession session, String path) {

        try {
            // String name = (String) session.getAttribute("name");
            String name = UserUtils.getUser().getName();
            if (hdfsService.delete(name, path)) {
                return JsonModel.success("删除成功!");
            }
        } catch (MyException e) {
            return JsonModel.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonModel.error("删除失败!");
        }
        return JsonModel.error("删除失败!");
    }

    @RequestMapping(value = "/downFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "下载文件", notes = "下载")
    @ApiImplicitParam(name = "path", value = " path", required = true)
    public ResponseEntity<InputStreamResource> downFile(HttpSession session, @RequestParam("path") String path) {

        String name = UserUtils.getUser().getName();
        ResponseEntity<InputStreamResource> res = null;
        try {
            res = hdfsService.downFile(name, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    @RequestMapping(value = "/lookPhoto", method = RequestMethod.GET)
    @ApiOperation(value = "查看图片", notes = "查看")
    @ApiImplicitParam(name = "path", value = " path", required = true)
    public JsonModel lookPhoto(String path, HttpServletResponse resp) {
        try {
            String name = UserUtils.getUser().getName();
            hdfsService.outputImage(resp, name, path);
        } catch (MyException e) {
            return JsonModel.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonModel.error("查看失败!");
        }
        return JsonModel.success("查看成功!");
    }


    /**
     * 查看文档
     *
     * @param path
     * @param resp

     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/lookDoc", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "查看文本文件", notes = "查看")
    @ApiImplicitParam(name = "path", value = " path", required = true)
    public JsonLayui lookDoc(String path, HttpServletResponse resp, HttpSession session) throws IOException {
        JsonLayui jsonLayui = new JsonLayui();
        try {
            String name = UserUtils.getUser().getName();
            jsonLayui.setCode(1);
            jsonLayui.setData(hdfsService.lookDoc(name, path));
            jsonLayui.setMsg("OK");
        } catch (MyException e) {
            jsonLayui.setCode(0);
            jsonLayui.setMsg(e.getMessage());
        } catch (Exception e) {
            jsonLayui.setCode(0);
            jsonLayui.setMsg("ERROR");
        }
        return jsonLayui;
    }

    /**
     * 下载文件夹
     *
     * @param path

     * @param request
     * @return
     */
    @RequestMapping(value = "/downDir", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downDir(String path, HttpSession session, HttpServletRequest request) {
        String name = UserUtils.getUser().getName();
        ResponseEntity<InputStreamResource> result = null;
        try {
            String filename = hdfsService.getFileName(new Path(path));
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
