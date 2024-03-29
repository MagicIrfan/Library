package org.irfan.library.services;

import jakarta.persistence.EntityNotFoundException;
import org.irfan.library.model.User;
import org.irfan.library.dao.UserRepository;
import org.irfan.library.dto.UserDTO;
import org.irfan.library.dto.request.EditUserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void editUser(Integer id, EditUserRequest request){
        User user = getUserEntityById(id);
        Optional.ofNullable(request.getEmail())
                .filter(StringUtils::hasText)
                .ifPresent(user::setEmail);
        Optional.ofNullable(request.getUsername())
                .filter(StringUtils::hasText)
                .ifPresent(user::setUsername);
        Optional.ofNullable(request.getPassword())
                .filter(StringUtils::hasText)
                .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer id){
        boolean userExists = userRepository.existsById(id);
        if(!userExists){
            throw new EntityNotFoundException("Utilisateur non trouvé avec l'ID " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void addUser(User user){
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Integer id){
        return modelMapper.map(getUserEntityById(id), UserDTO.class);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByCriterias(Optional<Integer> id, Optional<String> username, Optional<String> email){
        Optional<User> userOptional = Optional.empty();
        if (username.isPresent() && !username.get().isEmpty() && email.isPresent() && !email.get().isEmpty()) {
            userOptional = userRepository.findByUsernameAndEmail(username.get(), email.get());
        }
        else if (username.isPresent() && !username.get().isEmpty()) {
            userOptional = userRepository.findByUsername(username.get());
        }
        else if (email.isPresent() && !email.get().isEmpty()) {
            userOptional = userRepository.findByEmail(email.get());
        }
        else if (id.isPresent()) {
            userOptional = userRepository.findById(id.get());
        }
        return userOptional.map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("L'utilisateur n'existe pas"));
    }

    @Transactional(readOnly = true)
    public User getUserEntityByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur: " + username));
    }

    @Transactional(readOnly = true)
    public User getUserEntityById(Integer id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'ID': " + id));
    }

    @Transactional(readOnly = true)
    public boolean existsByUsernameOrEmail(String username, String email){
        return userRepository.existsByUsernameOrEmail(username,email);
    }
}
