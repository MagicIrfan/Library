package org.irfan.library;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.irfan.library.Model.Role;
import org.irfan.library.Model.User;
import org.irfan.library.dao.RoleRepository;
import org.irfan.library.dao.UserRepository;
import org.irfan.library.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.password}") // Injection de la valeur depuis les propriétés de configuration
    private String adminPassword;

    @PostConstruct
    @Transactional
    public void init() {
        createRoles();
        createAdminUser();
    }

    private void createAdminUser(){
        String adminEmail = "root@root.com";
        if(userRepository.findByEmail(adminEmail).isEmpty()) {
            Optional<Role> role = roleRepository.findByName(RoleEnum.ADMIN.name());
            if(role.isPresent()){
                User user = new User(role.get(),"Irfan",adminEmail,passwordEncoder.encode(adminPassword));
                userRepository.save(user);
            }
        }
    }

    private void createRoles(){
        if(roleRepository.count() == 0) {
            List<Role> roles = new ArrayList<>();
            roles.add(new Role(RoleEnum.ADMIN.name()));
            roles.add(new Role(RoleEnum.USER.name()));
            roleRepository.saveAll(roles);
        }
    }
}