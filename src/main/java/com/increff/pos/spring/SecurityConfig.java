package com.increff.pos.spring;

import com.increff.pos.db.pojo.UserPojo;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Constants constants;

    @Autowired
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(email -> {
            Optional<UserPojo> userOpt = userService.getUserByEmail(email);
            if (!userOpt.isPresent()) {
                throw new UsernameNotFoundException("User not found: " + email);
            }
            UserPojo user = userOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );
        }).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/login", "/api/auth/logout", "/api/auth/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/api/order/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/**").hasAnyRole(constants.OPERATOR, constants.SUPERVISOR)
                .antMatchers(HttpMethod.POST, "/api/**").hasRole(constants.SUPERVISOR)
                .antMatchers(HttpMethod.PUT, "/api/**").hasRole(constants.SUPERVISOR)
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((req, resp, auth) -> resp.setStatus(HttpStatus.OK.value()))
                .and()
                .sessionManagement()
                .maximumSessions(1);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //TODO: props file
        configuration.setAllowedOrigins(Collections.singletonList(constants.FRONTEND_URL));
        configuration.setAllowedMethods(constants.ALLOWED_METHODS);
        configuration.setExposedHeaders(constants.EXPOSED_HEADERS);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws ApiException {
        webSecurity.ignoring().antMatchers("/swagger-ui.html", "/v2/api-docs", "/configuration/ui", "/swagger-resources", "configuration/security",
                "/webjars/**");
    }
}