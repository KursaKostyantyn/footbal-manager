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
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CustomUserService {

    private CustomUserDAO customUserDAO;

    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    private MailService mailService;

    @Bean
    private void autoCreateCustomUser() {
        if (customUserDAO.findCustomUserByLogin("superAdmin") == null) {
            save(new CustomUser("superAdmin", "admin", "forjava2022@gmail.com", Role.ROLE_SUPERADMIN, true));
        }
        if (customUserDAO.findCustomUserByLogin("admin") == null) {
            save(new CustomUser("admin", "admin", "forjava2022@gmail.com", Role.ROLE_ADMIN, true));
        }
        if (customUserDAO.findCustomUserByLogin("user") == null) {
            save(new CustomUser("user", "user", "forjava2022@gmail.com", Role.ROLE_USER, true));
        }
    }

    private CustomUserDTO convertCustomUserToCustomUserDTO(CustomUser customUser) {
        CustomUserDTO customUserDTO = new CustomUserDTO();
        customUserDTO.setId(customUser.getId());
        customUserDTO.setLogin(customUser.getLogin());
        customUserDTO.setEmail(customUser.getEmail());
        customUserDTO.setRole(customUser.getRole());
        customUserDTO.setActivated(customUser.isActivated());
        customUserDTO.setBlocked(customUser.isBlocked());
        customUserDTO.setPlayers(customUser.getPlayers());
        customUserDTO.setClubs(customUser.getClubs());
        customUserDTO.setPhoto(customUser.getPhoto());
        return customUserDTO;
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

    public ResponseEntity<CustomUserDTO> saveWithoutSendingMail(CustomUser customUser) {

        if (customUser != null) {
            customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));
            customUserDAO.save(customUser);
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
        if (customUserDAO.findCustomUserByLogin(customUser.getLogin()).isBlocked()) {
            return new ResponseEntity<>("The user is blocked. Contact the admin", HttpStatus.BAD_REQUEST);
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
                    .setExpiration(new Date(System.currentTimeMillis() + 5000000))
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

    public ResponseEntity<?> blockUser(String login, boolean isBlocked) {
        CustomUser customUser = customUserDAO.findCustomUserByLogin(login);
        if (customUser.getLogin() != null) {
            customUser.setBlocked(isBlocked);
            customUserDAO.save(customUser);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<CustomUserDTO> deleteCustomUserByLogin(String login) {
        CustomUser customUser = customUserDAO.findCustomUserByLogin(login);
        if (customUser.getRole() == Role.ROLE_SUPERADMIN) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (customUser.getLogin() != null) {
            customUserDAO.delete(customUser);
            return new ResponseEntity<>(convertCustomUserToCustomUserDTO(customUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<CustomUserDTO> updateCustomUser(CustomUser customUser,int id) {
        CustomUser oldCustomUser = customUserDAO.findById(id).orElse(new CustomUser());
        if (oldCustomUser.getLogin() != null) {
            oldCustomUser.setLogin(customUser.getLogin());
            oldCustomUser.setEmail(customUser.getEmail());
            oldCustomUser.setRole(customUser.getRole());
            oldCustomUser.setBlocked(customUser.isBlocked());
            oldCustomUser.setActivated(customUser.isActivated());
            if (customUser.getPassword().equals("")) {
                oldCustomUser.setPassword(passwordEncoder.encode(customUser.getPassword()));
            }
            customUserDAO.save(oldCustomUser);
            return new ResponseEntity<>(convertCustomUserToCustomUserDTO(oldCustomUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<List<CustomUserDTO>> getAllCustomUsers() {
        return new ResponseEntity<>(customUserDAO.findAll()
                .stream()
                .map(this::convertCustomUserToCustomUserDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<CustomUserDTO> saveUserPhoto (MultipartFile photo, int id){
        String originalFilename = photo.getOriginalFilename();
        File usersPhoto = new File("usersPhoto");

        if (!usersPhoto.exists()) {
            usersPhoto.mkdir();
        }

        String pathToSavePhoto = usersPhoto.getAbsolutePath() + File.separator + originalFilename;

        try {
            photo.transferTo(new File(pathToSavePhoto));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CustomUser customUser = customUserDAO.findById(id).orElse(new CustomUser());

        if (customUser.getLogin() != null) {
            customUser.setPhoto(originalFilename);
            customUserDAO.save(customUser);
            return new ResponseEntity<>(convertCustomUserToCustomUserDTO(customUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
