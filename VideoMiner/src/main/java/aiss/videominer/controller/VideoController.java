package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoRepository repository;

    @GetMapping
    public List<Video> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Video findOne(@PathVariable String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));
    }

    @GetMapping("/{id}/captions")
    public List<Caption> findCaptionsByVideoId(@PathVariable String id) {
        Video video = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));
        return video.getCaptions();
    }

    @GetMapping("/{id}/comments")
    public List<Comment> findCommentsByVideoId(@PathVariable String id) {
        Video video = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));
        return video.getComments();
    }
}