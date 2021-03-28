package com.hsbcdev.authdemo.controller;

import com.hsbcdev.authdemo.exception.AlreadyExistsException;
import com.hsbcdev.authdemo.exception.NotFoundException;
import com.hsbcdev.authdemo.exception.NotSupportedException;
import com.hsbcdev.authdemo.exception.TokenExpiredException;
import com.hsbcdev.authdemo.model.Role;
import com.hsbcdev.authdemo.model.User;
import com.hsbcdev.authdemo.repository.InMemoryRoleRepository;
import com.hsbcdev.authdemo.repository.InMemoryUserRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserControllerTest {

    private UserController userController;

    @Before
    public void setUp() {
        userController = new UserController(new InMemoryUserRepository(), new InMemoryRoleRepository());
    }

    @Test
    public void testAddUser() throws NotFoundException, AlreadyExistsException, TokenExpiredException {
        User u = userController.addUser("test","pwd");
        assertNotNull(u);
        assertEquals(u,userController.getUser(u.getToken().getTokenId()));
    }

    @Test (expected= AlreadyExistsException.class)
    public void testExistingUser() throws AlreadyExistsException {
        User u = userController.addUser("test","pwd");
        assertNotNull(u);
        userController.addUser("test","pwd");
    }

    @Test (expected=NotFoundException.class)
    public void testUserNotFound() throws NotFoundException, TokenExpiredException {
        userController.getUser("test");
    }

    @Test
    public void testTokenValid() throws NotFoundException, AlreadyExistsException, TokenExpiredException {
        User u = userController.addUser("test","pwd");
        assertFalse(u.getToken().isExpired());
    }

    @Test (expected=NotFoundException.class)
    public void deleteUser()throws NotFoundException, AlreadyExistsException, TokenExpiredException {
        User u = userController.addUser("test","pwd");
        userController.deleteUser(u);
        userController.getUser(u.getToken().getTokenId());
    }

    @Test (expected=NotFoundException.class)
    public void invalidateUser()throws NotFoundException, AlreadyExistsException, TokenExpiredException {
        User u = userController.addUser("test","pwd");
        String tokenId = u.getToken().getTokenId();
        userController.invalidate(tokenId);
        userController.getUser(tokenId);
    }

    @Test
    public void testAddRole() throws NotFoundException, AlreadyExistsException, TokenExpiredException {
        User u = userController.addUser("test","pwd");
        Role ro = new Role("RO");
        userController.addRole(u,ro);
        assertTrue(u.getRoles().contains(ro));
        assertTrue(userController.checkRole(u.getToken().getTokenId(),ro));
    }

    @Test
    public void testAuthenticate() throws NotFoundException, AlreadyExistsException, TokenExpiredException {
        User u = userController.addUser("test","pwd");
        assertNotNull(u);
        String tokenId = userController.authenticate("test","pwd");
        assertEquals(u.getToken().getTokenId(),tokenId);
    }

    @Test
    public void testAuthenticateAnonymous() throws NotFoundException, AlreadyExistsException, TokenExpiredException, NotSupportedException {
        String tokenId = userController.authenticateAnonymous();
        assertNotNull(tokenId);
    }

    @Test
    public void testGetAllRoles() throws NotFoundException, AlreadyExistsException, TokenExpiredException {
        User u = userController.addUser("test","pwd");
        userController.addRole(u,new Role("RO"));
        userController.addRole(u,new Role("RW"));
        assertEquals(2,userController.allRoles(u.getToken().getTokenId()).size());
    }


}
