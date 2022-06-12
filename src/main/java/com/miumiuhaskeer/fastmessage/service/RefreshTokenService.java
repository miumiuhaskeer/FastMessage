package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.entity.RefreshToken;

public interface RefreshTokenService {
    String refreshExpiredToken(Long userId, String refreshToken);
    RefreshToken createRefreshToken(Long userId);
    RefreshToken verifyExpiration(RefreshToken token);
    void deleteByUserId(Long userId);
}
