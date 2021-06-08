package com.king.kingcloud.util;

import com.king.kingcloud.bean.HdfsFileStatus;
import org.apache.catalina.UserDatabase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-05-30 17:06
 */
@Repository
public class HdfsUtil {

    // HDFS文件系统服务器的地址以及端口
    private String HDFS_PATH = "hdfs://dn1:9000";
    // HDFS文件系统的操作对象
    private FileSystem fileSystem = null;
    // 配置对象
    private Configuration configuration = null;

    public HdfsUtil() {
        configuration = new Configuration();
        // 第一参数是服务器的URI，第二个参数是配置对象，第三个参数是文件系统的用户名
        try {
            fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, "root");
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public HdfsUtil(String HDFS_PATH) {
        this.HDFS_PATH = HDFS_PATH;
        configuration = new Configuration();
        // 第一参数是服务器的URI，第二个参数是配置对象，第三个参数是文件系统的用户名
        try {
            fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, "root");
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * http://localhost:8080/downDir?path=/%E6%96%87%E4%BB%B6%E5%A4%B94/
     * @param path
     * @param stream1
     * @throws IOException
     */
    public void compress(String path, ZipOutputStream stream1) throws IOException {
        Path path1 = new Path(path);
        FileStatus[] status = fileSystem.listStatus(path1);

        String[] split = path.split("/");
        String lastName = split[split.length - 1];
        for (int i = 0; i < status.length; i++) {
            String name = status[i].getPath().toString();
            name = name.substring(name.indexOf("/" + lastName));
            if (status[i].isFile()) {
                Path path2 = status[i].getPath();
                FSDataInputStream open = fileSystem.open(path2);
                stream1.putNextEntry(new ZipEntry(name.substring(1)));
                IOUtils.copyBytes(open, stream1, 1024);

            } else {
                stream1.putNextEntry(new ZipEntry(status[i].getPath().getName()+"/"));
                compress(status[i].getPath().toString(),stream1);
            }
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
        // 需要传递一个Path对象
        boolean result = false;
        try {
            result = fileSystem.mkdirs(new Path("/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
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
        Path path;
        if (pathS == null || pathS.equals("") || pathS.endsWith("undefined")) {
            path = new Path("/" + name);
        } else {
            path = new Path("/" + name + "/" + pathS);
        }
        boolean result = false;
        try {
            result = fileSystem.mkdirs(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean changeName() {


        return true;
    }

    public boolean changeFileName(String uname, String path, String oldName, String newName) {

        Path oldPath = new Path("/" + uname + "/" + path + "/" + oldName);
        Path newPath = new Path("/" + uname + "/" + path + "/" + newName);
        System.out.println(oldPath);
        System.out.println(newPath);
        // 第一个参数是原文件的名称，第二个则是新的名称
        try {
            fileSystem.rename(oldPath, newPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 上传文件
     *
     *
     * @param path
     * @param name
     * @param uploadPath
     * @return
     */
    public boolean upload(Path path, String name, String uploadPath) {
        Path path1 = new Path("/" + name + "/" + uploadPath);
        try {
            fileSystem.copyFromLocalFile(path, path1);
            System.out.println(path.toUri());
            System.out.println(path1.toUri());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
        Path path;
        if (pathS == null || pathS.equals("") || pathS.endsWith("undefined")) {
            path = new Path("/" + name);
        } else {
            path = new Path("/" + name + "/" + pathS);
        }

        FileStatus[] fileStatuses;
        List<HdfsFileStatus> list = new ArrayList<>();
        try {
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
                hfs.setFileSize((fileSystem.getContentSummary(fileStatus.getPath()).getSpaceConsumed()) / 3);
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
        Path path = new Path("/" + name);
        List<HdfsFileStatus> list = new ArrayList<>();

        try {
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
                hfs.setFileSize((fileSystem.getContentSummary(file.getPath()).getSpaceConsumed()) / 3);
                hfs.setBlocksize(file.getBlockSize());
                list.add(hfs);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
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
        Path path;
        if (name == null || dlPath == null || dlPath.equals("") || dlPath.endsWith("undefined")) {
            return false;
        } else {
            path = new Path("/" + name + "/" + dlPath);
        }
        // 第二个参数指定是否要递归删除，false=否，true=是
        try {
            fileSystem.delete(path, true);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
        Path path = new Path("/" + name + paths);

        try {
            FSDataInputStream inputStream = fileSystem.open(path);
            return downloadFile(inputStream, getFileName(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ResponseEntity<InputStreamResource> downloadFile(FSDataInputStream inputStream, String fileName) throws IOException {
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
        System.out.println(httpHeaders);
        return ResponseEntity.ok().headers(httpHeaders).contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/octet-stream;charset=UTF-8")).body(new InputStreamResource(inputStream));
    }

    /**
     * 查看预览文档
     *
     * @param paths
     * @return
     */
    public StringBuilder lookDoc(String paths) {
        StringBuilder builder = new StringBuilder();
        Path path = new Path(paths);
        try (
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
        if (EmptyUtil.isEmpty(path)) {
            return null;
        }
        byte[] bytes = null;
        Path path1 = new Path(path);
        try {

            FSDataInputStream in = fileSystem.open(path1);
            InputStream ins = in.getWrappedStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bs = new byte[10 * 1024];
            int length = 0;
            while ((length = ins.read(bs, 0, bs.length)) != -1) {
                baos.write(bs, 0, length);
            }
            baos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * 输出成图片
     *
     * @return
     * @throws IOException
     */
    public void outputImage(HttpServletResponse response, String path)
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
        BufferedImage image = getImgBuffered(path);
        // 输出图片
        outputImage(image, out);

    }

    private BufferedImage getImgBuffered(String paths) {
        BufferedImage bufferedImage = null;
        Path path = new Path(paths);
        try {
            FSDataInputStream in = fileSystem.open(path);
            bufferedImage = ImageIO.read(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public void outputImage(BufferedImage image, OutputStream os) throws IOException {
        ImageIO.write(image, "jpg", os);
    }
}
