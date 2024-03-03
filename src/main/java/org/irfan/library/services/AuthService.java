package org.irfan.library.services;

import org.irfan.library.Model.Role;
import org.irfan.library.Model.User;
import org.irfan.library.dao.UserRepository;
import org.irfan.library.enums.RoleEnum;
import org.irfan.library.exception.DuplicateDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RoleService roleService;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder bCryptPasswordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.roleService = roleService;
    }

    @Transactional
    public void signUp(String username, String email, String password){
        Optional<Role> role = roleService.getRole(RoleEnum.USER.toString());
        if(role.isPresent()){
            Optional<User> user = Optional.of(new User(role.get(), username, email, bCryptPasswordEncoder.encode(password)));
            if(userExists(username,email)){
                throw new DuplicateDataException("L'utilisateur existe déjà");
            }
            userRepository.save(user.get());
        }
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
