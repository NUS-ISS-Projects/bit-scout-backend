package com.webapp.cryptotracker.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Watchlist {

    @Id
    private String userId;

    @ElementCollection
    private List<String> cryptos = new ArrayList<>();

    public Watchlist() {
    }

    public Watchlist(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getCryptos() {
        return cryptos;
    }

    public void setCryptos(List<String> cryptos) {
        this.cryptos = cryptos;
    }

    public void addCrypto(String cryptoId) {
        if (!cryptos.contains(cryptoId)) {
            cryptos.add(cryptoId);
        }
    }

    public void removeCrypto(String cryptoId) {
        cryptos.remove(cryptoId);
    }
}
