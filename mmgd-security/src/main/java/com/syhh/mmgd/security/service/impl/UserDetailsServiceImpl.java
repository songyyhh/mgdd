package com.syhh.mmgd.security.service.impl;

import com.syhh.mmgd.security.domain.CustomUser;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @ClassName : UserDetailServiceImpl
 * @Description ：TODO
 * @Author : songzg
 * @Date ：2019年08月08日 18:01
 * @Version ：
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
    CustomUser user = new CustomUser(1, "jack", getPassword("123456"), getGrants("ROLE_USER"));
    return user;
//      return new User(name,passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
  }

  private String getPassword(String raw) {
    return passwordEncoder.encode(raw);
  }

  private Collection<GrantedAuthority> getGrants(String role) {
    return AuthorityUtils.commaSeparatedStringToAuthorityList(role);
  }


}