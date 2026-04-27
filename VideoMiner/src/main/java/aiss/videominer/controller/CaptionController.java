package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.repository.CaptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/captions")
public class CaptionController {

    @Autowired
    private CaptionRepository repository;

    // "listar todas las captions"
    @GetMapping
    public List<Caption> findAll() {
        return repository.findAll();
    }

    // "buscarlas por id"
    @GetMapping("/{id}")
    public Caption findOne(@PathVariable String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Caption no encontrada"));
    }
}