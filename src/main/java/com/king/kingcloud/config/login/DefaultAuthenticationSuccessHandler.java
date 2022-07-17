//package com.king.kingcloud.config.login;
//
//import com.alibaba.fastjson.JSON;
//import com.king.mooc.entity.User;
//import com.king.mooc.entity.UserLog;
//import com.king.mooc.mapper.mongodb.UserLogMapper;
//import com.king.mooc.util.IPSeeker;
//import com.king.mooc.util.UserIPUtil;
//import com.king.mooc.vo.ResultObj;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Date;
//
///**
// * @program: springboot
// * @description: 登录成功
// * @author: King
// * @create: 2022-03-13 08:41
// */
//@Component
//public class DefaultAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Autowired
//    private UserLogMapper userLogMapper;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication e) throws IOException {
//        IPSeeker ipSeeker =IPSeeker.getInstance();
//        User user = (User) e.getPrincipal();
//        String ip = UserIPUtil.getIPAddress(request);
//        UserLog log = UserLog.builder().uid(user.getId()).name(user.getName()).
//                ip(ip).address(ipSeeker.getAddress(ip)).loginTime(new Date()).build();
//        logger.info(userLogMapper.save(log).toString());
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(JSON.toJSONString(ResultObj.ok("登录成功！")));
//    }
//
//
//}