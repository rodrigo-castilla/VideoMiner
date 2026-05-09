# ETAPA 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# TRUCO DE DEVOPS: Creamos un archivo settings.xml al vuelo para obligar a Maven 
# a descargar desde el espejo de Google y así esquivar el Error 403 de Maven Central
RUN mkdir -p /root/.m2 && \
    echo '<settings><mirrors><mirror><id>google-maven-central</id><name>Google Maven Central</name><url>https://maven-central.storage-download.googleapis.com/maven2/</url><mirrorOf>central</mirrorOf></mirror></mirrors></settings>' > /root/.m2/settings.xml

# Compilamos usando la nueva configuración
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución (Run)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
