package aiss.videominer.controller;

import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChannelControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ChannelRepository repository;

    private Channel crearCanalFalso(String id) {
        Channel mokChannel = new Channel();
        mokChannel.setId(id); // Le pasamos el ID por parámetro para poder variar
        mokChannel.setName("mokChannel");
        mokChannel.setDescription("mok description for test");
        mokChannel.setCreatedTime("2026-05-08T10:00:00Z");
        return mokChannel;
    }

    @Test
    public void getAllChannels() {
        String url = "/videominer/channels";
        ResponseEntity<Channel[]> response = restTemplate.getForEntity(url, Channel[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "El endpoint debería devolver un código 200 OK");
        assertNotNull(response.getBody(), "La lista de canales devuelta no debería ser nula");
    }

    @Test
    public void getChannel() {
        // Create and save Channel
        Channel mokChannel = crearCanalFalso("mok-123");
        repository.save(mokChannel);

        String url = "/videominer/channels/{id}";

        ResponseEntity<Channel> response = restTemplate.getForEntity(url, Channel.class, mokChannel.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Debería devolver 200 OK porque el canal existe");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debe ser nulo");
        assertEquals(mokChannel.getId(), response.getBody().getId(),
                "El ID del canal devuelto no coincide con el solicitado");
    }

    @Test
    public void notFoundChannel() {
        String idFalso = "id-que-no-existe-12345";
        String url = "/videominer/channels/{id}";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, idFalso);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Debería devolver 404 si el canal no existe");
    }

    @Test
    public void createChannel() {
        Channel nuevoCanal = crearCanalFalso("nuevo-canal-456");
        String url = "/videominer/channels";

        ResponseEntity<Channel> response = restTemplate.postForEntity(url, nuevoCanal, Channel.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Debería devolver 201 CREATED al crear el canal");
        assertNotNull(response.getBody(), "El canal devuelto no debería ser nulo");
        assertEquals(nuevoCanal.getId(), response.getBody().getId(),
                "El ID devuelto debe coincidir con el que enviamos");
    }
}
