package aiss.peertubeminer.controller;

import aiss.peertubeminer.service.PeerTubeService;
import aiss.peertubeminer.service.VideoMinerService;
import aiss.peertubeminer.model.videominer.Channel; // Ojo a este import, debe ser tu modelo
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

    // ¡ESTA ES LA LÍNEA CLAVE QUE FALTA!
    @PostMapping("/channels/{channelId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel mineChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "10") int maxComments) {

        // 1. Extraer datos de la API externa (PeerTube)
        Channel channel = peerTubeService.getChannel(channelId, maxVideos, maxComments);

        // 2. Enviar los datos al Almacén Central (VideoMiner)
        videoMinerService.sendChannelToVideoMiner(channel);

        // 3. Devolver el canal
        return channel;
    }
}