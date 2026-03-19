package io.security.springsecuritymaster;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .formLogin(Customizer.withDefaults())
        .rememberMe(rememberMe -> rememberMe
            //.alwaysRemember(true) //기억하기 매개변수가 설정 안됐을때도 쿠키가 항상 생성되어야하는지에 대한 여부
            .tokenValiditySeconds(3600)   //토큰이 유효한 시간(초 단위)를 지정할 수 있음
            .userDetailsService(userDetailsService())  //UserDetails조회를 위한 서비스 지정
            .rememberMeParameter("remember")  //로그인시 사용자 기억을 위한 HTTP 매개변수. 기본값 remember-me
            .rememberMeCookieName("remember") //기억하기 인증을 위한 토큰을 저장하는 쿠키. 기본값 remember-me
            .key("security")  //기억하기 인증을 위해 생성된 토큰을 식별하는 키 설정
        );

    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService(){
    UserDetails user = User.withUsername("user")
        .password("{noop}1111")
        .roles("USER").build();
    return new InMemoryUserDetailsManager(user);
  }
}
