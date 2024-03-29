package com.example.tutorialwebapp.config;

import com.example.tutorialwebapp.loginDB.DBLoginSuccessHandler;
import com.example.tutorialwebapp.loginDB.UserDetailsServiceImpl;
import com.example.tutorialwebapp.oAuth2.CustomerOAuth2UserService;
import com.example.tutorialwebapp.oAuth2.OAuth2LoginSuccessHandler;
import com.example.tutorialwebapp.serviceForBoth.PasswordEncoder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
@RestController
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer  {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/welcome_page").setViewName("/welcome_page");
        registry.addViewController("/").setViewName("/welcome_page");
        registry.addViewController("/homePage").setViewName("/homePage");
        registry.addViewController("/swipe").setViewName("/swipe");
        registry.addViewController("/profil").setViewName("/profil");
        registry.addViewController("/ml").setViewName("/ml");
        registry.addViewController("/katzenrassen").setViewName("/katzenrassen");
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(getDaoAuthProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public DaoAuthenticationProvider getDaoAuthProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder.getPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/","/welcome_page", "/images/**", "/css/**", "/js/**", "/createUser","/oauth/**", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin()
                        .loginPage("/welcome_page")
                        .usernameParameter("email")
                        .successHandler(dbLoginSuccessHandler)
                        .permitAll()
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    .userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)
                .and()
                .logout()
                .permitAll();

        return http.build();
    }

    //Wenn man sich erfolgreich angemeldet hat :)
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    @Autowired
    private DBLoginSuccessHandler dbLoginSuccessHandler;
    @Autowired
    private CustomerOAuth2UserService oAuth2UserService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

}