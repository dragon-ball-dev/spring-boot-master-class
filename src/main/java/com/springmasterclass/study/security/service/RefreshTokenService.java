package com.springmasterclass.study.security.service;

import com.springmasterclass.study.entity.auth.RefreshToken;
import com.springmasterclass.study.entity.auth.User;
import com.springmasterclass.study.repository.auth.AuthUserRepository;
import com.springmasterclass.study.repository.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final AuthUserRepository authUserRepository;

    @Transactional
    public RefreshToken createRefreshToken(String userId) {
        User user = authUserRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not exist!"));

        refreshTokenRepository.deleteAllByUserId(userId);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken){
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new RuntimeException("Refresh token expiry!");
        }

        return refreshToken;
    }

    @Transactional
    public void revokeAllUserToken(String userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }
}
