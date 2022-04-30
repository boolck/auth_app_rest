package com.boolck.authdemo.controller;

import com.boolck.authdemo.exception.AlreadyExistsException;
import com.boolck.authdemo.exception.TokenExpiredException;
import com.boolck.authdemo.repository.RoleRepository;
import com.boolck.authdemo.repository.UserRepository;
import com.boolck.authdemo.exception.NotFoundException;
import com.boolck.authdemo.exception.NotSupportedException;
import com.boolck.authdemo.model.Role;
import com.boolck.authdemo.model.User;
import com.boolck.authdemo.security.Token;
import com.boolck.authdemo.security.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Value("${allow.anonymous}")
    private boolean allowAnonymous=true;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("user")
    public User addUser(@RequestParam("user") String username, @RequestParam("password") String pwd) throws AlreadyExistsException {
        Token token = TokenUtil.getToken(username,pwd);

        if(userRepository.checkUser(token.getTokenId())){
            throw new AlreadyExistsException("User with token already exists");
        }
        return userRepository.addUser(username,token);
    }

    @GetMapping("user")
    public User getUser(@RequestParam("token") String tokenId) throws TokenExpiredException, NotFoundException {
        return userRepository.getUser(tokenId);
    }

    @DeleteMapping("user")
    public void deleteUser(@RequestParam("user") User user) throws NotFoundException {
        userRepository.deleteUser(user.getToken().getTokenId());
    }

    @PostMapping("addrole")
    public User addRole(@RequestParam("user") User user, @RequestParam("role") Role role) throws TokenExpiredException, NotFoundException {
        userRepository.getUser(user.getToken().getTokenId());
        user.addRole(role);
        roleRepository.addRoleToUser(role,user);
        return user;
    }

    @GetMapping("authenticate")
    public String authenticate(@RequestParam("user") String username, @RequestParam("password") String pwd) throws TokenExpiredException, NotFoundException {
        Token token = TokenUtil.getToken(username,pwd);
        User u =  userRepository.getUser(token.getTokenId());
        return u.getToken().getTokenId();
    }

    @GetMapping("authenticateAnonymous")
    public String authenticateAnonymous() throws TokenExpiredException, NotFoundException, NotSupportedException {
        if (!allowAnonymous) {
            throw new NotSupportedException("Anonymous login is not supported");
        }
        Token token = TokenUtil.getToken("guest", "guest");
        userRepository.addUser("guest", token);
        User u = userRepository.getUser(token.getTokenId());
        return u.getToken().getTokenId();
    }

    @PostMapping("invalidate")
    public void invalidate(@RequestParam("token") String token) throws NotFoundException {
       userRepository.invalidate(token);
    }

    @PostMapping("checkrole")
    public boolean checkRole(@RequestParam("token") String token, @RequestParam("role") Role role) throws TokenExpiredException, NotFoundException {
        User user = userRepository.getUser(token);
        return user.containsRole(role);
    }

    @PostMapping("allroles")
    public Set<Role> allRoles(@RequestParam("token") String token) throws TokenExpiredException, NotFoundException {
        User user = userRepository.getUser(token);
        return user.getRoles();
    }

}
