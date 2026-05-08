package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VideoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private VideoRepository repository;

    // --- HELPER PARA CREAR VIDEOS DE PRUEBA ---
    private Video crearVideoFalso(String id) {
        Video mokVideo = new Video();
        mokVideo.setId(id);
        // OBLIGATORIO: name y releaseTime no pueden estar vacíos por la anotación
        // @NotEmpty
        mokVideo.setName("Instalación de Arch Linux paso a paso");
        mokVideo.setReleaseTime("2026-05-08T10:00:00Z");
        mokVideo.setDescription("Guía definitiva de instalación desde la terminal");

        // Preparamos un subtítulo falso anidado para probar la ruta /{id}/captions
        Caption mokCaption = new Caption();
        mokCaption.setId("cap-" + id);
        mokCaption.setLanguage("es");
        mokCaption.setLink("http://subtitulos.test/es");

        // Preparamos un comentario falso anidado para probar la ruta /{id}/comments
        Comment mokComment = new Comment();
        mokComment.setId("com-" + id);
        mokComment.setText("¡Excelente tutorial, me salvó la vida!");
        mokComment.setCreatedOn("2026-05-08T10:05:00Z");

        // Usamos los setters personalizados que ya programaste en tu clase Video (muy
        // bien hechos, por cierto)
        mokVideo.setCaptions(List.of(mokCaption));
        mokVideo.setComments(List.of(mokComment));

        return mokVideo;
    }

    @Test
    public void getAllVideos() {
        String url = "/videominer/videos";
        ResponseEntity<Video[]> response = restTemplate.getForEntity(url, Video[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "El endpoint debería devolver un código 200 OK");
        assertNotNull(response.getBody(), "La lista de videos devuelta no debería ser nula");
    }

    @Test
    public void getVideoIfExists() {
        Video mokVideo = crearVideoFalso("vid-123");
        repository.save(mokVideo);

        String url = "/videominer/videos/{id}";
        ResponseEntity<Video> response = restTemplate.getForEntity(url, Video.class, mokVideo.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Debería devolver 200 OK porque el video existe");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debe ser nulo");
        assertEquals(mokVideo.getId(), response.getBody().getId(), "El ID del video devuelto no coincide");
    }

    @Test
    public void notFoundVideoIfNotExists() {
        String idFalso = "video-falso-999";
        String url = "/videominer/videos/{id}";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, idFalso);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Debería devolver 404 si el video no existe");
    }

    @Test
    public void getCaptionsByVideoId() {
        Video mokVideo = crearVideoFalso("vid-cap-123");
        repository.save(mokVideo);

        String url = "/videominer/videos/{id}/captions";
        ResponseEntity<Caption[]> response = restTemplate.getForEntity(url, Caption[].class, mokVideo.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Debería devolver 200 OK al pedir las captions");
        assertNotNull(response.getBody(), "El array de captions no debe ser nulo");
        assertTrue(response.getBody().length > 0, "Debería devolver al menos una caption");
    }

    @Test
    public void getCommentsByVideoId() {
        Video mokVideo = crearVideoFalso("vid-com-123");
        repository.save(mokVideo);

        String url = "/videominer/videos/{id}/comments";
        ResponseEntity<Comment[]> response = restTemplate.getForEntity(url, Comment[].class, mokVideo.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Debería devolver 200 OK al pedir los comentarios");
        assertNotNull(response.getBody(), "El array de comentarios no debe ser nulo");
        assertTrue(response.getBody().length > 0, "Debería devolver al menos un comentario");
    }
}
