package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import aiss.dailymotionminer.model.videominer.Channel;
import aiss.dailymotionminer.model.videominer.Video;
import aiss.dailymotionminer.model.videominer.Caption;
import aiss.dailymotionminer.model.videominer.Comment;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DailymotionService {

    private final RestTemplate restTemplate;

    @Value("${dailymotion.api.url:https://api.dailymotion.com}")
    private String apiUrl;

    public DailymotionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ===== EQUIVALENTE A getChannel =====
    public Channel getChannel(String channelId, int maxVideos, int maxPages) {

        try {
            // 1. CHANNEL (OWNER en Dailymotion)
            String channelUrl = apiUrl + "/user/" + channelId;
            DMChannelDTO dmChannel = restTemplate.getForObject(channelUrl, DMChannelDTO.class);

            Channel channel = mapToChannel(dmChannel);

            // 2. VIDEOS
            String videosUrl = apiUrl + "/user/" + channelId + "/videos?limit=" + maxVideos;
            DMVideoDTO[] videosArray = restTemplate.getForObject(videosUrl, DMVideoDTO[].class);

            List<DMVideoDTO> videos = List.of(videosArray);

            List<Video> videoList = videos.stream()
                    .map(v -> mapToVideo(v, maxPages))
                    .collect(Collectors.toList());

            channel.setVideos(videoList);
            videoList.forEach(v -> v.setChannel(channel));

            return channel;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found");
            }
            throw e;
        }
    }

    // ===== CHANNEL =====
    private Channel mapToChannel(DMChannelDTO dmChannel) {
        Channel c = new Channel();
        c.setId(dmChannel.getId());
        c.setName(dmChannel.getUsername());
        c.setDescription(dmChannel.getDescription());
        return c;
    }

    // ===== VIDEO =====
    private Video mapToVideo(DMVideoDTO dmVideo, int maxPages) {

        Video v = new Video();
        v.setId(dmVideo.getId());
        v.setName(dmVideo.getTitle());
        v.setDescription(dmVideo.getDescription());

        // ===== CAPTIONS (subtitles en Dailymotion) =====
        try {
            String url = apiUrl + "/video/" + dmVideo.getId() + "?fields=subtitles";
            DMVideoDTO detail = restTemplate.getForObject(url, DMVideoDTO.class);

            if (detail.getSubtitles() != null) {
                List<Caption> captions = detail.getSubtitles().stream()
                        .map(this::mapToCaption)
                        .collect(Collectors.toList());

                v.setCaptions(captions);
            }

        } catch (Exception e) {
            // ignorar si no hay subtitles
        }

        // ===== COMMENTS = TAGS =====
        if (dmVideo.getTags() != null) {
            List<Comment> comments = dmVideo.getTags().stream()
                    .map(tag -> mapToComment(tag, dmVideo.getId()))
                    .collect(Collectors.toList());

            v.setComments(comments);
        }

        return v;
    }

    // ===== CAPTION =====
    private Caption mapToCaption(DMSubtitleDTO sub) {
        Caption c = new Caption();
        c.setId(UUID.randomUUID().toString());
        c.setLanguage(sub.getLanguage());
        c.setLink(sub.getUrl());
        return c;
    }

    // ===== COMMENT (tags) =====
    private Comment mapToComment(String tag, String videoId) {
        Comment c = new Comment();
        c.setId(UUID.randomUUID().toString());
        c.setText(tag);

        Video v = new Video();
        v.setId(videoId);

        c.setVideo(v);
        return c;
    }
}