package com.king.kingcloud.controllers;

import com.king.kingcloud.util.EmptyUtil;
import com.king.kingcloud.util.VerifyCodeUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-05-27 21:23
 */
@RestController
@Api(value = "验证码接口", tags = {"验证码"})
public class ValCodeController {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ValCodeController.class);
    @RequestMapping(value = "/verifyCodeServlet", method = RequestMethod.GET)
    public void valcode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();

        String vcode = VerifyCodeUtils.outputImage(resp);
        //获得验证码的时间
        Date date = new Date();
        // System.out.println(date.getTime());
        long time = date.getTime();
        session.setAttribute("login_code", vcode);
        session.setAttribute("oldTime", time);
        logger.info(vcode);
    }

    @RequestMapping(value = "/HelloWorld", method = RequestMethod.GET)
    public String HelloWorld(String name) throws IOException {
        if (EmptyUtil.isEmpty(name)) {
            return "HelloWorld";
        } else {
            return "HelloWorld" + name;
        }

    }
}
