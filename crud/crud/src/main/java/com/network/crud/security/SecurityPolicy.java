package com.network.crud.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityPolicy extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userService;
    private final PasswordEncoder passwordEncoder;
    //WhiteListed urls from authentication
    private static final String[] WHITE_LIST_URLS = {
            "/user/register**",
            "/user/verifyRegistration**",
            "/user/resendVerificationToken**",
            "/user/loggedout**",
            "/css/**",
            "/js/**",
            "/error**",
            "/favicon.ico",
            "/**"
    };

    //Security context configurer and session provider configurer
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Cross origin enable
                .cors().and()
                //Cross site request forgery disable for testing purposes
                .csrf()//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .disable()
                //.and()
                //.formLogin().usernameParameter("email")
                //.successForwardUrl("/user/login")
                /*.failureHandler((request, response, exception) -> {
                    String email = request.getParameter("email");
                    try {
                        String error = exception.getMessage();
                        String userIfExistsEmail = userService.loadUserByUsername(email).getUsername();
                        if(userIfExistsEmail.isEmpty()){
                            throw new Exception("A failed login attempt with email: "
                                    + userIfExistsEmail + ". Reason: " + error);
                        }

                        throw new Exception("A failed login attempt with email: "
                                + userIfExistsEmail + ". Reason: " + error);
                    } catch (Exception e) {
                        //event notifier increase user's failed count + 1
                    }
                })*///.and()
                .authorizeRequests()
                //Allow all white-listed urls without authentication
                .antMatchers(WHITE_LIST_URLS).permitAll()
                .anyRequest()
                .authenticated().and().logout().deleteCookies()
                .and()
                .httpBasic();/*.and()
                .sessionManagement()
                .maximumSessions(1).maxSessionsPreventsLogin(true)
                .expiredUrl("/user/loggedout");*/
    }

    //Cross-origin domain configurer, replace wild card(*) with frontend server ip
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }


    //Data access object spring security configuration
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
