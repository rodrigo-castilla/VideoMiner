package aiss.peertubeminer.service;

import aiss.peertubeminer.model.videominer.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class VideoMinerService {

    private final RestTemplate restTemplate;

    @Value("${videominer.url:http://localhost:8080}")
    private String videoMinerUrl;

    public VideoMinerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendChannelToVideoMiner(Channel channel) {
        // La ruta del controlador en tu VideoMiner es /api/channels
        String url = videoMinerUrl + "/api/channels";

        try {
            restTemplate.postForEntity(url, channel, Channel.class);
        } catch (Exception e) {
            System.err.println("Error enviando a VideoMiner: " + e.getMessage());
        }
    }
}