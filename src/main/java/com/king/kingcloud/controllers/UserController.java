package com.king.kingcloud.controllers;

import com.king.kingcloud.bean.User;
import com.king.kingcloud.biz.UserBizImpl;
import com.king.kingcloud.util.HdfsUtil;
import com.king.kingcloud.vo.JsonModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
    private UserBizImpl userBiz;

    @Autowired
    private HdfsUtil hdfsUtil;

    /**
     * 注册
     *
     * @param jm
     * @param name
     * @param pwd1
     * @return
     */
    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", required = true),
            @ApiImplicitParam(name = "pwd1", value = "密码", required = true),
            @ApiImplicitParam(name = "pwd2", value = "密码", required = true),
            @ApiImplicitParam(name = "email", value = "邮箱", required = false)}
    )
    public JsonModel registerOp(JsonModel jm, String name, String pwd1, String pwd2, String email) {
        if (name == null || "".equals(name)) {
            jm.setCode(0);
            jm.setMsg("请输入用户名！");
            return jm;
        }
        if (pwd1 == null || "".equals(pwd1)) {
            jm.setCode(0);
            jm.setMsg("请输入密码！");
            return jm;
        }
        System.out.println(pwd1 + " " + pwd2);
        if (!pwd1.equals(pwd2)) {
            jm.setCode(0);
            jm.setMsg("两次密码不一致请重新输入！");
            return jm;
        }
        User user = new User(null, name, pwd1, email);
        if (userBiz.register(user)) {
            jm.setCode(1);
            jm.setMsg("注册成功！");
            hdfsUtil.mkdir(name);
            return jm;
        } else {
            jm.setCode(0);
            jm.setMsg("用户名已被使用！");
            return jm;
        }


    }

    /**
     * //登录
     *
     * @param session
     * @param jm
     * @param vcode
     * @param name
     * @param pwd
     * @return
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "登录", notes = "登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "vcode", value = "验证码", required = true),
            @ApiImplicitParam(name = "name", value = "用户名", required = true),
            @ApiImplicitParam(name = "pwd", value = "密码", required = true)}
    )
    public JsonModel loginOp(HttpSession session, JsonModel jm, String vcode, String name, String pwd) {
        if (name == null || "".equals(name)) {
            jm.setCode(0);
            jm.setMsg("请输入用户名！");
            return jm;
        }
        if (pwd == null || "".equals(pwd)) {
            jm.setCode(0);
            jm.setMsg("请输入密码！");
            return jm;
        }
        if (vcode == null || "".equals(vcode)) {
            jm.setCode(0);
            jm.setMsg("验证码不能为空!");
            return jm;
        }
        long nowTime = new Date().getTime(); //oldTime
        long oldTime = (long) session.getAttribute("oldTime");
        if (nowTime - oldTime > 60 * 1000) {
            jm.setCode(0);
            jm.setMsg("验证码已超时!");
            return jm;
        }

        String validateCode = (String) session.getAttribute("validateCode");
        if (!vcode.equalsIgnoreCase(validateCode)) {
            jm.setCode(0);
            jm.setMsg("验证码输入错误!");
            return jm;
        }

        User u = new User();
        u.setName(name);
        u.setPwd(pwd);
        // resUserDao.login(u);
        if (userBiz.login(u)) {
            //保存这个用户：在数据库中保存用户状态
            //TODO 更好的方案是使用一个数据库/Redis 来储存
            session.setAttribute("name", u.getName());
            jm.setCode(1);
            jm.setMsg("登陆成功");

        } else {
            jm.setCode(0);
            jm.setMsg("用户名或密码错误！");
        }
        return jm;
    }

    @RequestMapping(value = "/getUser", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取用户信息", notes = "User")
    public JsonModel getUser(HttpSession session, JsonModel jm) {
        String name = (String) session.getAttribute("name");
        System.out.println(name);
        if (name == null||name.equals("null")) {
            jm.setCode(0);
            jm.setMsg("您没有登录 请先登录!");
        } else {
            jm.setCode(1);
            jm.setObj(userBiz.getUserByName(name));
        }

        return jm;
    }
}
