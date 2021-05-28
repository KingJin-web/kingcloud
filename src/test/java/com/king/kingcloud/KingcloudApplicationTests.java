package com.king.kingcloud;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class KingcloudApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encode = passwordEncoder.encode("123");
        String encode1 = passwordEncoder.encode("123");

        System.out.println(encode);
        boolean matches = passwordEncoder.matches("123", encode); //true
        boolean matches1 = passwordEncoder.matches("123", encode1); //true
        boolean matches2 = passwordEncoder.matches("123","$2a$10$hzVB85xRKdnHpG0Akpp5i.4fuHu0hqnkQa89LN4d6k1PX8nL5geFS"); //false

        System.out.println(matches);
        System.out.println(matches1);
        System.out.println(matches2);
    }


}
