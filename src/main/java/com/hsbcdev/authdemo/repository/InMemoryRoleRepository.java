package com.hsbcdev.authdemo.repository;

import com.hsbcdev.authdemo.exception.NotFoundException;
import com.hsbcdev.authdemo.model.Role;
import com.hsbcdev.authdemo.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRoleRepository implements RoleRepository{

    private final Map<String, Role> mapRoles = new ConcurrentHashMap<>();
    private final Map<String, List<User>> roleToUserMap = new ConcurrentHashMap<>();

    @Override
    public Role addRole(String roleName) {
        Role role = new Role(roleName);
        mapRoles.put(roleName,role);
        return role;
    }

    @Override
    public Role getRole(String roleName) throws NotFoundException {
        Role r = mapRoles.get(roleName);
        if(r==null){
            throw new NotFoundException("Role not found");
        }
        return r;
    }

    @Override
    public void deleteRole(String roleName) throws NotFoundException {
        Role r = mapRoles.remove(roleName);
        if(r==null){
            throw new NotFoundException("Role not found");
        }
        List<User> users = roleToUserMap.get(roleName);
        if(users!=null){
            users.forEach(x->x.removeRole(r));
        }
    }

    @Override
    public void addRoleToUser(Role role, User user) {
        roleToUserMap.putIfAbsent(role.getName(),new ArrayList<>());
        roleToUserMap.get(role.getName()).add(user);
    }

    @Override
    public boolean checkRole(String roleName) {
        return mapRoles.containsKey(roleName);
    }
}
