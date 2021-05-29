package com.king.kingcloud.bean;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @program: kingcloud
 * @description: 用户类
 * @author: King
 * @create: 2021-05-28 18:25
 */
@Data
@Entity
@Table(name="user")
@Proxy(lazy = false)
public class User {
    private static final long serialVersionUID = -3532352203559882495L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer uid;
    private String name;
    private String pwd;


    public User() {
    }

    public User(Integer uid, String name, String pwd, String email) {
        this.uid = uid;
        this.name = name;
        this.pwd = pwd;
        this.email = email;
    }

    private String email;




}
