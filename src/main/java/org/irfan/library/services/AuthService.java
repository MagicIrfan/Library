package org.irfan.library.services;

import org.irfan.library.model.Role;
import org.irfan.library.model.TokenBlacklist;
import org.irfan.library.model.User;
import org.irfan.library.dto.request.LogoutRequest;
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

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService tokenProvider;
    private final RoleService roleService;
    private final TokenBlacklistService tokenBlacklistService;

    @Autowired
    public AuthService(UserService userService,
                       PasswordEncoder bCryptPasswordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenService tokenProvider,
                       RoleService roleService,
                       TokenBlacklistService tokenBlacklistService) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.roleService = roleService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Transactional
    public void signUp(String username, String email, String password){
        Role role = roleService.getRole(RoleEnum.USER.toString());
        User user = new User(role, username, email, bCryptPasswordEncoder.encode(password));
        if(userExists(username,email)){
            throw new DuplicateDataException("L'utilisateur existe déjà");
        }
        userService.addUser(user);
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
        return userService.existsByUsernameOrEmail(username,email);
    }

    public void logout(LogoutRequest request){
        String token = request.getToken();
        if(tokenBlacklistService.existsByToken(token)){
            throw new DuplicateDataException("Le token existe déjà !");
        }
        User user = userService.getUserEntityByUsername(tokenProvider.getUsernameFromToken(token));
        TokenBlacklist tokenToBlacklist = TokenBlacklist
                .builder()
                .token(token)
                .userInfo(user)
                .build();
        tokenBlacklistService.addTokenOnBlackList(tokenToBlacklist);
    }
}
