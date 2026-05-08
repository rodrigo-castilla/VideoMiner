package aiss.peertubeminer.service;

import aiss.peertubeminer.model.videominer.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VideoMinerService {

    private final RestTemplate restTemplate;

    @Value("${videominer.url:http://localhost:8080}")
    private String videoMinerUrl;

    public VideoMinerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendChannelToVideoMiner(Channel channel) {
        // ¡Cambiado a la ruta correcta de VideoMiner!
        String url = videoMinerUrl + "/videominer/channels";

        try {
            restTemplate.postForEntity(url, channel, Channel.class);
            System.out.println("¡Canal enviado a VideoMiner correctamente!");
        } catch (Exception e) {
            System.err.println("Error enviando a VideoMiner: " + e.getMessage());
        }
    }
}