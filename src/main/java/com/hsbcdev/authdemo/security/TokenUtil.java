package com.hsbcdev.authdemo.security;

import java.util.Base64;

public class TokenUtil {

    public static final long EXPIRY_MS = 2*60*60*1000;

    public static Token getToken(String username, String pwd) {
        byte[] encode = Base64.getUrlEncoder().encode((username + pwd).getBytes());
        String tokenId =  "Bearer " + new String(encode);
        Token token = new Token(tokenId,System.currentTimeMillis()+EXPIRY_MS);
        return token;
    }
}
