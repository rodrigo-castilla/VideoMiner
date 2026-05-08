package aiss.videominer.controller;

import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
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
@RequestMapping("/videominer/channels")
@Tag(name = "Channel", description = "API para la gestión y consulta de Canales")
public class ChannelController {

    @Autowired
    private ChannelRepository repository;

    @Operation(summary = "Listar todos los canales", description = "Devuelve una lista de todos los canales almacenados en la base de datos.")
    @GetMapping
    public List<Channel> findAll() {
        return repository.findAll();
    }

    @Operation(summary = "Obtener un canal por ID", description = "Busca y devuelve un canal específico según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canal encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Channel.class)) }),
            @ApiResponse(responseCode = "404", description = "Canal no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public Channel findOne(@Parameter(description = "ID del canal a buscar") @PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found"));
    }

    @Operation(summary = "Crear un nuevo canal", description = "Guarda un nuevo canal (y sus vídeos, comentarios, etc. anidados) en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Canal creado exitosamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Channel.class)) }),
            @ApiResponse(responseCode = "400", description = "Petición mal formada (datos inválidos)", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Channel create(@RequestBody Channel channel) {
        return repository.save(channel);
    }
}
