package aiss.peertubeminer.service;

import aiss.peertubeminer.model.PTChannelDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

/**
 * Envia datos de channel de PeerTube a la aplicación VideoMiner.
 * - Recibe el objeto Channel mapeado
 * - Lo envia a VideoMiner con POST
 * - Devuelve la respuesta o lanza error si falla
 */
@Service
public class VideoMinerService {

    private final RestTemplate restTemplate;

    @Value("${videominer.url:http://localhost:8080}")
    private String videoMinerUrl;

    public VideoMinerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //Envía un Channel de PeerTube a la aplicación de VideoMiner
    public ResponseEntity<String> sendChannelToVideoMiner(PTChannelDTO channel) {
        String url = videoMinerUrl + "/videominer/channels";
        
        try {
            EntidadREspuesta<String> respuesta = restTemplate.postForEntity(url, channel, String.class);
            
            if (respuesta.getStatusCode().is2xxSuccessful()) {
                return respuesta;
            } else {
                throw new RuntimeException("VideoMiner returned error status: " + respuesta.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error sending channel to VideoMiner at " + url + ": " + e.getMessage(), e);
        }
    }
}
