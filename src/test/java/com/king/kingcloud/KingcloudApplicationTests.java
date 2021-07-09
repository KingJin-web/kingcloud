package com.king.kingcloud;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class KingcloudApplicationTests {

    @Test
    void contextLoads() {
    }



}


class User {
    private String name;
    private String pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public User(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }
    public static void main(String[] args) {
        User user = new User("蔡徐坤","aaa");
        System.out.println(user);
        user.setName("乔碧萝");
        System.out.println(user);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
