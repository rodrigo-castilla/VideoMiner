package aiss.videominer.controller;

import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    @Autowired
    private ChannelRepository repository;

    @GetMapping
    public List<Channel> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Channel findOne(@PathVariable String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Channel create(@RequestBody Channel channel) {
        return repository.save(channel);
    }
}