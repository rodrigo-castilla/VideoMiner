package aiss.peertubeminer.controller;

import aiss.peertubeminer.service.PeerTubeService;
import aiss.peertubeminer.service.VideoMinerService;
import aiss.peertubeminer.model.videominer.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/peertube")
public class PeerTubeController {

    @Autowired
    private PeerTubeService peerTubeService;

    @Autowired
    private VideoMinerService videoMinerService;

    // Operación POST: Mina el canal y lo ENVÍA a VideoMiner
    @PostMapping("/channels/{channelId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel mineChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "10") int maxComments) {

        Channel channel = peerTubeService.getChannel(channelId, maxVideos, maxComments);
        videoMinerService.sendChannelToVideoMiner(channel);
        return channel;
    }

    // NUEVA Operación GET: Solo lectura para pruebas (NO envía a VideoMiner)
    @GetMapping("/channels/{channelId}")
    public Channel checkChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "10") int maxComments) {

        // Solo recuperamos los datos de PeerTube y los devolvemos al usuario
        return peerTubeService.getChannel(channelId, maxVideos, maxComments);
    }
}