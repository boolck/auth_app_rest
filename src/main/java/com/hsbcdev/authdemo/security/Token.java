package com.hsbcdev.authdemo.security;

import java.util.Objects;

public final class Token {

    private final String tokenId;
    private final long expiry;

    public Token(String tokenId){
        this(tokenId,System.currentTimeMillis()+TokenUtil.EXPIRY_MS);
    }

    public Token(String tokenId, long expiry){
        this.tokenId = tokenId;
        this.expiry = expiry;
    }

    public boolean isExpired(){
        return System.currentTimeMillis() > expiry;
    }

    public String getTokenId(){
        return tokenId;
    }

    public int hashCode(){
        return Objects.hashCode(tokenId);
    }

    public boolean equals(Object o){
        if(o==null){
            return false;
        }
        Token t = (Token)o;
        return this.tokenId.equals(t.getTokenId());
    }

}
