package com.activityWindow_Backend.activityWindow.Services;


import com.activityWindow_Backend.activityWindow.Model.RefreshToken;
import com.activityWindow_Backend.activityWindow.Repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());


        return refreshTokenRepository.save(refreshToken);
    }

    void validateRefreshToken(String token) {
        try {
            refreshTokenRepository.findByToken(token)
                    .orElseThrow(() -> new Exception("Invalid refresh Token"));
        } catch (Exception e) {
            throw new RuntimeException("Token not available in database");
        }
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}