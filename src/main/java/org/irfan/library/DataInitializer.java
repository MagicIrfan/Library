package org.irfan.library;

import jakarta.annotation.PostConstruct;
import org.irfan.library.Model.User;
import org.irfan.library.dao.RoleRepository;
import org.irfan.library.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        User user = new User(null,roleRepository.findByName("ADMIN"),"Irfan","root@root.com",passwordEncoder.encode("saucisse"));
        userRepository.save(user);
    }
}