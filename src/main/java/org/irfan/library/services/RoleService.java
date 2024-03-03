package org.irfan.library.services;

import org.irfan.library.Model.Role;
import org.irfan.library.dao.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }
    public Optional<Role> getRole(String name){
        return roleRepository.findByName(name);
    }
}
