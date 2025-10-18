# SmartLog - API de Rapports de Bug

API REST pour recevoir et gérer des rapports de bug depuis des applications mobiles (Android, iOS) et Web.

## Technologies

- Java 21
- Spring Boot 3.5.6
- Spring Data JPA
- H2 Database (en mémoire) / PostgreSQL (production)
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

# Lancer le conteneur avec H2
docker run -p 8080:8080 smartlog-api
```

### Option 2 : En local (développement)

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

### Créer un rapport de bug

**POST** `/api/bug-reports`

```bash
curl -X POST http://localhost:8080/api/bug-reports \
  -H "Content-Type: application/json" \
  -d '{
    "title": "App crashes on startup",
    "description": "The app crashes immediately after opening",
    "platform": "ANDROID",
    "appVersion": "1.0.5",
    "osVersion": "Android 14",
    "deviceModel": "Samsung Galaxy S23",
    "severity": "CRITICAL",
    "stackTrace": "java.lang.NullPointerException at MainActivity.onCreate()",
    "userEmail": "user@example.com",
    "stepsToReproduce": "1. Open app\n2. Crash occurs"
  }'
```

### Récupérer tous les rapports

**GET** `/api/bug-reports`

```bash
curl http://localhost:8080/api/bug-reports
```

### Récupérer un rapport spécifique

**GET** `/api/bug-reports/{id}`

```bash
curl http://localhost:8080/api/bug-reports/1
```

### Filtrer par plateforme

**GET** `/api/bug-reports/platform/{platform}`

Plateformes disponibles: `ANDROID`, `IOS`, `WEB`

```bash
# Rapports Android
curl http://localhost:8080/api/bug-reports/platform/ANDROID

# Rapports iOS
curl http://localhost:8080/api/bug-reports/platform/IOS

# Rapports Web
curl http://localhost:8080/api/bug-reports/platform/WEB
```

### Filtrer par statut

**GET** `/api/bug-reports/status/{status}`

Statuts disponibles: `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`

```bash
curl http://localhost:8080/api/bug-reports/status/OPEN
```

### Mettre à jour le statut d'un rapport

**PATCH** `/api/bug-reports/{id}/status`

```bash
curl -X PATCH "http://localhost:8080/api/bug-reports/1/status?status=IN_PROGRESS"
```

### Supprimer un rapport

**DELETE** `/api/bug-reports/{id}`

```bash
curl -X DELETE http://localhost:8080/api/bug-reports/1
```

## Modèle de données

### BugReportRequest (JSON)

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| title | string | Oui | Titre du bug |
| description | string | Oui | Description détaillée |
| platform | enum | Oui | ANDROID, IOS, ou WEB |
| appVersion | string | Oui | Version de l'application |
| osVersion | string | Oui | Version du système d'exploitation |
| deviceModel | string | Oui | Modèle de l'appareil |
| severity | enum | Non | LOW, MEDIUM, HIGH, CRITICAL (défaut: MEDIUM) |
| stackTrace | string | Non | Trace de la pile d'exécution |
| userEmail | string | Non | Email de l'utilisateur |
| stepsToReproduce | string | Non | Étapes pour reproduire le bug |

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

## Console H2

Pour visualiser et interroger la base de données :

1. Accéder à `http://localhost:8080/h2-console`
2. Configuration :
   - JDBC URL: `jdbc:h2:mem:smartlog`
   - Username: `sa`
   - Password: (laisser vide)
3. Cliquer sur "Connect"

## Gestion des erreurs

L'API retourne des réponses d'erreur structurées :

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": [
    "Title is required",
    "Platform is required"
  ],
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
