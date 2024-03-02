package org.irfan.library;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.irfan.library.Model.*;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dao.BookTypeRepository;
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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorRepository authorRepository;
    private final BookTypeRepository bookTypeRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthorRepository authorRepository, BookTypeRepository bookTypeRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorRepository = authorRepository;
        this.bookTypeRepository = bookTypeRepository;
    }

    @Value("${app.admin.password}")
    private String adminPassword;

    @PostConstruct
    @Transactional
    public void init() {
        createRoles();
        createAdminUser();
        createAuthors();
        createBookTypes();
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

    private void createAuthors(){
        if(authorRepository.count() == 0) {
            List<Author> authors = new ArrayList<>();
            authors.add(new Author("Albert", "Camus"));
            authors.add(new Author("Guillaume", "Apollinaire"));
            authors.add(new Author("Victor", "Hugo"));
            authorRepository.saveAll(authors);
        }
    }

    private void createBookTypes(){
        if(bookTypeRepository.count() == 0){
            List<Type> bookTypes = new ArrayList<>();
            bookTypes.add(new Type("Roman"));
            bookTypes.add(new Type("Nouvelle"));
            bookTypes.add(new Type("Science-fiction"));
            bookTypes.add(new Type("Fantasy"));
            bookTypes.add(new Type("Policier"));
            bookTypes.add(new Type("Horreur"));
            bookTypes.add(new Type("Romance"));
            bookTypes.add(new Type("Biographie"));
            bookTypes.add(new Type("Autobiographie"));
            bookTypes.add(new Type("Essai"));
            bookTypes.add(new Type("Histoire"));
            bookTypes.add(new Type("Science et Éducation"));
            bookTypes.add(new Type("Développement personnel"));
            bookTypes.add(new Type("Poésie"));
            bookTypes.add(new Type("Théâtre"));
            bookTypes.add(new Type("Bande dessinée"));
            bookTypes.add(new Type("Roman graphique"));
            bookTypes.add(new Type("Littérature jeunesse"));
            bookTypes.add(new Type("Guide et manuel"));
            bookTypes.add(new Type("Manga"));
            bookTypeRepository.saveAll(bookTypes);
        }
    }
}