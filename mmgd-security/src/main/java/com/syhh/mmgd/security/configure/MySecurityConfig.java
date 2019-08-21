package com.syhh.mmgd.security.configure;

import com.syhh.mmgd.security.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassName : MySecurityConfig
 * @Description ：TODO
 * @Author : songzg
 * @Date ：2019年08月08日 17:56
 * @Version ：
 */
@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/hello.html")
        //注意这里hello.html需要配置成不需要身份认证，否则会报重定向次数过多
        .permitAll()
        .and()
        .formLogin()
        //指定我们自己的登录页面
        .loginPage("/hello.html")
        //指定让UsernamePasswordAuthenticationFilter拦截器拦截的路径
        .loginProcessingUrl("/admin/login")
        //默认登录成功后跳转的页面。
        // http://localhost:8080/hello.html登录成功之后返回index默认请求。
        // 其他请求登录成功之后，返回原有请求路径。
        .defaultSuccessUrl("/index.html")
        .and()
        .authorizeRequests()
        .anyRequest()
        .authenticated();
    http.csrf().disable();
    http.headers().frameOptions().sameOrigin();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}