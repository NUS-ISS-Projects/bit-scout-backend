package com.webapp.cryptotracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.cryptotracker.entity.FlightBookmark;

import java.util.List;

public interface FlightBookmarkRepository extends JpaRepository<FlightBookmark, Integer> {
    List<FlightBookmark> findByUserName(String userName);
}
