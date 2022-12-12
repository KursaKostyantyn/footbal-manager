package com.example.footbalmanager.services;

import com.example.footbalmanager.constants.Role;
import com.example.footbalmanager.models.Club;
import com.example.footbalmanager.models.Player;
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
import org.springframework.web.bind.annotation.RequestParam;


import java.security.SecureRandom;
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
            save(new CustomUser("admin", "admin", "forjava2022@gmail.com\n", Role.ROLE_ADMIN, true));
        }
        if (customUserDAO.findCustomUserByLogin("user") == null) {
            save(new CustomUser("user", "user", "forjava2022@gmail.com\n", Role.ROLE_USER, true));
        }
    }

    private CustomUserDTO convertCustomUserToCustomUserDTO(CustomUser customUser) {
        return new CustomUserDTO(customUser.getLogin(), customUser.getEmail());
    }

    private String createRandomPassword(int length) {
        SecureRandom secureRandom = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public ResponseEntity<CustomUserDTO> save(CustomUser customUser) {

        if (customUser != null) {
            customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));
            customUserDAO.save(customUser);
            if (customUser.getId() != 0) {
                mailService.sendMailForActivateUser(customUser);
            }
            return new ResponseEntity<>(convertCustomUserToCustomUserDTO(customUser), HttpStatus.OK);
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

    public ResponseEntity<String> login(CustomUser customUser) {
        if (!customUserDAO.findCustomUserByLogin(customUser.getLogin()).isActivated()) {
            return new ResponseEntity<>("The user is not activated", HttpStatus.BAD_REQUEST);
        }
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        customUser.getLogin(), customUser.getPassword()
                )
        );
        if (authenticate != null) {
            String jwtToken = Jwts.builder()
                    .setSubject(authenticate.getName())
                    .signWith(SignatureAlgorithm.HS512, "secretKey".getBytes())
                    .setExpiration(new Date(System.currentTimeMillis() + 50000))
                    .compact();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtToken);
            return new ResponseEntity<>(jwtToken + "//" + customUser.getLogin(), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>("bad credentials", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<?> activateCustomUser(int id) {
        CustomUser customUser = customUserDAO.findById(id).orElse(new CustomUser());
        if (customUser.getLogin() != null) {
            customUser.setActivated(true);
            customUserDAO.save(customUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<CustomUserDTO> addPlayerToCustomUser(String customUserLogin, Player player) {
        CustomUser customUser = findCustomUserByLogin(customUserLogin);
        if (customUser.getLogin() != null && player != null) {
            customUser.getPlayers().add(player);
            customUserDAO.save(customUser);
            player.setCustomUser(customUser);
            return new ResponseEntity<>(convertCustomUserToCustomUserDTO(customUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<CustomUserDTO> addClubToCustomer(String customUserLogin, Club club) {
        CustomUser customUser = findCustomUserByLogin(customUserLogin);
        if (customUser.getLogin() != null && club != null) {
            customUser.getClubs().add(club);
            customUserDAO.save(customUser);
            club.setCustomUser(customUser);
            return new ResponseEntity<>(convertCustomUserToCustomUserDTO(customUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> resetPassword(String userLogin) {
        CustomUser customUser = findCustomUserByLogin(userLogin);
        String resetPassword = createRandomPassword(8);
        customUser.setResetPassword(passwordEncoder.encode(resetPassword));
        customUser.setResetPasswordExpiryDate(new Date().getTime() + 1000000);
        customUserDAO.save(customUser);
        mailService.sendMailForResetPassword(customUser, resetPassword);
        return null;
    }


    public ResponseEntity<?> createNewPassword(String resetPassword, CustomUser customUser) {

        CustomUser currentCustomUser = findCustomUserByLogin(customUser.getLogin());
        if (
                currentCustomUser.getLogin() != null &&
                        currentCustomUser.getResetPassword() != null &&
                        passwordEncoder.matches(resetPassword, currentCustomUser.getResetPassword()) &&
                        currentCustomUser.getResetPasswordExpiryDate() > new Date().getTime()
        ) {
            currentCustomUser.setPassword(passwordEncoder.encode(customUser.getPassword()));
            currentCustomUser.setResetPassword(null);
            currentCustomUser.setResetPasswordExpiryDate(0);
            customUserDAO.save(currentCustomUser);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
