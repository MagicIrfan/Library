package org.irfan.library.services;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.model.Role;
import org.irfan.library.dao.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }
    public Role getRole(String name){
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Rôle non trouvé avec le nom: " + name));
    }
}
