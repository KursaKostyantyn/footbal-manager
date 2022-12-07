package com.example.footbalmanager.services;

import com.example.footbalmanager.constants.Role;
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

    private MailService mailService;

    @Bean
    private void autoCreateCustomUser() {
        if (customUserDAO.findCustomUserByLogin("admin") == null) {
            save(new CustomUserDTO("admin", "admin", "forjava2022@gmail.com\n", Role.ROLE_ADMIN));
            activateCustomUser(customUserDAO.findCustomUserByLogin("admin").getId());
        }
        if (customUserDAO.findCustomUserByLogin("user") == null) {
            save(new CustomUserDTO("user", "user", "forjava2022@gmail.com\n", Role.ROLE_USER));
            activateCustomUser(customUserDAO.findCustomUserByLogin("user").getId());
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
            if (customUserDTO.getRole() != null) customUser.setRole(customUserDTO.getRole());
            customUserDAO.save(customUser);
            if (customUser.getId()!=0){
                mailService.sendMail(customUser);
            }
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
        if (!customUserDAO.findCustomUserByLogin(customUserDTO.getLogin()).isActivated()){
            return new ResponseEntity<>("The user is not activated",HttpStatus.BAD_REQUEST);
        }
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

    public ResponseEntity<?> activateCustomUser (int id){
        CustomUser customUser = customUserDAO.findById(id).orElse(new CustomUser());
        if (customUser.getLogin() != null){
            customUser.setActivated(true);
            customUserDAO.save(customUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
