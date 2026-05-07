package aiss.peertubeminer.service;

import aiss.peertubeminer.model.PTChannelDTO;
import aiss.peertubeminer.model.videominer.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PeerTubeService {

    private final RestTemplate restTemplate;

    @Value("${peertube.api.url:https://peertube.tv}")
    private String peertubeApiUrl;

    public PeerTubeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Channel getChannel(String channelId, int maxVideos, int maxComments) {
        try {
            String channelUrl = peertubeApiUrl + "/api/v1/video-channels/" + channelId;
            PTChannelDTO ptChannel = restTemplate.getForObject(channelUrl, PTChannelDTO.class);

            String videosUrl = peertubeApiUrl + "/api/v1/video-channels/" + channelId + "/videos?count=" + maxVideos;
            Map<String, Object> response = restTemplate.getForObject(videosUrl, Map.class);
            List<Map<String, Object>> ptVideosRaw = (List<Map<String, Object>>) response.get("data");

            Channel channel = new Channel();
            channel.setId(ptChannel.getId());
            channel.setName(ptChannel.getName());
            channel.setDescription(ptChannel.getDescription());

            List<Video> videos = new ArrayList<>();
            if (ptVideosRaw != null) {
                for (Map<String, Object> vRaw : ptVideosRaw) {
                    Video v = new Video();
                    v.setId(vRaw.get("uuid").toString());
                    v.setName(vRaw.get("name").toString());
                    v.setReleaseTime(vRaw.get("publishedAt").toString());
                    videos.add(v);
                }
            }
            channel.setVideos(videos);
            return channel;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error buscando el canal: " + e.getMessage());
        }
    }
}