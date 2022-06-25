package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.entity.RefreshToken;

public interface RefreshTokenService {

    /** {@inheritDoc} */
    String refreshExpiredToken(Long userId, String refreshToken);

    /** {@inheritDoc} */
    RefreshToken createRefreshToken(Long userId);

    /** {@inheritDoc} */
    RefreshToken verifyExpiration(RefreshToken token);

    /** {@inheritDoc} */
    void deleteByUserId(Long userId);
}
