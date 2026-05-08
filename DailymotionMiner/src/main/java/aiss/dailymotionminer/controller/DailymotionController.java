package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.videominer.*;
import aiss.dailymotionminer.service.DailymotionService;
import aiss.dailymotionminer.service.VideoMinerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dailymotion")
@CrossOrigin(origins = "*")
public class DailymotionController {

    private final DailymotionService dailymotionService;
    private final VideoMinerService videoMinerService;

    public DailymotionController(DailymotionService dailymotionService,
                                 VideoMinerService videoMinerService) {
        this.dailymotionService = dailymotionService;
        this.videoMinerService = videoMinerService;
    }

    @GetMapping("/status")
    public String status() {
        return "DailymotionMiner is running";
    }

    @PostMapping("/channels")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel createChannel(@RequestBody Channel channel) {
        videoMinerService.sendChannelToVideoMiner(channel);
        return channel;
    }

    @PostMapping("/videos")
    @ResponseStatus(HttpStatus.CREATED)
    public Video createVideo(@RequestBody Video video) {
        return video;
    }

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@RequestBody Comment comment) {
        return comment;
    }

    @PostMapping("/captions")
    @ResponseStatus(HttpStatus.CREATED)
    public Caption createCaption(@RequestBody Caption caption) {
        return caption;
    }
}