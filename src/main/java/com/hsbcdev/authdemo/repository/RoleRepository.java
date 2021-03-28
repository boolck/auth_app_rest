package com.hsbcdev.authdemo.repository;

import com.hsbcdev.authdemo.exception.NotFoundException;
import com.hsbcdev.authdemo.model.Role;
import com.hsbcdev.authdemo.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository {

    Role addRole(String username);

    Role getRole(String roleName) throws NotFoundException;

    void deleteRole(String roleName) throws NotFoundException;

    void addRoleToUser(Role role, User user);

    boolean checkRole(String roleName);
}
