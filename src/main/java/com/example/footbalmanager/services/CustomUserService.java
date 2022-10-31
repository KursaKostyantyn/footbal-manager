package com.example.footbalmanager.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.footbalmanager.dao.CustomUserDAO;
import com.example.footbalmanager.models.CustomUser;
import com.example.footbalmanager.models.dto.CustomUserDTO;


import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class CustomUserService {

    private CustomUserDAO customUserDAO;

    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @Bean
    private void autoCreateAdminUser() {
        if (customUserDAO.findCustomUserByLogin("admin") == null) {
            save(new CustomUserDTO("admin", "admin", "admin@test.com"));
        }
    }

    private CustomUserDTO convertCustomUserToCustomUserDTO(CustomUser customUser) {
        return new CustomUserDTO();
    }

    public ResponseEntity<CustomUserDTO> save(CustomUserDTO customUserDTO) {

        if (customUserDTO != null) {
            CustomUser customUser = new CustomUser();
            customUser.setLogin(customUserDTO.getLogin());
            customUser.setPassword(passwordEncoder.encode(customUserDTO.getPassword()));
            customUser.setEmail(customUserDTO.getEmail());
            customUserDAO.save(customUser);
            return new ResponseEntity<>(customUserDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public List<CustomUser> findAll() {
        return customUserDAO.findAll();
    }


    public CustomUser findCustomUserByLogin(String CustomUserLogin) {
        return customUserDAO.findCustomUserByLogin(CustomUserLogin);

    }

    public ResponseEntity<String> login(CustomUserDTO customUserDTO) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        customUserDTO.getLogin(), customUserDTO.getPassword()
                )
        );
        if (authenticate != null) {
            String jwtToken = Jwts.builder()
                    .setSubject(authenticate.getName())
                    .signWith(SignatureAlgorithm.HS512, "secretKey".getBytes())
                    .setExpiration(new Date(System.currentTimeMillis() + 500000))
                    .compact();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtToken);
            return new ResponseEntity<>(jwtToken, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>("bad credentials", HttpStatus.FORBIDDEN);
    }

}
