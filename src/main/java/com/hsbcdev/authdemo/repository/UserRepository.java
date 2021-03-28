package com.hsbcdev.authdemo.repository;

import com.hsbcdev.authdemo.exception.NotFoundException;
import com.hsbcdev.authdemo.exception.TokenExpiredException;
import com.hsbcdev.authdemo.model.User;
import com.hsbcdev.authdemo.security.Token;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    User addUser(String username, Token token);

    User getUser(String token) throws TokenExpiredException, NotFoundException;

    void deleteUser(String token) throws NotFoundException;

    void invalidate(String token) throws NotFoundException;

    boolean checkUser(String tokenId);
}
