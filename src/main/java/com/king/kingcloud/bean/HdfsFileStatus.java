package com.king.kingcloud.bean;

/**
 * @program: kingcloud
 * @description: hdfs文件信息
 * @author: King
 * @create: 2021-05-30 23:10
 */
public class HdfsFileStatus {
    //文件路径
    private String path;
    //是否为文件夹
    private Boolean isDirectory;
    private int length;
    //备份数
    private int replication = 3;
    //文件大小
    private Long blocksize;
    //修改时间
    private long modification_time;
    //访问时间
    private Long access_time;
    //所有者
    private String owner;
    //
    private String group;
    private String permission;
    private Boolean isSymlink;
}
