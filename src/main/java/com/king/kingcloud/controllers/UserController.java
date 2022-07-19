package com.king.kingcloud.controllers;

import com.king.kingcloud.entity.User;
import com.king.kingcloud.service.HdfsServiceImpl;
import com.king.kingcloud.service.UserServiceImpl;
import com.king.kingcloud.util.*;
import com.king.kingcloud.vo.JsonModel;
import com.king.kingcloud.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @program: kingcloud
 * @description: 用户
 * @author: King
 * @create: 2021-05-28 21:26
 */
@RestController
@Api(value = "账户操作接口", tags = {"账户操作接口"})
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private HdfsServiceImpl hdfsService;

    Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);


    /**
     * 注册
     *
     * @param name
     * @param pwd1
     * @return
     */
    @RequestMapping(value = "/register.do", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", required = true),
            @ApiImplicitParam(name = "pwd1", value = "密码", required = true),
            @ApiImplicitParam(name = "pwd2", value = "密码", required = true),
            @ApiImplicitParam(name = "email", value = "邮箱", required = false)}
    )
    public JsonModel registerOp(String name, String pwd1, String pwd2, String email) {

        try {
            User user = new User(null, name, pwd1, email);
            logger.info("注册用户：" + user.toString());
            if (userService.register(user)) {
                hdfsService.mkdir(name);
                return JsonModel.success("注册成功！");
            } else {
                return JsonModel.error("用户名已被使用！");
            }
        } catch (MyException e) {
            e.printStackTrace();
            return JsonModel.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonModel.error("注册失败！");
        }

    }

    @RequestMapping(value = "/getUser", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取用户信息", notes = "User")
    public JsonModel getUser() {
        try {
            User user = UserUtils.getUser();
            return JsonModel.success(user);
        }catch (Exception e){
            e.printStackTrace();
            return JsonModel.error("获取用户信息失败！");
        }

    }
}
