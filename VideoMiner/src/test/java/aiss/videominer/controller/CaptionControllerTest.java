package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.repository.CaptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CaptionControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CaptionRepository repository;

    // --- HELPER PARA CREAR CAPTIONS DE PRUEBA ---
    private Caption crearCaptionFalsa(String id) {
        Caption mokCaption = new Caption();
        mokCaption.setId(id);
        mokCaption.setLanguage("en");
        mokCaption.setName("http://subtitulos.test/en"); // <--- CAMBIADO DE setLink A setName
        return mokCaption;
    }

    @Test
    public void getAllCaptions() {
        String url = "/videominer/captions";
        ResponseEntity<Caption[]> response = restTemplate.getForEntity(url, Caption[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "El endpoint debería devolver un código 200 OK");
        assertNotNull(response.getBody(), "La lista de captions devuelta no debería ser nula");
    }

    @Test
    public void getCaptionIfExists() {
        Caption mokCaption = crearCaptionFalsa("cap-789");
        repository.save(mokCaption);

        String url = "/videominer/captions/{id}";
        ResponseEntity<Caption> response = restTemplate.getForEntity(url, Caption.class, mokCaption.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Debería devolver 200 OK porque la caption existe");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debe ser nulo");
        assertEquals(mokCaption.getId(), response.getBody().getId(), "El ID de la caption devuelta no coincide");
    }

    @Test
    public void notFoundCaptionIfNotExists() {
        String idFalso = "caption-falsa-999";
        String url = "/videominer/captions/{id}";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, idFalso);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Debería devolver 404 si la caption no existe");
    }
}
