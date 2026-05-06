package aiss.dailymotionminer.service;

import aiss.videominer.model.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.http.ResponseEntity;

/**
 * Envía datos de Dailymotion a VideoMiner.
 */
@Service
public class VideoMinerService {

    private final RestTemplate restTemplate;

    @Value("${videominer.url:http://localhost:8080}")
    private String videoMinerUrl;

    public VideoMinerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Igual que tu compañero pero con tu modelo
    public ResponseEntity<String> sendChannelToVideoMiner(Channel channel) {

        String url = videoMinerUrl + "/videominer/channels";

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, channel, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response;
            } else {
                throw new RuntimeException(
                        "VideoMiner returned error: " + response.getStatusCode()
                );
            }

        } catch (RestClientException e) {
            throw new RuntimeException(
                    "Error sending channel to VideoMiner at " + url + ": " + e.getMessage(),
                    e
            );
        }
    }
}