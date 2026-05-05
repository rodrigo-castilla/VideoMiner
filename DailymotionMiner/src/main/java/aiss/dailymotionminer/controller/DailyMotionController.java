package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.service.DailymotionService;
import aiss.dailymotionminer.service.VideoMinerService;
import aiss.dailymotionminer.model.Channel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dailymotion")
@CrossOrigin(origins = "*")
public class DailymotionController {

    private final DailymotionService dailymotionService;
    private final VideoMinerService videoMinerService;

    // Inyección por constructor
    public DailymotionController(DailymotionService dailymotionService,
                                 VideoMinerService videoMinerService) {
        this.dailymotionService = dailymotionService;
        this.videoMinerService = videoMinerService;
    }

    // GET para pruebas (no envía a VideoMiner)
    @GetMapping("/{id}")
    public Channel getChannelTest(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxPages) {

        return dailymotionService.getChannel(id, maxVideos, maxPages);
    }

    // POST oficial
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel createChannel(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxPages) {

        // 1 - extraer datos de Dailymotion
        Channel channel = dailymotionService.getChannel(id, maxVideos, maxPages);

        // 2 - enviar a VideoMiner
        videoMinerService.sendChannelToVideoMiner(channel);

        // 3 - devolver respuesta
        return channel;
    }
}