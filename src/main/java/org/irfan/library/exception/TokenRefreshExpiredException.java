package org.irfan.library.exception;

import java.io.Serial;

public class TokenRefreshExpiredException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TokenRefreshExpiredException(String token) {
        super(token + " Refresh token is expired. Please make a new login.");
    }
}