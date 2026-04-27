package aiss.videominer.controller;

import aiss.videominer.model.Comment;
import aiss.videominer.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentRepository repository;

    // "listar todos los comentarios"
    @GetMapping
    public List<Comment> findAll() {
        return repository.findAll();
    }

    // "buscarlos por id"
    @GetMapping("/{id}")
    public Comment findOne(@PathVariable String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario no encontrado"));
    }
}