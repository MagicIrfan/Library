[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/summary/new_code?id=MagicIrfan_Library)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=MagicIrfan_Library&metric=vulnerabilities)](https://sonarcloud.io/project/overview?id=MagicIrfan_Library)
[![Duplications](https://sonarcloud.io/api/project_badges/measure?project=MagicIrfan_Library&metric=duplicated_lines_density)](https://sonarcloud.io/project/overview?id=MagicIrfan_Library)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=MagicIrfan_Library&metric=sqale_rating)](https://sonarcloud.io/project/overview?id=MagicIrfan_Library)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=MagicIrfan_Library&metric=bugs)](https://sonarcloud.io/project/overview?id=MagicIrfan_Library)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=MagicIrfan_Library&metric=code_smells)](https://sonarcloud.io/project/overview?id=MagicIrfan_Library)
 # API de Gestion de Livres avec Spring Boot

## Objectif

Développer une API RESTful pour gérer une collection de livres, permettant les opérations CRUD (Créer, Lire, Mettre à jour, Supprimer) sur les livres.

## Fonctionnalités
### Livres
- **Création de livre**: Permettre aux administrateurs d'ajouter de nouveaux livres à la collection.
- **Liste des livres**: Fournir une liste de tous les livres disponibles dans la collection.
- **Détails d'un livre**: Afficher les détails d'un livre spécifique lorsque son ID est fourni.
- **Mise à jour de livre**: Permettre la mise à jour des informations d'un livre existant.
- **Suppression de livre**: Permettre la suppression d'un livre de la collection.

### Auteurs
- **Création d'un auteur**: Permettre aux administrateurs d'ajouter de nouveaux auteurs à la collection.
- **Liste des auteurs**: Fournir une liste de tous les auteurs disponibles.
- **Détails d'un auteur**: Afficher les détails d'un auteur spécifique lorsque son ID est fourni.
- **Mise à jour d'un auteur**: Permettre la mise à jour des informations d'un auteur existant.
- **Suppression d'un auteur**: Permettre la suppression d'un auteur de la collection.

### Genres
- **Création d'un genre**: Permettre aux administrateurs d'ajouter de nouveaux genres à la collection.
- **Liste des genres**: Fournir une liste de tous les genres disponibles.
- **Détails d'un genre**: Afficher les détails d'un genre spécifique lorsque son ID est fourni.
- **Mise à jour d'un genre**: Permettre la mise à jour des informations d'un genre existant.
- **Suppression d'un genre**: Permettre la suppression d'un genre de la collection.

### Utilisateurs
- **Création d'un utilisateur**: Permettre aux administrateurs d'ajouter de nouveaux utilisateurs.
- **Liste des utilisateur**: Fournir une liste de tous les utilisateurs disponibles.
- **Détails d'un utilisateur**: Afficher les détails d'un utilisateur spécifique lorsque son ID est fourni.
- **Mise à jour d'un utilisateur**: Permettre la mise à jour des informations d'un utilisateur existant.
- **Suppression d'un utilisateur**: Permettre la suppression d'un utilisateur de la collection.

## API Endpoints

| Route                      | Méthode | Tags                | OperationID           | Description                      |
|----------------------------|---------|---------------------|-----------------------|----------------------------------|
| `/api/v1/signup`           | POST    | auth-controller     | signup                | Inscription d'un nouvel utilisateur |
| `/api/v1/refreshToken`     | POST    | auth-controller     | refreshToken          | Rafraîchissement du jeton d'accès |
| `/api/v1/logout`           | POST    | auth-controller     | logout                | Déconnexion de l'utilisateur     |
| `/api/v1/login`            | POST    | auth-controller     | login                 | Connexion de l'utilisateur       |
| `/api/v1/booktypes`        | GET     | book-type-controller| getBookTypes          | Liste de tous les types de livres |
| `/api/v1/booktypes`        | POST    | book-type-controller| createBookType        | Ajout d'un nouveau type de livre  |
| `/api/v1/booktypes/{id}`   | GET     | book-type-controller| getBookTypeById       | Détails d'un type de livre par ID |
| `/api/v1/booktypes/{id}`   | PATCH   | book-type-controller| editBookType          | Mise à jour d'un type de livre par ID |
| `/api/v1/books`            | GET     | book-controller     | getBooks              | Liste de tous les livres         |
| `/api/v1/books`            | POST    | book-controller     | createBook            | Ajout d'un nouveau livre         |
| `/api/v1/books/{id}`       | DELETE  | book-controller     | deleteBook            | Suppression d'un livre par ID    |
| `/api/v1/books/{id}`       | PATCH   | book-controller     | editBook              | Mise à jour d'un livre par ID    |
| `/api/v1/authors`          | GET     | author-controller   | getAuthors            | Liste de tous les auteurs        |
| `/api/v1/authors`          | POST    | author-controller   | createAuthor          | Ajout d'un nouvel auteur         |
| `/api/v1/authors/{id}`     | GET     | author-controller   | getAuthorById         | Détails d'un auteur par ID       |
| `/api/v1/authors/{id}`     | DELETE  | author-controller   | deleteAuthor          | Suppression d'un auteur par ID   |
| `/api/v1/authors/{id}`     | PATCH   | author-controller   | editAuthor            | Mise à jour d'un auteur par ID   |
| `/api/v1/users/{id}`       | GET     | user-controller     | getUser_1             | Détails d'un utilisateur par ID  |
| `/api/v1/users/{id}`       | DELETE  | user-controller     | deleteUser            | Suppression d'un utilisateur par ID |
| `/api/v1/users/{id}`       | PATCH   | user-controller     | editUser              | Mise à jour d'un utilisateur par ID |
| `/api/v1/users`            | GET     | user-controller     | getUser               | Liste filtrée des utilisateurs   |
| `/api/v1/users/all`        | GET     | user-controller     | getAllUsers           | Liste de tous les utilisateurs   |

## Technologies et Outils

- **Spring Boot**: Pour le framework de développement.
- **Spring Data JPA**: Pour l'interaction avec la base de données.
- **SQLite**: Comme base de données en mémoire pour le développement et les tests.
- **Maven**: Pour la gestion des dépendances et la construction du projet.
- **Lombok**: Pour réduire le boilerplate code.

## Cloner le dépôt

```git clone https://github.com/MagicIrfan/Library```

## Démarrer l'application

```mvn spring-boot:run```

## Démarrer les tests

```mvn test```
