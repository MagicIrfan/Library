[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/summary/new_code?id=MagicIrfan_Library)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=MagicIrfan_Morpion&metric=vulnerabilities)](https://sonarcloud.io/project/overview?id=MagicIrfan_Library)
[![Duplications](https://sonarcloud.io/api/project_badges/measure?project=MagicIrfan_Morpion&metric=duplicated_lines_density)](https://sonarcloud.io/project/overview?id=MagicIrfan_Library)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=MagicIrfan_Morpion&metric=sqale_rating)](https://sonarcloud.io/project/overview?id=MagicIrfan_Library)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=MagicIrfan_Morpion&metric=bugs)](https://sonarcloud.io/project/overview?id=MagicIrfan_Library)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=MagicIrfan_Morpion&metric=code_smells)](https://sonarcloud.io/project/overview?id=MagicIrfan_Library)
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


## Technologies et Outils

- **Spring Boot**: Pour le framework de développement.
- **Spring Data JPA**: Pour l'interaction avec la base de données.
- **SQLite**: Comme base de données en mémoire pour le développement et les tests.
- **Maven**: Pour la gestion des dépendances et la construction du projet.
- **Lombok**: Pour réduire le boilerplate code.
