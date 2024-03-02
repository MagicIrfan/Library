package org.irfan.library.services;

import org.irfan.library.Model.Role;
import org.irfan.library.dao.RoleRepository;
import org.irfan.library.dto.GetAuthorDTO;
import org.irfan.library.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> getRole(String name){
        return roleRepository.findByName(name);
    }
}
