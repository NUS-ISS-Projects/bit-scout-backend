package com.webapp.cryptotracker.service;

import com.webapp.cryptotracker.entity.Watchlist;
import com.webapp.cryptotracker.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    public void addToWatchlist(String userId, String cryptoId) {
        Watchlist watchlist = watchlistRepository.findByUserId(userId)
                .orElse(new Watchlist(userId));
        watchlist.addCrypto(cryptoId);
        watchlistRepository.save(watchlist);
    }

    public void removeFromWatchlist(String userId, String cryptoId) {
        Watchlist watchlist = watchlistRepository.findByUserId(userId)
                .orElse(new Watchlist(userId));
        watchlist.removeCrypto(cryptoId);
        watchlistRepository.save(watchlist);
    }

    public List<String> getWatchlist(String userId) {
        return watchlistRepository.findByUserId(userId)
                .map(Watchlist::getCryptos)
                .orElse(Collections.emptyList());
    }

    public boolean isInWatchlist(String userId, String cryptoId) {
        return watchlistRepository.findByUserId(userId)
                .map(watchlist -> watchlist.getCryptos().contains(cryptoId))
                .orElse(false);
    }
}
