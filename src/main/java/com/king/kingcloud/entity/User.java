package com.king.kingcloud.entity;

import lombok.Data;
import org.hibernate.annotations.Proxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @program: kingcloud
 * @description: 用户类
 * @author: King
 * @create: 2021-05-28 18:25
 */
@Data
@Entity
@Table(name = "user")
@Proxy(lazy = false)
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = -3532352203559882495L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String pwd;
    //邮箱
    private String email;
    //头像路径
    private String avatar;
    //账号是否激活
    private Boolean enabled;

    public User() {
    }


    public User(Long id, String name, String pwd, String email) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.email = email;
    }


    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //角色列表
        Collection<GrantedAuthority> roles = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        roles.add(grantedAuthority);
        return roles;
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
