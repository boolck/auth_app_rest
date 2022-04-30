package com.boolck.authdemo.controller;

import com.boolck.authdemo.exception.AlreadyExistsException;
import com.boolck.authdemo.exception.NotFoundException;
import com.boolck.authdemo.model.Role;
import com.boolck.authdemo.repository.InMemoryRoleRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RoleControllerTest {
    private RoleController roleController;

    @Before
    public void setUp() {
        roleController = new RoleController(new InMemoryRoleRepository());
    }

    @Test
    public void testAddRole() throws NotFoundException, AlreadyExistsException {
        roleController.addRole("RW");
        assertNotNull(roleController.getRole("RW"));
    }

    @Test (expected = AlreadyExistsException.class)
    public void testRoleAlreadyExists() throws NotFoundException, AlreadyExistsException {
        String rw_role= "RW";
        roleController.addRole(rw_role);
        roleController.addRole(rw_role);
    }

    @Test (expected = NotFoundException.class)
    public void testRoleNotFound() throws NotFoundException {
        roleController.getRole("RW");
    }

    @Test (expected = NotFoundException.class)
    public void testDeleteRole() throws NotFoundException, AlreadyExistsException {
        Role rw = new Role("RW");
        roleController.addRole(rw.getName());
        roleController.addRole("RO");
        roleController.deleteRole(rw);
        roleController.getRole(rw.getName());
    }
}
