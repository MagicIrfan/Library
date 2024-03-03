package org.irfan.library.services;

import org.irfan.library.Model.User;
import org.irfan.library.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    public MyUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().getName()));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities); // Assurez-vous que les rôles/autorisations sont correctement gérés ici
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}