package com.boolck.authdemo.repository;

import com.boolck.authdemo.exception.NotFoundException;
import com.boolck.authdemo.exception.TokenExpiredException;
import com.boolck.authdemo.model.User;
import com.boolck.authdemo.security.Token;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    User addUser(String username, Token token);

    User getUser(String token) throws TokenExpiredException, NotFoundException;

    void deleteUser(String token) throws NotFoundException;

    void invalidate(String token) throws NotFoundException;

    boolean checkUser(String tokenId);
}
