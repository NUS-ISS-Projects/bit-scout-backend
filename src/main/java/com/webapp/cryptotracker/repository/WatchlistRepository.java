package com.webapp.cryptotracker.repository;

import com.webapp.cryptotracker.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, String> {
    Optional<Watchlist> findByUserId(String userId);
}
