package com.hsbcdev.authdemo.model;

import com.hsbcdev.authdemo.exception.NotFoundException;
import com.hsbcdev.authdemo.exception.TokenExpiredException;
import com.hsbcdev.authdemo.repository.InMemoryUserRepository;
import com.hsbcdev.authdemo.security.Token;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryUserRepositoryTest {

    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();

    @Test
    public void testAddUser() throws NotFoundException, TokenExpiredException {
        User u = new User("test",new Token("testtoken"));
       userRepository.addUser("test",new Token("testtoken"));
       assertEquals(u.getName(),userRepository.getUser("testtoken").getName());
    }

    @Test (expected = NotFoundException.class)
    public void testDeleteUserNotExists() throws NotFoundException {
        userRepository.deleteUser("testtoken");
    }

    @Test
    public void testDeleteUser() throws NotFoundException {
        userRepository.addUser("test",new Token("testtoken"));
        userRepository.deleteUser("testtoken");
    }

    @Test(expected = TokenExpiredException.class)
    public void testTokenExpired() throws TokenExpiredException, NotFoundException {
        userRepository.addUser("test",new Token("testtoken",System.currentTimeMillis()-1));
        userRepository.getUser("testtoken");
    }

    @Test
    public void testAddRole() throws TokenExpiredException, NotFoundException {
        Role ro_role = new Role("RO");
        User u = userRepository.addUser("test",new Token("testtoken"));
        u.addRole(ro_role);
        User p = userRepository.getUser("testtoken");
        assertTrue(p.getRoles().contains(ro_role));
    }

    @Test
    public void testRemoveRole() throws TokenExpiredException, NotFoundException {
        Role ro_role = new Role("RO");
        User u = userRepository.addUser("test",new Token("testtoken"));
        u.addRole(ro_role);
        User p = userRepository.getUser("testtoken");
        p.removeRole(ro_role);
        assertFalse(p.getRoles().contains(ro_role));
    }
}
