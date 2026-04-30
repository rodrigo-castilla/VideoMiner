package aiss.peertubeminer.service;

import aiss.peertubeminer.model.*;
import aiss.videominer.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//Clase que se conecta con la API de Peertube, recoge los datos y los transforma al modelo comun
@Service
public class PeerTubeService {

    private final RestTemplate restTemplate;

    @Value("${peertube.api.url:https://peertube.example.com}")
    private String peertubeApiUrl;

    public PeerTubeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Channel getChannel(String channelId, int maxVideos, int maxComments) {
        try {
            // Get channel info
            String channelUrl = peertubeApiUrl + "/api/v1/video-channels/" + channelId;
            PTChannelDTO ptChannel = restTemplate.getForObject(channelUrl, PTChannelDTO.class);

            // Get videos
            String videosUrl = peertubeApiUrl + "/api/v1/video-channels/" + channelId + "/videos?count=" + maxVideos;
            PTVideoDTO[] ptVideosArray = restTemplate.getForObject(videosUrl, PTVideoDTO[].class);
            List<PTVideoDTO> ptVideos = List.of(ptVideosArray);

            // Map to common model
            Channel channel = mapToChannel(ptChannel);

            List<Video> videos = ptVideos.stream()
                .map(ptVideo -> mapToVideo(ptVideo, maxComments))
                .collect(Collectors.toList());

            channel.setVideos(videos);
            videos.forEach(video -> video.setChannel(channel));

            return channel;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found");
            }
            throw e;
        }
    }

    private Channel mapToChannel(PTChannelDTO ptChannel) {
        Channel channel = new Channel();
        channel.setId(ptChannel.getId());
        channel.setName(ptChannel.getName());
        channel.setDescription(ptChannel.getDescription());
        channel.setCreatedTime(ptChannel.getCreatedAt().toString());
        return channel;
    }

    private Video mapToVideo(PTVideoDTO ptVideo, int maxComments) {
        Video video = new Video();
        video.setId(ptVideo.getId());
        video.setName(ptVideo.getName());
        video.setDescription(ptVideo.getDescription());
        video.setReleaseTime(ptVideo.getPublishedAt().toString());

        // Map author
        if (ptVideo.getAccount() != null) {
            User author = mapToUser(ptVideo.getAccount());
            video.setAuthor(author);
        }

        // Get captions
        try {
            String captionsUrl = peertubeApiUrl + "/api/v1/videos/" + ptVideo.getId() + "/captions";
            PTCaptionDTO[] ptCaptionsArray = restTemplate.getForObject(captionsUrl, PTCaptionDTO[].class);
            List<PTCaptionDTO> ptCaptions = List.of(ptCaptionsArray);
            List<Caption> captions = ptCaptions.stream()
                .map(this::mapToCaption)
                .collect(Collectors.toList());
            video.setCaptions(captions);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
            // If 404, no captions
        }

        // Get comments
        try {
            String commentsUrl = peertubeApiUrl + "/api/v1/videos/" + ptVideo.getId() + "/comment-threads?count=" + maxComments;
            PTCommentDTO[] ptCommentsArray = restTemplate.getForObject(commentsUrl, PTCommentDTO[].class);
            List<PTCommentDTO> ptComments = List.of(ptCommentsArray);
            List<Comment> comments = ptComments.stream()
                .map(this::mapToComment)
                .collect(Collectors.toList());
            video.setComments(comments);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
            // If 404, no comments
        }

        return video;
    }

    private User mapToUser(PTAccountDTO ptAccount) {
        User user = new User();
        user.setId(ptAccount.getId());
        user.setName(ptAccount.getName());
        user.setUser_link(ptAccount.getUrl());
        user.setPicture_link(ptAccount.getAvatar());
        return user;
    }

    private Caption mapToCaption(PTCaptionDTO ptCaption) {
        Caption caption = new Caption();
        caption.setId(UUID.randomUUID().toString()); // Generate ID since not provided
        caption.setName(ptCaption.getFileUrl());
        caption.setLanguage(ptCaption.getLanguage());
        return caption;
    }

    private Comment mapToComment(PTCommentDTO ptComment) {
        Comment comment = new Comment();
        comment.setId(ptComment.getId());
        comment.setText(ptComment.getText());
        comment.setCreatedOn(ptComment.getCreatedAt().toString());
        return comment;
    }
}