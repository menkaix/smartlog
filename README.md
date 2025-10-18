# SmartLog - API de Rapports de Bug

API REST pour recevoir et gérer des rapports de bug depuis des applications mobiles (Android, iOS) et Web.

## Technologies

- Java 21
- Spring Boot 3.5.6
- Spring Data JPA
- PostgreSQL 16
- Lombok
- Bean Validation
- Docker & Docker Compose
- Spring Boot Actuator

## Démarrage

### Option 1 : Avec Docker (Recommandé)

#### Prérequis

- Docker
- Docker Compose

#### Démarrage rapide

```bash
# Démarrer tous les services (API + PostgreSQL + PgAdmin)
docker-compose up -d

# Vérifier les logs
docker-compose logs -f smartlog-app

# Arrêter les services
docker-compose down

# Arrêter et supprimer les volumes (données)
docker-compose down -v
```

L'application sera accessible sur :

- API: `http://localhost:8080`
- PgAdmin: `http://localhost:5050` (admin@smartlog.com / admin123)

#### Build de l'image Docker seule

```bash
# Construire l'image
docker build -t smartlog-api .

# Lancer le conteneur (nécessite PostgreSQL en cours d'exécution)
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/smartlog \
  -e SPRING_DATASOURCE_USERNAME=smartlog \
  -e SPRING_DATASOURCE_PASSWORD=smartlog123 \
  smartlog-api
```

### Option 2 : En local (développement)

#### Prérequis supplémentaires

- PostgreSQL 16 ou supérieur installé et en cours d'exécution
- Base de données `smartlog` créée

#### Créer la base de données PostgreSQL

```bash
# Se connecter à PostgreSQL
psql -U postgres

# Créer la base de données et l'utilisateur
CREATE DATABASE smartlog;
CREATE USER smartlog WITH PASSWORD 'smartlog123';
GRANT ALL PRIVILEGES ON DATABASE smartlog TO smartlog;
```

### Option 2 : En local (suite)

#### Prérequis

- JDK 21 ou supérieur
- Gradle (wrapper inclus)

#### Lancer l'application

```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

L'application démarre sur `http://localhost:8080`

## Endpoints de l'API

### 1. Créer un rapport de bug

**POST** `/api/bug-reports`

**Exemple complet :**

```bash
curl -X POST http://localhost:8080/api/bug-reports \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Crash lors de la connexion",
    "description": "L'\''application plante lorsque l'\''utilisateur tente de se connecter",
    "platform": "ANDROID",
    "appVersion": "2.1.0",
    "osVersion": "Android 13",
    "deviceModel": "Samsung Galaxy S22",
    "severity": "HIGH",
    "stackTrace": "java.lang.NullPointerException at com.example.LoginActivity.onCreate()",
    "userEmail": "user@example.com",
    "stepsToReproduce": "1. Ouvrir l'\''app\n2. Cliquer sur Se connecter\n3. L'\''app crash"
  }'
```

**Exemple minimal (champs obligatoires uniquement) :**

```bash
curl -X POST http://localhost:8080/api/bug-reports \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Bug simple",
    "description": "Description du bug",
    "platform": "WEB",
    "appVersion": "1.0.0",
    "osVersion": "Windows 11",
    "deviceModel": "Chrome 120"
  }'
```

### 2. Récupérer tous les rapports

**GET** `/api/bug-reports`

```bash
curl -X GET http://localhost:8080/api/bug-reports
```

**Avec formatage JSON (nécessite jq) :**

```bash
curl -X GET http://localhost:8080/api/bug-reports | jq
```

### 3. Récupérer un rapport spécifique

**GET** `/api/bug-reports/{id}`

```bash
curl -X GET http://localhost:8080/api/bug-reports/1
```

### 4. Filtrer par plateforme

**GET** `/api/bug-reports/platform/{platform}`

Plateformes disponibles: `ANDROID`, `IOS`, `WEB`

```bash
# Rapports Android
curl -X GET http://localhost:8080/api/bug-reports/platform/ANDROID

# Rapports iOS
curl -X GET http://localhost:8080/api/bug-reports/platform/IOS

# Rapports Web
curl -X GET http://localhost:8080/api/bug-reports/platform/WEB
```

### 5. Filtrer par statut

**GET** `/api/bug-reports/status/{status}`

Statuts disponibles: `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`

```bash
# Bugs ouverts
curl -X GET http://localhost:8080/api/bug-reports/status/OPEN

# Bugs en cours
curl -X GET http://localhost:8080/api/bug-reports/status/IN_PROGRESS

# Bugs résolus
curl -X GET http://localhost:8080/api/bug-reports/status/RESOLVED

# Bugs fermés
curl -X GET http://localhost:8080/api/bug-reports/status/CLOSED
```

### 6. Mettre à jour le statut d'un rapport

**PATCH** `/api/bug-reports/{id}/status?status={status}`

```bash
# Passer en IN_PROGRESS
curl -X PATCH "http://localhost:8080/api/bug-reports/1/status?status=IN_PROGRESS"

# Passer en RESOLVED
curl -X PATCH "http://localhost:8080/api/bug-reports/1/status?status=RESOLVED"

# Passer en CLOSED
curl -X PATCH "http://localhost:8080/api/bug-reports/1/status?status=CLOSED"
```

### 7. Supprimer un rapport

**DELETE** `/api/bug-reports/{id}`

```bash
curl -X DELETE http://localhost:8080/api/bug-reports/1
```

**Avec affichage du code HTTP :**

```bash
curl -X DELETE http://localhost:8080/api/bug-reports/1 -w "\nHTTP Status: %{http_code}\n"
```

---

## Scénarios d'utilisation

### Scénario complet : Créer et suivre un bug

```bash
# 1. Créer un bug
curl -X POST http://localhost:8080/api/bug-reports \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Erreur 500 sur /api/users",
    "description": "Le serveur renvoie une erreur 500",
    "platform": "WEB",
    "appVersion": "3.0.1",
    "osVersion": "Windows 11",
    "deviceModel": "Firefox 121",
    "severity": "CRITICAL"
  }'

# 2. Lister tous les bugs
curl -X GET http://localhost:8080/api/bug-reports

# 3. Mettre à jour le statut à IN_PROGRESS
curl -X PATCH "http://localhost:8080/api/bug-reports/1/status?status=IN_PROGRESS"

# 4. Marquer comme résolu
curl -X PATCH "http://localhost:8080/api/bug-reports/1/status?status=RESOLVED"

# 5. Fermer le bug
curl -X PATCH "http://localhost:8080/api/bug-reports/1/status?status=CLOSED"
```

### Options cURL utiles

```bash
# Afficher les headers de réponse
curl -i -X GET http://localhost:8080/api/bug-reports

# Mode verbeux (debugging)
curl -v -X GET http://localhost:8080/api/bug-reports

# Sauvegarder la réponse dans un fichier
curl -X GET http://localhost:8080/api/bug-reports -o bugs.json

# Timeout (10 secondes)
curl -X GET http://localhost:8080/api/bug-reports --max-time 10

# Suivre les redirections
curl -L -X GET http://localhost:8080/api/bug-reports
```

## Modèle de données

### BugReportRequest (JSON)

| Champ            | Type   | Requis | Description                                  |
| ---------------- | ------ | ------ | -------------------------------------------- |
| title            | string | Oui    | Titre du bug                                 |
| description      | string | Oui    | Description détaillée                        |
| platform         | enum   | Oui    | ANDROID, IOS, ou WEB                         |
| appVersion       | string | Oui    | Version de l'application                     |
| osVersion        | string | Oui    | Version du système d'exploitation            |
| deviceModel      | string | Oui    | Modèle de l'appareil                         |
| severity         | enum   | Non    | LOW, MEDIUM, HIGH, CRITICAL (défaut: MEDIUM) |
| stackTrace       | string | Non    | Trace de la pile d'exécution                 |
| userEmail        | string | Non    | Email de l'utilisateur                       |
| stepsToReproduce | string | Non    | Étapes pour reproduire le bug                |

### BugReportResponse (JSON)

Contient tous les champs du modèle plus :

- `id` : Identifiant unique
- `status` : Statut actuel (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
- `createdAt` : Date de création
- `updatedAt` : Date de dernière modification

## Exemples de requêtes

### Rapport de bug Android

```json
{
  "title": "Crash au démarrage",
  "description": "L'application plante dès l'ouverture sur Android 14",
  "platform": "ANDROID",
  "appVersion": "2.1.0",
  "osVersion": "Android 14",
  "deviceModel": "Google Pixel 8",
  "severity": "CRITICAL",
  "stackTrace": "java.lang.RuntimeException: Unable to start activity",
  "userEmail": "test@example.com",
  "stepsToReproduce": "1. Installer l'app\n2. Ouvrir l'app\n3. Crash immédiat"
}
```

### Rapport de bug iOS

```json
{
  "title": "Interface figée",
  "description": "L'interface se fige lors du scroll sur iOS 17",
  "platform": "IOS",
  "appVersion": "2.1.0",
  "osVersion": "iOS 17.2",
  "deviceModel": "iPhone 15 Pro",
  "severity": "HIGH",
  "userEmail": "ios.user@example.com",
  "stepsToReproduce": "1. Ouvrir la liste\n2. Scroller rapidement\n3. Interface se fige"
}
```

### Rapport de bug Web

```json
{
  "title": "Bouton non cliquable",
  "description": "Le bouton de validation ne répond pas sur Safari",
  "platform": "WEB",
  "appVersion": "1.5.2",
  "osVersion": "macOS 14.2",
  "deviceModel": "Safari 17.2",
  "severity": "MEDIUM",
  "userEmail": "web.user@example.com",
  "stepsToReproduce": "1. Remplir le formulaire\n2. Cliquer sur Valider\n3. Aucune réaction"
}
```

## PgAdmin - Interface de gestion PostgreSQL

Pour visualiser et interroger la base de données via l'interface web :

1. Accéder à `http://localhost:5050`
2. Se connecter avec :
   - Email: `admin@smartlog.com`
   - Password: `admin123`
3. Ajouter un nouveau serveur :
   - Host: `postgres` (ou `localhost` si PgAdmin est en local)
   - Port: `5432`
   - Database: `smartlog`
   - Username: `smartlog`
   - Password: `smartlog123`

## Gestion des erreurs

L'API retourne des réponses d'erreur structurées :

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": ["Title is required", "Platform is required"],
  "timestamp": "2025-10-17T10:30:00"
}
```

Codes d'erreur :

- `400` : Validation échouée ou paramètre invalide
- `404` : Ressource non trouvée
- `500` : Erreur serveur interne

## Tests avec Postman/Insomnia

Importer la collection d'exemples ci-dessus ou utiliser les URLs suivantes :

- Base URL: `http://localhost:8080`
- Content-Type: `application/json`
- CORS: Activé pour tous les domaines

## Configuration

### Variables d'environnement Docker

Le fichier `docker-compose.yml` configure automatiquement :

```yaml
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/smartlog
SPRING_DATASOURCE_USERNAME=smartlog
SPRING_DATASOURCE_PASSWORD=smartlog123
```

### Configuration locale

Modifier `src/main/resources/application.properties` pour :

- Changer le port : `server.port=8080`
- Configurer une base de données persistante (PostgreSQL, MySQL)
- Ajuster les niveaux de logging

## Architecture Docker

### Services

1. **smartlog-app** : Application Spring Boot

   - Port: 8080
   - Dépend de PostgreSQL
   - Health check via Actuator

2. **postgres** : Base de données PostgreSQL 16

   - Port: 5432
   - Persistance via volume Docker
   - Health check intégré

3. **pgadmin** : Interface de gestion PostgreSQL (optionnel)
   - Port: 5050
   - Interface web pour gérer la base de données

### Volumes

- `postgres-data` : Stockage persistant des données PostgreSQL

### Réseau

- `smartlog-network` : Réseau bridge pour la communication entre services

## Monitoring et Health Check

L'application expose des endpoints Actuator :

```bash
# Vérifier la santé de l'application
curl http://localhost:8080/actuator/health

# Informations sur l'application
curl http://localhost:8080/actuator/info
```

## Prochaines étapes

- Ajouter l'authentification JWT
- Implémenter l'upload de fichiers (screenshots, logs)
- Ajouter des notifications par email
- Créer un dashboard de visualisation
- Ajouter des filtres avancés
- Implémenter la pagination
- Configurer un reverse proxy (Nginx)
- Ajouter des métriques Prometheus
- Implémenter CI/CD avec GitHub Actions
