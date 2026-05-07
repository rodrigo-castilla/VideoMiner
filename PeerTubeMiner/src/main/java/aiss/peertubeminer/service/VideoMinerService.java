package aiss.peertubeminer.service;

import aiss.peertubeminer.model.videominer.Channel; // Importante: debe usar el modelo estandarizado
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.http.ResponseEntity;

@Service
public class VideoMinerService {

    private final RestTemplate restTemplate;

    // Apunta al puerto 8080 donde está tu VideoMiner
    @Value("${videominer.url:http://localhost:8080}")
    private String videoMinerUrl;

    public VideoMinerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Envía el Channel ya formateado a la aplicación de VideoMiner
    public ResponseEntity<Channel> sendChannelToVideoMiner(Channel channel) {
        String url = videoMinerUrl + "/videominer/channels"; // Tu ruta exacta

        try {
            // Corrección: ResponseEntity en lugar de EntidadREspuesta
            ResponseEntity<Channel> respuesta = restTemplate.postForEntity(url, channel, Channel.class);

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