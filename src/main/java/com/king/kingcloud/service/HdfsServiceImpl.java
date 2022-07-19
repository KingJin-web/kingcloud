package com.king.kingcloud.service;

import com.king.kingcloud.entity.HdfsFileStatus;
import com.king.kingcloud.util.EmptyUtil;
import com.king.kingcloud.util.MyException;
import com.king.kingcloud.util.StringUtils;
import com.king.kingcloud.util.TimeUtil;
import com.king.kingcloud.vo.JsonModel;
import com.king.kingcloud.vo.ResultObj;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author: King
 * @project: kingcloud
 * @date: 2022年07月02日 18:17
 * @description:
 */
@Service
public class HdfsServiceImpl {

    // HDFS文件系统服务器的地址以及端口

    //"http://erfengv.com:9000";
    @Value(value = "${config.hadoop.hdfs.url}")
    private String HDFS_PATH;
    @Value(value = "${config.hadoop.hdfs.user}")
    private String HDFS_USER;
    // HDFS文件系统的操作对象
    private FileSystem fileSystem;

    //日志
    private static final Logger logger = LoggerFactory.getLogger(HdfsServiceImpl.class);


    /**
     * 获取HDFS配置信息
     *
     * @return
     * @throws Exception
     */

    public Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", HDFS_PATH);
        configuration.set("fs.hdfs.impl.disable.cache", "true");
        configuration.set("dfs.client.use.datanode.hostname", "true");
        //其它参数
        return configuration;
    }

    public FileSystem getFileSystem(String HDFS_PATH, String HDFS_USER) {

        try {
            // 第一参数是服务器的URI，第二个参数是配置对象，第三个参数是文件系统的用户名
            return FileSystem.get(new URI(HDFS_PATH), getConfiguration(), HDFS_USER);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            logger.error("HDFS文件系统连接失败");
            return null;
        }

    }
//    public HdfsServiceImpl() {
//        configuration = new Configuration();
//        // 第一参数是服务器的URI，第二个参数是配置对象，第三个参数是文件系统的用户名
//        try {
//            fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, HDFS_USER);
//        } catch (IOException | InterruptedException | URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public HdfsServiceImpl(String HDFS_PATH) {
//        this.HDFS_PATH = HDFS_PATH;
//        configuration = new Configuration();
//        // 第一参数是服务器的URI，第二个参数是配置对象，第三个参数是文件系统的用户名
//        try {
//            fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, HDFS_USER);
//        } catch (IOException | InterruptedException | URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * http://localhost:8080/downDir?path=/%E6%96%87%E4%BB%B6%E5%A4%B94/
     *
     * @param path
     * @param stream1
     * @throws IOException
     */
    public void compress(String path, ZipOutputStream stream1) throws IOException {
        try (FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER)) {
            Path path1 = new Path(path);

            FileStatus[] status = fileSystem.listStatus(path1);

            String[] split = path.split("/");
            String lastName = split[split.length - 1];
            for (FileStatus fileStatus : status) {
                String name = fileStatus.getPath().toString();
                name = name.substring(name.indexOf("/" + lastName));
                if (fileStatus.isFile()) {
                    Path path2 = fileStatus.getPath();
                    FSDataInputStream open = fileSystem.open(path2);
                    stream1.putNextEntry(new ZipEntry(name.substring(1)));
                    IOUtils.copyBytes(open, stream1, 1024);

                } else {
                    stream1.putNextEntry(new ZipEntry(fileStatus.getPath().getName() + "/"));
                    compress(fileStatus.getPath().toString(), stream1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHDFS_PATH(String HDFS_PATH) {
        this.HDFS_PATH = HDFS_PATH;
    }

    /**
     * 新建文件夹
     * 根目录下新建文件夹  用户目录
     *
     * @param fileName
     */
    public void mkdir(String fileName) {
        // 需要传递一个Path对象，表示新建文件夹的路径
        try (FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER)) {
            fileSystem.mkdirs(new Path("/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 新建文件夹
     * 新建用户目录下的文件夹
     *
     * @param name
     * @param pathS
     * @return
     */
    public boolean mkdir(String name, String pathS) {
        try (FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER)) {

            Path path;
            if (pathS == null || pathS.equals("") || pathS.endsWith("undefined")) {
                path = new Path("/" + name);
            } else {
                path = new Path("/" + name + "/" + pathS);
            }
            return fileSystem.mkdirs(path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeName() {


        return true;
    }

    public boolean changeFileName(String uname, String path, String oldName, String newName) {
        if (StringUtils.isEmpty(path, oldName, newName)) {
            throw new MyException("路径或文件名不能为空!");
        }
        try (FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER)) {
            Path oldPath = new Path("/" + uname + "/" + path + "/" + oldName);
            Path newPath = new Path("/" + uname + "/" + path + "/" + newName);
            System.out.println(oldPath);
            System.out.println(newPath);
            // 第一个参数是原文件的名称，第二个则是新的名称
            return fileSystem.rename(oldPath, newPath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 上传文件
     *
     * @param path
     * @param name
     * @param uploadPath
     * @return
     */
    public boolean upload(Path path, String name, String uploadPath) {
        try (FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER)) {
            Path path1 = new Path("/" + name + "/" + uploadPath);
            fileSystem.copyFromLocalFile(path, path1);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 文件上传
     *
     * @param file       文件对象
     * @param uploadPath 文件路径
     * @return
     */
    public ResultObj upload(MultipartFile file, String uploadPath) {
        FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER);
        try {
            // 先判断文件夹是否存在，如果不存在，则创建文件夹
            if (file.isEmpty()) {
                return ResultObj.error("文件为空");
            }
            String fileName = file.getOriginalFilename();
            //上传文件
//            String path = com.king.hadoop_helper.util.FileUtil.saveFile(file);
//
//            fileSystem.copyFromLocalFile(true, new Path(path), new Path("/" + uploadPath + "/" + fileName));
            //out对应的是Hadoop文件系统中的目录
            FSDataOutputStream out = fileSystem.create(new Path(uploadPath + "/" + fileName));
            //OutputStream out = fileSystem.create(new Path(uploadPath + "/" + name));
            IOUtils.copyBytes(file.getInputStream(), out, getConfiguration());
            //  IOUtils.copyBytes(file.getInputStream(), out, 4096, true);

            return ResultObj.success("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.error("上传失败");
        } finally {
            try {
                fileSystem.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取指定文件夹下的所有·文件或文件夹
     *
     * @param name
     * @param pathS
     * @return
     */
    public List<HdfsFileStatus> query(String name, String pathS) {
        List<HdfsFileStatus> list = new ArrayList<>();
        Path path;
        try (FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER)) {
            if (pathS == null || pathS.equals("") || pathS.endsWith("undefined")) {
                path = new Path("/" + name);
            } else {
                path = new Path("/" + name + "/" + pathS);
            }
            FileStatus[] fileStatuses;
            fileStatuses = fileSystem.listStatus(path);
            for (FileStatus fileStatus : fileStatuses) {
                HdfsFileStatus hfs = new HdfsFileStatus();
                //获取文件名
                hfs.setName(fileStatus.getPath().getName());
                hfs.setPath(getPathDeName(name, fileStatus.getPath()));
                //获取文件是否为文件夹
                hfs.setIsDirectory(fileStatus.isDirectory());
                //文件上次修改时间
                hfs.setModification_time(TimeUtil.timeToString(fileStatus.getModificationTime()));
                hfs.setAccess_time(TimeUtil.timeToString(fileStatus.getAccessTime()));
                //获取文件实际大小
                hfs.setFileSize((fileSystem.getContentSummary(fileStatus.getPath()).getSpaceConsumed()));
                hfs.setBlocksize(fileStatus.getBlockSize());
                list.add(hfs);
            }
        } catch (IOException e) {
            e.printStackTrace();
            list.add(new HdfsFileStatus(false));
        }
        return list;
    }

    /**
     * 查询所有文件
     *
     * @param name
     * @return
     */
    public List<HdfsFileStatus> queryAll(String name) {
        try (FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER)) {
            Path path = new Path("/" + name);
            List<HdfsFileStatus> list = new ArrayList<>();
            RemoteIterator<LocatedFileStatus> iter = fileSystem.listFiles(path, true);
            //这里的第二个参数true表示递归遍历，false反之
            while (iter.hasNext()) {
                HdfsFileStatus hfs = new HdfsFileStatus();
                LocatedFileStatus file = iter.next();
                //获取文件名
                hfs.setName(file.getPath().getName());
                hfs.setPath(getPathDeName(name, file.getPath()));
                //获取文件是否为文件夹
                hfs.setIsDirectory(file.isDirectory());
                //文件上次修改时间
                hfs.setModification_time(TimeUtil.timeToString(file.getModificationTime()));
                hfs.setAccess_time(TimeUtil.timeToString(file.getAccessTime()));
                //获取文件实际大小
                hfs.setFileSize((fileSystem.getContentSummary(file.getPath()).getSpaceConsumed()));
                hfs.setBlocksize(file.getBlockSize());
                list.add(hfs);

            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回指定文件类型的
     *
     * @param name
     * @param type 1 为图片 2 为文档 3 为视频 4 为音频
     * @return
     */
    public List<HdfsFileStatus> queryAllType(String name, int type) {
        List<HdfsFileStatus> list = queryAll(name);
        List<HdfsFileStatus> list1 = new ArrayList<>();
        String reg = null;
        if (type == 1) {
            reg = ".+(.jpeg|.jpg|.png|.bmp|.gif|.svg)$";
        } else if (type == 2) {
            reg = ".+(.txt|.doc|.docx|.xls|.xlsx|.html|.hml|.js|.pdf|.ppt|.cpp|.css)$";
        } else if (type == 3) {
            reg = ".+(.mp4|.avi|.wmv|.flv)$";
        } else if (type == 4) {
            reg = ".+(.mp3|.wav)$";
        } else if (type == 5) {
            reg = "^\\S+\\.*$";
        } else {
            reg = ".+(.torrent)$";
        }

        Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        for (HdfsFileStatus hfs : list) {
            if (pattern.matcher(hfs.getName()).find()) {
                list1.add(hfs);
            }
        }
        return list1;
    }

    /**
     * 删除文件
     *
     * @param name
     * @param dlPath
     * @return
     */
    public boolean delete(String name, String dlPath) {
        try (FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER)) {
            Path path;
            if (name == null || dlPath == null || dlPath.equals("") || dlPath.endsWith("undefined")) {
                return false;
            } else {
                path = new Path("/" + name + "/" + dlPath);
            }
            // 第二个参数指定是否要递归删除，false=否，true=是
            return fileSystem.delete(path, true);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getFileName(Path path) {
        Assert.notNull(path, "");
        String file = String.valueOf(path);
        return file.substring(file.lastIndexOf("/") + 1);
    }

    /**
     * 返回文件的路径
     * String a = "hdfs://dn1:9000/king/文件夹/a";
     *
     * @param name king
     * @param path hdfs://dn1:9000/king/文件夹/a
     * @return /文件夹/a
     */
    public String getPathDeName(String name, Path path) {
        String paths = String.valueOf(path);
        return paths.substring(paths.indexOf(name) + name.length());
    }

    /**
     * 下载文件
     *
     * @param name
     * @param paths
     * @return
     */
    public ResponseEntity<InputStreamResource> downFile(String name, String paths) {
        Path path = new Path(HDFS_PATH +"/" + name + "/" + paths);
        FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER);
        try {
            FSDataInputStream inputStream = fileSystem.open(path);
            return downloadFile(inputStream, getFileName(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //网络协议的拼接
    private ResponseEntity<InputStreamResource>
    downloadFile(FSDataInputStream inputStream, String fileName) throws
            IOException {
        Byte[] bytes = new Byte[inputStream.available()];
        System.out.println(fileName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cache-Control", "no-cache, no-store, must-revalidate");
        //通过设置头信息给文件命名，也即是，在前端，文件流被接受完还原成原文件的时候会以你传递的文件名来命名
        httpHeaders.add("Content-Disposition", String.format("attachment; filename=\"%s\"",
                URLEncoder.encode(fileName, "utf-8")));
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");
        httpHeaders.add("Content-Language", "UTF-8");
        logger.info(httpHeaders.toString());
        return ResponseEntity.ok().headers(httpHeaders).contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/octet-stream;charset=UTF-8")).body(new InputStreamResource(inputStream));
    }

    /**
     * 查看预览文档
     *
     * @param paths
     * @return
     */
    public StringBuilder lookDoc(String name, String paths) {

        if (StringUtils.isEmpty(paths)) {
            throw new RuntimeException("文件路径不能为空");
        }
        paths = "/" + name + paths;
        StringBuilder builder = new StringBuilder();
        Path path = new Path(paths);
        try (
                FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER);
                FSDataInputStream in = fileSystem.open(path);
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader bf = new BufferedReader(isr);

        ) {
            String str;
            while ((str = bf.readLine()) != null) {
                builder.append(str).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder;

    }

    public byte[] downFile(String path) {
        try (FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER)) {
            if (EmptyUtil.isEmpty(path)) {
                return null;
            }
            Path path1 = new Path(path);
            FSDataInputStream in = fileSystem.open(path1);
            InputStream ins = in.getWrappedStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bs = new byte[10 * 1024];
            int length = 0;
            while ((length = ins.read(bs, 0, bs.length)) != -1) {
                baos.write(bs, 0, length);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 输出成图片
     *
     * @return
     * @throws IOException
     */
    public void outputImage(HttpServletResponse response, String name, String path)
            throws IOException {
        // 随机生成验证码
        // 图形写给浏览器
        response.setContentType("image/jpeg");
        // 发头控制浏览器不要缓存
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        //允许跨域访问
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 获取响应输出流
        OutputStream out = response.getOutputStream();

        if (StringUtils.isEmpty(path)) {
            throw new MyException("文件路径不能为空");
        }
        path = "/" + name + path;
        BufferedImage image = getImgBuffered(path);
        // 输出图片
        logger.info("输出图片");

        outputImage(image, out);

    }

    private BufferedImage getImgBuffered(String paths) {
        try {
            FileSystem fileSystem = getFileSystem(HDFS_PATH, HDFS_USER);
            BufferedImage bufferedImage = null;
            Path path = new Path(paths);

            FSDataInputStream in = fileSystem.open(path);
            logger.info("获取文件的输入流");
            logger.info(in.toString());
            return ImageIO.read(in.getWrappedStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void outputImage(BufferedImage image, OutputStream os) throws IOException {
        ImageIO.write(image, "jpg", os);
    }
}