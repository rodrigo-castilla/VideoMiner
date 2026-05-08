package aiss.peertubeminer.controller;

import aiss.peertubeminer.service.PeerTubeService;
import aiss.peertubeminer.service.VideoMinerService;
import aiss.peertubeminer.model.videominer.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/peertube")
@Tag(name = "PeerTube Miner", description = "Adaptador para extraer datos de PeerTube")
public class PeerTubeController {

    @Autowired
    private PeerTubeService peerTubeService;

    @Autowired
    private VideoMinerService videoMinerService;

    @Operation(summary = "Minar y enviar canal", description = "Extrae los datos de un canal de PeerTube y los envía automáticamente a VideoMiner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Canal minado y enviado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Canal no encontrado en PeerTube")
    })
    @PostMapping("/channels/{channelId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel mineChannel(
            @Parameter(description = "ID del canal en PeerTube") @PathVariable String channelId,
            @Parameter(description = "Número máximo de vídeos a extraer") @RequestParam(defaultValue = "10") int maxVideos,
            @Parameter(description = "Número máximo de comentarios por vídeo a extraer") @RequestParam(defaultValue = "10") int maxComments) {

        Channel channel = peerTubeService.getChannel(channelId, maxVideos, maxComments);
        videoMinerService.sendChannelToVideoMiner(channel);
        return channel;
    }

    @Operation(summary = "Comprobar canal (Modo lectura)", description = "Extrae los datos de un canal de PeerTube pero NO los envía a VideoMiner. Útil para pruebas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos del canal devueltos exitosamente"),
            @ApiResponse(responseCode = "404", description = "Canal no encontrado en PeerTube")
    })
    @GetMapping("/channels/{channelId}")
    public Channel checkChannel(
            @Parameter(description = "ID del canal en PeerTube") @PathVariable String channelId,
            @Parameter(description = "Número máximo de vídeos a extraer") @RequestParam(defaultValue = "10") int maxVideos,
            @Parameter(description = "Número máximo de comentarios por vídeo a extraer") @RequestParam(defaultValue = "10") int maxComments) {

        return peerTubeService.getChannel(channelId, maxVideos, maxComments);
    }
}
