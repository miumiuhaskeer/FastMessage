package com.miumiuhaskeer.fastmessage.config;

import com.miumiuhaskeer.fastmessage.JsonConverter;
import com.miumiuhaskeer.fastmessage.exception.AuthenticationErrorHandler;
import com.miumiuhaskeer.fastmessage.handler.UserAuthenticationFailureHandler;
import com.miumiuhaskeer.fastmessage.handler.UserAuthenticationSuccessHandler;
import com.miumiuhaskeer.fastmessage.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JsonConverter jsonConverter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/sign-in/**", "/sign-up/**", "/test")
                .permitAll()
                .anyRequest()
                .authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint());

        http.formLogin()
                .successHandler(userAuthenticationSuccessHandler())
                .failureHandler(userAuthenticationFailureHandler())
                .loginPage("/sign-in")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll();

        http.cors();

        // not required for api
        http.csrf().disable();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationErrorHandler();
    }

    @Bean
    public AuthenticationSuccessHandler userAuthenticationSuccessHandler(){
        return new UserAuthenticationSuccessHandler(jsonConverter);
    }

    @Bean
    public AuthenticationFailureHandler userAuthenticationFailureHandler(){
        return new UserAuthenticationFailureHandler();
    }
}
