package org.irfan.library.services;

import org.irfan.library.Model.Role;
import org.irfan.library.Model.User;
import org.irfan.library.dao.UserRepository;
import org.irfan.library.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;;import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        if (user != null) {
            List<SimpleGrantedAuthority> authorites = List.of(new SimpleGrantedAuthority(user.getRole().getName()));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorites); // Assurez-vous que les rôles/autorisations sont correctement gérés ici
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}