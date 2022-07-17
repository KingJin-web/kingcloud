package com.king.kingcloud.entity;

import lombok.Data;

/**
 * @program: kingcloud
 * @description: hdfs文件信息
 * @author: King
 * @create: 2021-05-30 23:10
 */
@Data
public class HdfsFileStatus {
    //文件是否存在
    private boolean is;
    //文件路径
    private String path;
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

    public HdfsFileStatus() {
        this.is = true;
    }

    public HdfsFileStatus(boolean is) {
        this.is = is;
    }
}
