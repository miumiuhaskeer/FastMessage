package com.miumiuhaskeer.fastmessage.config;

import com.miumiuhaskeer.fastmessage.JsonConverter;
import com.miumiuhaskeer.fastmessage.config.filter.JWTokenFilter;
import com.miumiuhaskeer.fastmessage.exception.handler.AuthenticationErrorHandler;
import com.miumiuhaskeer.fastmessage.handler.UserAuthenticationFailureHandler;
import com.miumiuhaskeer.fastmessage.handler.UserAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableMongoRepositories(basePackages = "com.miumiuhaskeer.fastmessage.repository.mongodb")
@ConfigurationPropertiesScan("com.miumiuhaskeer.fastmessage.properties.config")
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JsonConverter jsonConverter;
    private final JWTokenFilter jwTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/authentication/**", "/sign-up/**", "/health/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.cors();

        // not required for api
        http.csrf().disable();

        http.addFilterBefore(jwTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationErrorHandler(jsonConverter);
    }

    // TODO remove handlers userAuthenticationSuccessHandler and userAuthenticationFailureHandler
    @Bean
    public AuthenticationSuccessHandler userAuthenticationSuccessHandler(){
        return new UserAuthenticationSuccessHandler(jsonConverter);
    }

    @Bean
    public AuthenticationFailureHandler userAuthenticationFailureHandler(){
        return new UserAuthenticationFailureHandler();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }
}
