package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.DMChannelDTO;
import aiss.dailymotionminer.model.DMSubtitleDTO;
import aiss.dailymotionminer.model.DMVideoDTO;
import aiss.dailymotionminer.model.videominer.Caption;
import aiss.dailymotionminer.model.videominer.Channel;
import aiss.dailymotionminer.model.videominer.Comment;
import aiss.dailymotionminer.model.videominer.Video;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DailymotionService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${dailymotion.api.url:https://api.dailymotion.com}")
    private String apiUrl;

    public DailymotionService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Channel getChannel(String channelId, int maxVideos, int maxPages) {
        try {
            // 1. OBTENER CANAL
            String channelUrl = apiUrl + "/user/" + channelId + "?fields=id,screenname,description,created_time";
            DMChannelDTO dmChannel = restTemplate.getForObject(channelUrl, DMChannelDTO.class);

            if (dmChannel == null || dmChannel.getId() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Canal no encontrado");
            }

            Channel channel = mapToChannel(dmChannel);

            // 2. OBTENER VIDEOS
            List<Video> videoList = new ArrayList<>();

            for (int page = 1; page <= maxPages; page++) {
                String videosUrl = apiUrl + "/user/" + channelId + "/videos?fields=id,title,description,created_time,tags&limit=" + maxVideos + "&page=" + page;

                JsonNode response = restTemplate.getForObject(videosUrl, JsonNode.class);

                if (response != null && response.has("list")) {
                    JsonNode listNode = response.get("list");

                    DMVideoDTO[] videosArray = objectMapper.treeToValue(listNode, DMVideoDTO[].class);

                    for (DMVideoDTO dmVideo : videosArray) {
                        videoList.add(mapToVideo(dmVideo));
                    }
                }

                if (response != null && response.has("has_more") && !response.get("has_more").asBoolean()) {
                    break;
                }
            }

            channel.setVideos(videoList);
            videoList.forEach(v -> v.setChannel(channel));

            return channel;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Canal no encontrado en Dailymotion");
            }
            throw e;
        } catch (Exception e) {
            System.err.println("Error en DailymotionService: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al contactar con la API de Dailymotion");
        }
    }

    // ===== MAPEO CHANNEL =====
    private Channel mapToChannel(DMChannelDTO dmChannel) {
        Channel c = new Channel();
        c.setId(dmChannel.getId());
        c.setName(dmChannel.getUsername() != null ? dmChannel.getUsername() : "Unknown");
        c.setDescription(dmChannel.getDescription() != null ? dmChannel.getDescription() : "");
        c.setCreatedTime(dmChannel.getCreatedTime() != null ? dmChannel.getCreatedTime() : "Unknown");
        return c;
    }

    // ===== MAPEO VIDEO =====
    private Video mapToVideo(DMVideoDTO dmVideo) {
        Video v = new Video();
        v.setId(dmVideo.getId());
        v.setName(dmVideo.getTitle() != null ? dmVideo.getTitle() : "Untitled");
        v.setDescription(dmVideo.getDescription() != null ? dmVideo.getDescription() : "");
        // Se corrige la fecha de publicación del vídeo
        v.setReleaseTime(dmVideo.getCreatedTime() != null ? dmVideo.getCreatedTime() : "Unknown");

        // ===== CAPTIONS (Subtitles en Dailymotion) =====
        try {
            String url = apiUrl + "/video/" + dmVideo.getId() + "/subtitles";
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response != null && response.has("list")) {
                JsonNode listNode = response.get("list");
                DMSubtitleDTO[] subsArray = objectMapper.treeToValue(listNode, DMSubtitleDTO[].class);

                List<Caption> captions = new ArrayList<>();
                for (DMSubtitleDTO sub : subsArray) {
                    captions.add(mapToCaption(sub));
                }
                v.setCaptions(captions);
            }
        } catch (Exception e) {
        }

        // ===== COMMENTS (Tags) =====
        if (dmVideo.getTags() != null) {
            List<Comment> comments = dmVideo.getTags().stream()
                    .map(tag -> mapToComment(tag, dmVideo.getId(), v.getReleaseTime()))
                    .collect(Collectors.toList());

            v.setComments(comments);
        }

        return v;
    }

    // ===== MAPEO CAPTION =====
    private Caption mapToCaption(DMSubtitleDTO sub) {
        Caption c = new Caption();
        c.setId(UUID.randomUUID().toString());
        c.setLanguage(sub.getLanguage());
        c.setLink(sub.getUrl());
        return c;
    }

    // ===== MAPEO COMMENT =====
    private Comment mapToComment(String tag, String videoId, String releaseTime) {
        Comment c = new Comment();
        c.setId(UUID.randomUUID().toString());
        c.setText(tag);
        c.setCreatedOn(releaseTime);

        Video v = new Video();
        v.setId(videoId);

        c.setVideo(v);
        return c;
    }
}