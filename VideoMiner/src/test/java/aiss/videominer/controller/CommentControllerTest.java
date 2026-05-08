package aiss.videominer.controller;

import aiss.videominer.model.Comment;
import aiss.videominer.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CommentRepository repository;

    // --- HELPER PARA CREAR COMENTARIOS DE PRUEBA ---
    private Comment crearCommentFalso(String id) {
        Comment mokComment = new Comment();
        mokComment.setId(id);
        mokComment.setText("Buen video, pero prefiero usar gestores de ventanas en vez de escritorios completos.");
        mokComment.setCreatedOn("2026-05-08T11:00:00Z");
        return mokComment;
    }

    @Test
    public void getAllComments() {
        String url = "/videominer/comments";
        ResponseEntity<Comment[]> response = restTemplate.getForEntity(url, Comment[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "El endpoint debería devolver un código 200 OK");
        assertNotNull(response.getBody(), "La lista de comentarios devuelta no debería ser nula");
    }

    @Test
    public void getCommentIfExists() {
        Comment mokComment = crearCommentFalso("com-456");
        repository.save(mokComment);

        String url = "/videominer/comments/{id}";
        ResponseEntity<Comment> response = restTemplate.getForEntity(url, Comment.class, mokComment.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Debería devolver 200 OK porque el comentario existe");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debe ser nulo");
        assertEquals(mokComment.getId(), response.getBody().getId(), "El ID del comentario devuelto no coincide");
    }

    @Test
    public void notFoundCommentIfNotExists() {
        String idFalso = "comentario-falso-999";
        String url = "/videominer/comments/{id}";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, idFalso);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Debería devolver 404 si el comentario no existe");
    }
}
