package com.example.footbalmanager.filters;

import com.example.footbalmanager.dao.CustomUserDAO;
import com.example.footbalmanager.models.CustomUser;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
public class CustomFilter extends OncePerRequestFilter {

    private CustomUserDAO customUserDAO;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")) {
            String token = authorization.replace("Bearer ", "");
            String subject = Jwts.parser()
                    .setSigningKey("secretKey".getBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            CustomUser customUserByLogin = customUserDAO.findCustomUserByLogin(subject);
            if (customUserByLogin != null) {
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                new UsernamePasswordAuthenticationToken(
                                        customUserByLogin.getLogin(),
                                        customUserByLogin.getPassword(),
                                        Collections.singletonList(new SimpleGrantedAuthority(customUserByLogin.getRole().name()))
                                )
                        );
            }

        }

        filterChain
                .doFilter(request, response);
    }
}
