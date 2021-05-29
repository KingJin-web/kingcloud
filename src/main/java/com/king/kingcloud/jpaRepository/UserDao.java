package com.king.kingcloud.jpaRepository;

import com.king.kingcloud.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-05-28 20:24
 */

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
}
