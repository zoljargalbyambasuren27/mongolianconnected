package com.example.mongolianconnected.service;

import com.example.mongolianconnected.entity.RefreshToken;
import com.example.mongolianconnected.entity.User;
import com.example.mongolianconnected.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long REFRESH_EXPIRATION_MS = 1000L * 60 * 60 * 24 * 7; // 7 days
//    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60; // 1 минут test
//test
    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user) {
        // Хэрвээ хуучин token байвал revoked болгох
        refreshTokenRepository.findByUserAndRevokedFalse(user)
                .forEach(t -> {
                    t.setRevoked(true);
                    refreshTokenRepository.save(t);
                });

        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusMillis(REFRESH_EXPIRATION_MS));
        token.setRevoked(false);
        token.setCreatedAt(Instant.now());

        return refreshTokenRepository.save(token);
    }

    public boolean isTokenValid(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(Instant.now()) && !t.isRevoked())
                .isPresent();
    }

    public void revokeToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    public User getUserByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(Instant.now()) && !t.isRevoked())
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public class RefreshTokenNotFoundException extends RuntimeException {
        public RefreshTokenNotFoundException(String message) {
            super(message);
        }
    }
}
