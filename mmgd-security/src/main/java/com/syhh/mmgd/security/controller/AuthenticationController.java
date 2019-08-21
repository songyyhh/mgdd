package com.syhh.mmgd.security.controller;

import com.syhh.mmgd.security.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName : AuthenticationController
 * @Description ：TODO
 * @Author : songzg
 * @Date ：2019年08月08日 17:51
 * @Version ：
 */

@Controller
public class AuthenticationController {

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @GetMapping("/hello.html")
  public String hello() {
    return "/hello";
  }

  @GetMapping({"/", "/index.html"})
  public String index() {
    return "/index";
  }

  @RequestMapping("/syhh.html")
  public String syhh() {
    return "/syhh";
  }

  @GetMapping("/test.html")
  public void test() {

  }

  @GetMapping("/authentication")
  public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
    return user;
  }


}
