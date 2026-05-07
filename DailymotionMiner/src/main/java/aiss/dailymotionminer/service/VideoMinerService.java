package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.videominer.Channel;
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

    // REAL: enviar channel
    public Channel sendChannelToVideoMiner(Channel channel) {
        String url = videoMinerUrl + "/videominer/channels";
        restTemplate.postForEntity(url, channel, String.class);
        return channel;
    }
}