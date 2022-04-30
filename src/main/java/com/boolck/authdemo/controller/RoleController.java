package com.boolck.authdemo.controller;

import com.boolck.authdemo.exception.AlreadyExistsException;
import com.boolck.authdemo.exception.NotFoundException;
import com.boolck.authdemo.model.Role;
import com.boolck.authdemo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {

    @Autowired
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @PostMapping("role")
    public Role addRole(@RequestParam("rolename") String roleName) throws AlreadyExistsException, NotFoundException {
        if(roleRepository.checkRole(roleName)){
            throw new AlreadyExistsException("Role  already exists");
        }
        return roleRepository.addRole(roleName);
    }

    @GetMapping("role")
    public Role getRole(@RequestParam("roleName") String roleName) throws  NotFoundException {
        return roleRepository.getRole(roleName);
    }

    @DeleteMapping("role")
    public void deleteRole(@RequestParam("role") Role role) throws NotFoundException {
        roleRepository.deleteRole(role.getName());
    }
}
