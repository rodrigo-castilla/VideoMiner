package aiss.videominer.controller;

import aiss.videominer.model.Comment;
import aiss.videominer.repository.CommentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/videominer/comments")
@Tag(name = "Comment", description = "API para la consulta de Comentarios individuales")
public class CommentController {

    @Autowired
    private CommentRepository repository;

    @Operation(summary = "Listar todos los comentarios")
    @GetMapping
    public List<Comment> findAll() {
        return repository.findAll();
    }

    @Operation(summary = "Obtener un comentario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentario encontrado"),
            @ApiResponse(responseCode = "404", description = "Comentario no encontrado")
    })
    @GetMapping("/{id}")
    public Comment findOne(@Parameter(description = "ID del comentario") @PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario no encontrado"));
    }
}
