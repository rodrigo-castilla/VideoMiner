package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.VideoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/videominer/videos")
@Tag(name = "Video", description = "API para la consulta de Vídeos y sus elementos asociados")
public class VideoController {

    @Autowired
    private VideoRepository repository;

    @Operation(summary = "Listar todos los vídeos", description = "Devuelve una lista de todos los vídeos almacenados.")
    @GetMapping
    public List<Video> findAll() {
        return repository.findAll();
    }

    @Operation(summary = "Obtener un vídeo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vídeo encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Video.class)) }),
            @ApiResponse(responseCode = "404", description = "Vídeo no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public Video findOne(@Parameter(description = "ID del vídeo") @PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));
    }

    @Operation(summary = "Obtener los subtítulos de un vídeo", description = "Devuelve la lista de subtítulos (captions) asociados a un vídeo concreto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vídeo encontrado y subtítulos devueltos"),
            @ApiResponse(responseCode = "404", description = "Vídeo no encontrado", content = @Content)
    })
    @GetMapping("/{id}/captions")
    public List<Caption> findCaptionsByVideoId(@Parameter(description = "ID del vídeo") @PathVariable String id) {
        Video video = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));
        return video.getCaptions();
    }

    @Operation(summary = "Obtener los comentarios de un vídeo", description = "Devuelve la lista de comentarios asociados a un vídeo concreto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vídeo encontrado y comentarios devueltos"),
            @ApiResponse(responseCode = "404", description = "Vídeo no encontrado", content = @Content)
    })
    @GetMapping("/{id}/comments")
    public List<Comment> findCommentsByVideoId(@Parameter(description = "ID del vídeo") @PathVariable String id) {
        Video video = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));
        return video.getComments();
    }
}
