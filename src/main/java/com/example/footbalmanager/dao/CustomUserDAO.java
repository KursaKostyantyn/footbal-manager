package com.example.footbalmanager.dao;

import com.example.footbalmanager.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserDAO extends JpaRepository<CustomUser, Integer> {

    CustomUser findCustomUserByLogin (String login);


}
