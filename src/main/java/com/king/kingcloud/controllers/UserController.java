package com.king.kingcloud.controllers;

import com.king.kingcloud.entity.User;
import com.king.kingcloud.service.UserServiceImpl;
import com.king.kingcloud.util.EmptyUtil;
import com.king.kingcloud.util.HdfsUtil;
import com.king.kingcloud.util.RedisUtil;
import com.king.kingcloud.vo.JsonModel;
import com.king.kingcloud.vo.UserVo;
import io.swagger.annotations.Api;
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
@Api(value = "账户操作接口", tags = {"账户操作接口"})
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private HdfsUtil hdfsUtil;

    @Autowired
    RedisUtil redisUtil;
    private JsonModel jm;

    /**
     * 注册
     *
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
    public JsonModel registerOp(String name, String pwd1, String pwd2, String email) {

        jm = new JsonModel();

        if (EmptyUtil.isEmpty(name)) {
            jm.setCode(0);
            jm.setMsg("请输入用户名！");
            return jm;
        }
        if (EmptyUtil.isEmpty(pwd1)) {
            jm.setCode(0);
            jm.setMsg("请输入密码！");
            return jm;
        }

        if (!pwd1.equals(pwd2)) {
            jm.setCode(0);
            jm.setMsg("两次密码不一致请重新输入！");
            return jm;
        }
        User user = new User(null, name, pwd1, email);
        if (userService.register(user)) {
            jm.setCode(1);
            jm.setMsg("注册成功！");
            hdfsUtil.mkdir(name);

        } else {
            jm.setCode(0);
            jm.setMsg("用户名已被使用！");

        }

        return jm;
    }

    /**
     * //登录
     *
     * @param session
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
    public JsonModel loginOp(HttpSession session, String vcode, String name, String pwd) {
        jm = new JsonModel();
        jm.setSessionId(session.getId());
        if (EmptyUtil.isEmpty(name)) {
            jm.setCode(0);
            jm.setMsg("请输入用户名！");
            return jm;
        }
        if (EmptyUtil.isEmpty(pwd)) {
            jm.setCode(0);
            jm.setMsg("请输入密码！");
            return jm;
        }
        if (EmptyUtil.isEmpty(vcode)) {
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
        if (userService.login(u)) {
            UserVo userVo = EmptyUtil.UserToUserVo(userService.getUserByName(name));
            //保存这个用户：在数据库中保存用户状态
            //TODO 更好的方案是使用一个数据库/Redis 来储存
            redisUtil.insertUserVo(session.getId(), userVo);

            jm.setCode(1);
            jm.setMsg("登陆成功");
            session.removeAttribute("validateCode");
            session.removeAttribute("oldTime");
            //session.setAttribute("user", userBiz.getUserByName(name));
        } else {
            jm.setCode(0);
            jm.setMsg("用户名或密码错误！");
        }
        return jm;
    }

    @RequestMapping(value = "/getUser", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取用户信息", notes = "User")
    public JsonModel getUser(HttpSession session) {
        jm = new JsonModel();
        jm.setSessionId(session.getId());

//        String name = redisUtil.getValue(session.getId(),"name");
//        String email = redisUtil.getValue(session.getId(),"email");
//        User user = new User();
//        user.setName(name);
//        user.setEmail(email);

        UserVo userVo = redisUtil.getUserVo(session.getId());


        System.out.println(userVo.getName());
        System.out.println(userVo.getName().equals("null"));
        if (userVo.getName().equals("null") || EmptyUtil.isEmpty(userVo.getName())) {

            jm.setCode(0);
            jm.setMsg("您没有登录 请先登录!");
        } else {
            jm.setCode(1);
            jm.setObj(userVo);
        }

        return jm;
    }
}
