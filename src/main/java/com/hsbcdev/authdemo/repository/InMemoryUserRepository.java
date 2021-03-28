package com.hsbcdev.authdemo.repository;

import com.hsbcdev.authdemo.exception.NotFoundException;
import com.hsbcdev.authdemo.exception.TokenExpiredException;
import com.hsbcdev.authdemo.model.User;
import com.hsbcdev.authdemo.security.Token;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> usersMap = new ConcurrentHashMap<>();
    private final Map<String,Token> tokenMap = new ConcurrentHashMap<>();

    @Override
    public User addUser(String username, Token token) {
        User value = new User(username, token);
        this.usersMap.put(token.getTokenId(), value);
        this.tokenMap.put(token.getTokenId(),token);
        return value;
    }

    @Override
    public User getUser(String token) throws TokenExpiredException, NotFoundException {
        Token t = tokenMap.get(token);
        if(t==null){
            throw new NotFoundException("User not fond");
        }
        else if(t.isExpired()){
            tokenMap.remove(token);
            usersMap.remove(token);
            throw new TokenExpiredException("Token expired");
        }
        return usersMap.get(token);
    }

    @Override
    public void deleteUser(String token) throws NotFoundException {
        User v = usersMap.remove(token);
        if(v==null){
            throw new NotFoundException("User not fond");
        }
        tokenMap.remove(token);
    }

    @Override
    public void invalidate(String token) throws NotFoundException {
        Token t = tokenMap.get(token);
        if(t==null){
            throw new NotFoundException("User not fond");
        }
        else {
            tokenMap.remove(token);
            usersMap.remove(token);
        }
    }

    @Override
    public boolean checkUser(String tokenId) {
        return tokenMap.containsKey(tokenId);
    }


}
