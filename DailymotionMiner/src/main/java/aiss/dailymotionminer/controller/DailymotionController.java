package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.videominer.Channel;
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

    // OPERACIÓN POST (Obligatoria): Busca el canal en Dailymotion y lo envía a VideoMiner
    @PostMapping("/channels/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel createChannel(@PathVariable String id,
                                 @RequestParam(defaultValue = "10") int maxVideos,
                                 @RequestParam(defaultValue = "2") int maxPages) {

        Channel channel = dailymotionService.getChannel(id, maxVideos, maxPages);
        videoMinerService.sendChannelToVideoMiner(channel);
        return channel;
    }

    // OPERACIÓN GET (Recomendada para pruebas): Busca en Dailymotion pero NO envía a VideoMiner
    @GetMapping("/channels/{id}")
    public Channel getChannel(@PathVariable String id,
                              @RequestParam(defaultValue = "10") int maxVideos,
                              @RequestParam(defaultValue = "2") int maxPages) {

        return dailymotionService.getChannel(id, maxVideos, maxPages);
    }
}