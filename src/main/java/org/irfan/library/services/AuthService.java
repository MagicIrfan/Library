package org.irfan.library.services;

import org.irfan.library.Model.User;
import org.irfan.library.components.JwtTokenProvider;
import org.irfan.library.dao.UserRepository;
import org.irfan.library.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Transactional
    public void signUp(String username, String email, String password){
        User user = new User(username,email,bCryptPasswordEncoder.encode(password));
        if(userExists(username,email)){
            throw new UserAlreadyExistsException("L'utilisateur existe déjà");
        }
        userRepository.save(user);
    }

    public String login(String username, String password) throws Exception {
        authenticate(username, password);
        return tokenProvider.createToken(username);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    private boolean userExists(String username, String email){
        return Optional.ofNullable(userRepository.findByUsernameOrEmail(username,email)).isPresent();
    }
}
