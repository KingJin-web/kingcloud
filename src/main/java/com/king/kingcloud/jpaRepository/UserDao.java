package com.king.kingcloud.jpaRepository;

import com.king.kingcloud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-05-28 20:24
 */

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

}
