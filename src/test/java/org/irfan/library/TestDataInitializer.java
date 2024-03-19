package org.irfan.library;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.irfan.library.Model.*;
import org.irfan.library.dao.*;
import org.irfan.library.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestConfiguration
@Profile("test")
public class TestDataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorRepository authorRepository;
    private final BookTypeRepository bookTypeRepository;
    private final BookRepository bookRepository;

    @Autowired
    public TestDataInitializer(UserRepository userRepository,
                               RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder,
                               AuthorRepository authorRepository,
                               BookTypeRepository bookTypeRepository,
                               BookRepository bookRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorRepository = authorRepository;
        this.bookTypeRepository = bookTypeRepository;
        this.bookRepository = bookRepository;
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
        createBooks();
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
            authors.add(new Author("Eiichiro", "Oda"));
            authorRepository.saveAll(authors);
        }
    }

    private void createBookTypes(){
        if(bookTypeRepository.count() == 0){
            List<BookType> bookBookTypes = new ArrayList<>();
            bookBookTypes.add(new BookType("Roman"));
            bookBookTypes.add(new BookType("Nouvelle"));
            bookBookTypes.add(new BookType("Science-fiction"));
            bookBookTypes.add(new BookType("Fantasy"));
            bookBookTypes.add(new BookType("Policier"));
            bookBookTypes.add(new BookType("Horreur"));
            bookBookTypes.add(new BookType("Romance"));
            bookBookTypes.add(new BookType("Biographie"));
            bookBookTypes.add(new BookType("Autobiographie"));
            bookBookTypes.add(new BookType("Essai"));
            bookBookTypes.add(new BookType("Histoire"));
            bookBookTypes.add(new BookType("Science et Éducation"));
            bookBookTypes.add(new BookType("Développement personnel"));
            bookBookTypes.add(new BookType("Poésie"));
            bookBookTypes.add(new BookType("Théâtre"));
            bookBookTypes.add(new BookType("Bande dessinée"));
            bookBookTypes.add(new BookType("Roman graphique"));
            bookBookTypes.add(new BookType("Littérature jeunesse"));
            bookBookTypes.add(new BookType("Guide et manuel"));
            bookBookTypes.add(new BookType("Manga"));
            bookTypeRepository.saveAll(bookBookTypes);
        }
    }

    private void createBooks(){
        if(bookRepository.count() == 0){
            // Exemple de récupération d'un auteur et d'un type de livre
            Optional<Author> camus = authorRepository.findByFirstnameAndLastname("Albert","Camus");
            Optional<BookType> roman = bookTypeRepository.findByName("Roman");

            List<Book> books = new ArrayList<>();

            // Assurez-vous que l'auteur et le type de livre existent avant de créer un livre
            if(camus.isPresent() && roman.isPresent()){
                books.add(new Book("L'Étranger", camus.get(), roman.get()));
                books.add(new Book("La Peste", camus.get(), roman.get()));
                // Ajoutez d'autres livres ici
            }

            // Vous pouvez répéter le processus pour d'autres auteurs et types de livres

            // Enregistrez tous les livres préparés
            bookRepository.saveAll(books);
        }
    }
}