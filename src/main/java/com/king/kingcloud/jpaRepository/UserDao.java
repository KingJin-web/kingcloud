package com.king.kingcloud.jpaRepository;

import com.king.kingcloud.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-05-28 20:24
 */
public interface UserDao extends JpaRepository<User, Integer> {

    /**
     * 判断密码是否正确
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 密文
     * @return
     */
    public static boolean matchesPwd(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, encodedPassword);
    }


}
