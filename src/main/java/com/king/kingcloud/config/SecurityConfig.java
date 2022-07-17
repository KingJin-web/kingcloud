//package com.king.kingcloud.config;
//
//
//import com.king.kingcloud.service.UserServiceImpl;
//import com.king.mooc.config.login.DefaultAuthenticationFailureHandler;
//import com.king.mooc.config.login.DefaultAuthenticationSuccessHandler;
//import com.king.mooc.config.login.LoginFilter;
//import com.king.mooc.service.impl.UserServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
///**
// * @program: springboot
// * @description:
// * @author: King
// * @create: 2022-03-13 08:28
// */
//@EnableGlobalMethodSecurity(prePostEnabled = true)//开启方法权限控制
//@EnableWebSecurity
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private UserServiceImpl userService;
//    @Autowired
//    private DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler;
//    @Autowired
//    private DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler;
//    @Autowired
//    LoginFilter loginFilter;
//
//    /**
//     * 加密方式
//     */
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     * @Description: 主配置方法2
//     * @author tanleijin
//     * @date 2019/9/10 15:03
//     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //4.配置自己实现的登录认证的service,并设置密码的加密方式（）
//        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
//    }
//
//    //静态资源配置
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        //swagger2所需要用到的静态资源，允许访问
//        web.ignoring().antMatchers("/swagger/**")
//                .antMatchers("/swagger-ui.html")
//                .antMatchers("/webjars/**")
//                .antMatchers("/v2/**")
//                .antMatchers("/v2/api-docs-ext/**")
//                .antMatchers("/swagger-resources/**")
//                .antMatchers("/doc.html")
//
//                .antMatchers("/css/**")
//                .antMatchers("/js/**")
//                .antMatchers("/img/**")
//                .antMatchers("/lib/**")
//                .antMatchers("/layer/**")
//                .antMatchers("/layui/**")
//                .antMatchers("/layui/css/**")
//                .antMatchers("/login.html")
//                .antMatchers("/register.html")
//                .antMatchers("/agreement.html")
//                .antMatchers("/index.html");
//
//    }
//
//    /**
//     * @Description: 主配置方法1
//     * @author tanleijin
//     * @date 2019/9/10 15:03
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //关闭跨域保护
//        http.csrf().disable();
//        //验证码过滤器
//        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
//
//        http.authorizeRequests()
//                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") //添加/user/** 下的所有请求只能由user角色才能访问
//                .antMatchers("/admin/**").hasRole("ADMIN"); //添加/admin/** 下的所有请求只能由admin角色才能访问
////                .anyRequest().authenticated(); // 没有定义的请求，所有的角色都可以访问（temp也可以）。
//
//        //以下这句就可以控制单个用户只能创建一个session，也就只能在服务器登录一次
//        http.sessionManagement().maximumSessions(1).expiredUrl("/login");
//        // 指定指定要的登录页面
//        http.formLogin().loginPage("/login").loginProcessingUrl("/api/user/login.do")
//                .successHandler(defaultAuthenticationSuccessHandler)
//                .failureHandler(defaultAuthenticationFailureHandler).permitAll();
//        // http.authorizeRequests().anyRequest().fullyAuthenticated();
//        // http.authorizeRequests().anyRequest().fullyAuthenticated();
//        http.logout().logoutUrl("logout")
//                .logoutSuccessUrl("/login")
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
//                .logoutSuccessHandler(new LogoutSuccessHandler() {
//                    @Override
//                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        // System.out.println("onLogoutSuccess");
//                        response.sendRedirect("logout.html");
//                    }
//                })
//                .addLogoutHandler(new LogoutHandler() {
//                    @Override
//                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//                        // System.out.println("logout");
//                    }
//                })
//                .invalidateHttpSession(true)
//                .deleteCookies("token_token");
//
//    }
//
//}
