package com.boolck.authdemo.exception;

public class TokenExpiredException extends Exception{

    public TokenExpiredException(String msg){
        super(msg);
    }
}
