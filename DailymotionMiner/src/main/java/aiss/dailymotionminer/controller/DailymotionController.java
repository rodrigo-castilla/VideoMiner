package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.videominer.Channel;
import aiss.dailymotionminer.service.DailymotionService;
import aiss.dailymotionminer.service.VideoMinerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dailymotion")
@CrossOrigin(origins = "*")
@Tag(name = "Dailymotion Miner", description = "Adaptador para extraer datos de Dailymotion")
public class DailymotionController {

    private final DailymotionService dailymotionService;
    private final VideoMinerService videoMinerService;

    public DailymotionController(DailymotionService dailymotionService,
            VideoMinerService videoMinerService) {
        this.dailymotionService = dailymotionService;
        this.videoMinerService = videoMinerService;
    }

    @Operation(summary = "Minar y enviar canal", description = "Extrae los datos de un canal de Dailymotion y los envía automáticamente a VideoMiner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Canal minado y enviado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Canal no encontrado en Dailymotion")
    })
    @PostMapping("/channels/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel createChannel(
            @Parameter(description = "ID del canal en Dailymotion") @PathVariable String id,
            @Parameter(description = "Número máximo de vídeos a extraer por página") @RequestParam(defaultValue = "10") int maxVideos,
            @Parameter(description = "Número máximo de páginas a consultar") @RequestParam(defaultValue = "2") int maxPages) {

        Channel channel = dailymotionService.getChannel(id, maxVideos, maxPages);
        videoMinerService.sendChannelToVideoMiner(channel);
        return channel;
    }

    @Operation(summary = "Comprobar canal (Modo lectura)", description = "Extrae los datos de un canal de Dailymotion pero NO los envía a VideoMiner. Útil para pruebas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos del canal devueltos exitosamente"),
            @ApiResponse(responseCode = "404", description = "Canal no encontrado en Dailymotion")
    })
    @GetMapping("/channels/{id}")
    public Channel getChannel(
            @Parameter(description = "ID del canal en Dailymotion") @PathVariable String id,
            @Parameter(description = "Número máximo de vídeos a extraer por página") @RequestParam(defaultValue = "10") int maxVideos,
            @Parameter(description = "Número máximo de páginas a consultar") @RequestParam(defaultValue = "2") int maxPages) {

        return dailymotionService.getChannel(id, maxVideos, maxPages);
    }
}
