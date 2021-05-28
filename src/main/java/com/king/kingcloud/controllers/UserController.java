package com.king.kingcloud.controllers;

import com.king.kingcloud.bean.User;
import com.king.kingcloud.biz.UserBizImpl;
import com.king.kingcloud.vo.JsonModel;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
public class UserController {

    @Autowired
    private UserBizImpl userBiz;

    //登录
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "登录", notes = "登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "valcode", value = "验证码", required = true),
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "pwd", value = "密码", required = true)}
    )
    public JsonModel
    loginOp(HttpSession session, JsonModel jm, String valcode, String username, String pwd) {
        if (valcode == null || "".equals(valcode)) {
            jm.setCode(0);
            jm.setMsg("验证码不能为空...");
            return jm;
        }
        long nowTime = new Date().getTime(); //oldTime
        long oldTime = (long) session.getAttribute("oldTime");
        if (nowTime - oldTime > 6 * 100000) {
            jm.setCode(0);
            jm.setMsg("验证码已超时...");
            return jm;
        }

        String validateCode = (String) session.getAttribute("validateCode");
        if (!valcode.equalsIgnoreCase(validateCode)) {
            jm.setCode(0);
            jm.setMsg("验证码输入错误...");
            return jm;
        }

        User u = new User();
        u.setName(username);
        u.setPwd(pwd);
        // resUserDao.login(u);
        if (userBiz.login(u)) {
            //保存这个用户：在数据库中保存用户状态
            //TODO 更好的方案是使用一个数据库/Redis 来储存
            session.setAttribute("name", u);
            jm.setCode(1);
            jm.setObj("登陆成功");

        } else {
            jm.setCode(0);
            jm.setMsg("wrong username or password,plase tyr again");
        }
        return jm;
    }
}
