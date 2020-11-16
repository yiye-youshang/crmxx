package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.entry.Userinfo;
import com.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService service;

    @RequestMapping("/login")
    public String login(HttpSession session, Userinfo userinfo) {
        QueryWrapper<Userinfo> sql = new QueryWrapper<>();
        sql.eq("username", userinfo.getUsername());
        sql.eq("password", userinfo.getPassword());
        Userinfo user = service.getOne(sql);
        if (user != null) {
            //成功

            session.setAttribute("user", user);
            return "redirect:/seckill/tolist";
        } else {
            return "redirect:/";
        }


    }

    //注册
    @ResponseBody
    @RequestMapping("/register")
    public String register(HttpServletRequest request, Userinfo userinfo) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String realname = request.getParameter("realname");
        if ("".equals(username)) {
            return "<script>alert('用户名不能为空');" +
                    "location.href='/pages/login.html'</script>";
        } else {
            userinfo.setUsername(username);
            userinfo.setPassword(password);
            userinfo.setRealname(realname);
            boolean result = service.save(userinfo);
            if (result) {
                System.out.println("注册成功");
                return "<script>location.href='/pages/login.html'</script>";
            } else {
                return "";
            }
        }
    }

    //注销
    @ResponseBody
    @RequestMapping("/loginout")
    public String loginout(HttpServletRequest request,HttpSession session) {

        if (session.getAttribute("user") != null) {
            session.removeAttribute("user");
            System.out.println("用户注销了");
            return "<script>location.href='../pages/login.html'</script>";
        }else {
            return "<script>alert('没有用户登录 不用注销')</script>";
        }

    }
}
