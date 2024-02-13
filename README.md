# Cahier des Charges: API de Gestion de Livres avec Spring Boot

## Objectif

Développer une API RESTful pour gérer une collection de livres, permettant les opérations CRUD (Créer, Lire, Mettre à jour, Supprimer) sur les livres.

## Fonctionnalités

- **Création de livre**: Permettre aux utilisateurs d'ajouter de nouveaux livres à la collection.
- **Liste des livres**: Fournir une liste de tous les livres disponibles dans la collection.
- **Détails d'un livre**: Afficher les détails d'un livre spécifique lorsque son ID est fourni.
- **Mise à jour de livre**: Permettre la mise à jour des informations d'un livre existant.
- **Suppression de livre**: Permettre la suppression d'un livre de la collection.

## Modèle de données

### Livre
- ID (unique, généré automatiquement)
- Titre
- Auteur
- ISBN
- Date de publication (optionnel)
- Langue (optionnel)
- Nombre de pages (optionnel)

## API Endpoints

| Méthode HTTP | Endpoint           | Description                             |
|--------------|--------------------|-----------------------------------------|
| POST         | `/api/books`       | Ajouter un nouveau livre                |
| GET          | `/api/books`       | Lister tous les livres                  |
| GET          | `/api/books/{id}`  | Afficher les détails d'un livre spécifique |
| PUT          | `/api/books/{id}`  | Mettre à jour un livre existant         |
| DELETE       | `/api/books/{id}`  | Supprimer un livre                      |

## Technologies et Outils

- **Spring Boot**: Pour le framework de développement.
- **Spring Data JPA**: Pour l'interaction avec la base de données.
- **H2 Database**: Comme base de données en mémoire pour le développement et les tests.
- **Maven ou Gradle**: Pour la gestion des dépendances et la construction du projet.
- **Lombok**: Pour réduire le boilerplate code (optionnel).
- **Swagger ou SpringDoc**: Pour la documentation de l'API (optionnel).

## Sécurité (Optionnel)

- Implémenter une authentification de base ou JWT pour sécuriser l'accès à l'API.

## Documentation

- Fournir une documentation API, idéalement générée automatiquement à l'aide de Swagger ou SpringDoc.

## Tests

- Écrire des tests unitaires et d'intégration pour s'assurer que l'API fonctionne comme prévu.
