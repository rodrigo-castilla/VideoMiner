package aiss.peertubeminer.controller;

import aiss.peertubeminer.service.PeerTubeService;
import aiss.peertubeminer.service.VideoMinerService;
import aiss.videominer.model.Channel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/peertube")
@CrossOrigin(origins = "*")
public class PeerTubeController {

    private final PeerTubeService peerTubeService;
    private final VideoMinerService videoMinerService;

    //Inyección de dependencias de Spring Boot
    public PeerTubeController(PeerTubeService peerTubeService, VideoMinerService videoMinerService) {
        this.peerTubeService = peerTubeService;
        this.videoMinerService = videoMinerService;
    }

    //GET para pruebas (solo lee de PeerTube y lo muestra, no lo envía)
    @GetMapping("/{id}")
    public Channel getChannelTest(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxComments) {
        return peerTubeService.getChannel(id, maxVideos, maxComments);
    }

    //POST oficial (Leer de PeerTube y lo envía a VideoMiner)
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel createChannel(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxComments) {

        //1 - Extraer datos
        Channel channel = peerTubeService.getChannel(id, maxVideos, maxComments);
        //2 - Enviar a VideoMiner
        videoMinerService.sendChannelToVideoMiner(channel);
        //3 - Devolver canal creado
        return channel;
    }
}