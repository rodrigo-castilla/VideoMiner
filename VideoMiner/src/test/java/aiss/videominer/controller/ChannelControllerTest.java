package aiss.videominer.controller;

import aiss.videominer.model.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Esta etiqueta levanta tu aplicación Spring Boot en un puerto aleatorio para hacer la prueba
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChannelControllerTest {

    // TestRestTemplate es nuestro "Postman integrado". Nos permite hacer peticiones
    // GET, POST, etc.
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void listChannels() {

        // 1. PREPARAR (Arrange)
        // En este caso tan sencillo, la preparación es simplemente saber a qué URL
        // vamos a llamar.
        String url = "/videominer/channels";

        // 2. ACTUAR (Act)
        // Hacemos una petición GET a la URL. Le decimos que esperamos un Array de la
        // clase Channel.
        ResponseEntity<Channel[]> response = restTemplate.getForEntity(url, Channel[].class);

        // 3. COMPROBAR (Assert)
        // Comprobación A: El servidor nos debe devolver un código 200 OK (todo ha ido
        // bien)
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El endpoint debería devolver un código 200 OK");

        // Comprobación B: El cuerpo de la respuesta (la lista de canales) no debe ser
        // nulo.
        assertNotNull(response.getBody(), "La lista de canales devuelta no debería ser nula");
    }
}
