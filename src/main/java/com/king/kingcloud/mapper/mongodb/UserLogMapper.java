package com.king.kingcloud.mapper.mongodb;


import com.king.kingcloud.entity.UserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @program: springboot
 * @description:
 * @author: King
 * @create: 2022-03-13 15:09
 */
@Component
public class UserLogMapper {
    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 创建对象
     *
     * @return
     */
    public UserLog save(UserLog person) {
        return mongoTemplate.save(person);
    }
}
