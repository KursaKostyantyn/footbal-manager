package com.example.footbalmanager.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import com.example.footbalmanager.dao.CustomUserDAO;
import com.example.footbalmanager.filters.CustomFilter;
import com.example.footbalmanager.models.CustomUser;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomUserDAO customUserDAO;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomFilter customFilter() {
        return new CustomFilter(customUserDAO);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.PUT.name(),
                HttpMethod.OPTIONS.name(),
                HttpMethod.POST.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.HEAD.name()
        ));
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.csrf().disable();
        http = http.cors().configurationSource(corsConfigurationSource()).and();

        http = http.authorizeHttpRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/clubs").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/clubs").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/clubs/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/clubs/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/clubs").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/players").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/players").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/players/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/players/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/players/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/transfer").hasAnyRole("ADMIN")
                .and();
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        http = http.addFilterBefore(customFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            CustomUser customUser = customUserDAO.findCustomUserByLogin(username);
            return new User(
                    customUser.getLogin(),
                    customUser.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(customUser.getRole().name())));
        });
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


}
