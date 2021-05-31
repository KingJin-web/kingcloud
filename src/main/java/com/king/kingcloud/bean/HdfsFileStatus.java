package com.king.kingcloud.bean;

import lombok.Data;

import org.apache.hadoop.fs.Path;
import java.util.List;

/**
 * @program: kingcloud
 * @description: hdfs文件信息
 * @author: King
 * @create: 2021-05-30 23:10
 */
@Data
public class HdfsFileStatus {
    //文件路径
    private Path path;
    //是否为文件夹
    private Boolean isDirectory;
    private String name;
    //private int length;
    //备份数
    private int replication = 3;
    //文件大小
    private Long FileSize;
    private Long blocksize;
    //修改时间
    private String modification_time;
    //上次访问时间
    private String access_time;
    //所有者
//    private String owner;
//    private String group;
//    private String permission;
//    private Boolean isSymlink;
}
