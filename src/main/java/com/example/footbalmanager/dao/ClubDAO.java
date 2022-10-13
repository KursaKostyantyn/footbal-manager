package com.example.footbalmanager.dao;

import com.example.footbalmanager.models.Club;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClubDAO extends JpaRepository<Club,Integer> {
}
