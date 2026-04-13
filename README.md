# VideoMiner
---
## File Structure
```txt
Proyecto_Integracion_Final/
├── docker-compose.yml                 # Archivo para desplegar todo a la vez (Criterio avanzado)
├── pruebas-postman/                   # Carpeta para guardar los tests del sistema
│   └── VideoMiner_Postman_Tests.json  
│
├── VideoMiner/                        # (TU REPOSITORIO BASE AMPLIADO)
│   ├── pom.xml
│   └── src/
│       ├── main/java/aiss/videominer/
│       │   ├── VideominerApplication.java
│       │   ├── model/                 # (YA EXISTE) Channel, Video, Caption, Comment, User
│       │   ├── controller/            # (NUEVO) Aquí van los @RestController
│       │   │   ├── ChannelController.java
│       │   │   ├── VideoController.java
│       │   │   └── ...
│       │   ├── repository/            # (NUEVO) Interfaces para guardar en la BD H2
│       │   │   ├── ChannelRepository.java
│       │   │   ├── VideoRepository.java
│       │   │   └── ...
│       │   └── exception/             # (NUEVO) Para devolver errores 404 limpios
│       │       └── ResourceNotFoundException.java
│       │
│       └── test/java/aiss/videominer/ # (NUEVO) Tests unitarios con JUnit
│           └── controller/
│               └── VideoControllerTest.java
│
├── PeerTubeMiner/                     # (NUEVO MICROSERVICIO)
│   ├── pom.xml
│   └── src/
│       ├── main/java/aiss/peertubeminer/
│       │   ├── PeertubeminerApplication.java
│       │   ├── controller/            # (NUEVO) Recibe las peticiones POST de los usuarios
│       │   │   └── PeerTubeController.java
│       │   ├── model/                 # (NUEVO) Clases para traducir el JSON de PeerTube
│       │   │   └── peertube/          # Modelos exactos de la API de PeerTube
│       │   │   └── videominer/        # Modelos idénticos a los de VideoMiner para enviar
│       │   └── service/               # (NUEVO) Lógica de conexión externa
│       │       └── PeerTubeService.java # Hace el GET a PeerTube y el POST a VideoMiner
│       └── test/java/aiss/peertubeminer/
│
└── DailymotionMiner/                  # (NUEVO MICROSERVICIO)
    ├── pom.xml
    └── src/
        ├── main/java/aiss/dailymotionminer/
        │   ├── DailymotionminerApplication.java
        │   ├── controller/            # (NUEVO) Recibe las peticiones POST
        │   │   └── DailymotionController.java
        │   ├── model/                 # (NUEVO) Clases para traducir el JSON de Dailymotion
        │   │   └── dailymotion/       # Modelos exactos de la API de Dailymotion
        │   │   └── videominer/        # Modelos idénticos a los de VideoMiner para enviar
        │   └── service/               # (NUEVO) Lógica de conexión externa
        │       └── DailymotionService.java # Hace el GET, adapta los Tags a Comments, y envía
        └── test/java/aiss/dailymotionminer/
```