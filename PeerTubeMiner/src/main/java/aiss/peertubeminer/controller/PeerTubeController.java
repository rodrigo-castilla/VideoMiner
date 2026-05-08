package aiss.peertubeminer.controller;

import aiss.peertubeminer.service.PeerTubeService;
import aiss.peertubeminer.service.VideoMinerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/peertube")
public class PeerTubeController {

    @Autowired
    private PeerTubeService peerTubeService;

    @Autowired
    private VideoMinerService videoMinerService;

    @PostMapping("/channels/{channelId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel mineChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "10") int maxComments) {

        // 1. Descarga los datos de PeerTube
        Channel channel = peerTubeService.getChannel(channelId, maxVideos, maxComments);

        // 2. Envía los datos al VideoMiner (puerto 8080)
        videoMinerService.sendChannelToVideoMiner(channel);


        return channel;
    }
}