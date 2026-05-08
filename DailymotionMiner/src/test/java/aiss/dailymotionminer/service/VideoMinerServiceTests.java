package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.videominer.Channel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoMinerServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private VideoMinerService videoMinerService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(videoMinerService, "videoMinerUrl", "http://localhost:8080");
    }

    @Test
    void sendChannelToVideoMinerSuccess() {
        Channel mockChannel = new Channel();
        mockChannel.setId("dm-1");
        mockChannel.setName("DM Test");

        when(restTemplate.postForEntity(
                eq("http://localhost:8080/videominer/channels"),
                eq(mockChannel),
                eq(String.class))).thenReturn(new ResponseEntity<>("Created", HttpStatus.CREATED));

        Channel result = videoMinerService.sendChannelToVideoMiner(mockChannel);

        assertNotNull(result);
        assertEquals("dm-1", result.getId());

        verify(restTemplate, times(1)).postForEntity(
                "http://localhost:8080/videominer/channels",
                mockChannel,
                String.class);
    }

    @Test
    void sendChannelToVideoMinerHandlesException() {
        Channel mockChannel = new Channel();
        mockChannel.setId("dm-2");

        when(restTemplate.postForEntity(
                anyString(),
                any(Channel.class),
                eq(String.class))).thenThrow(new RestClientException("Connection refused"));

        assertThrows(RestClientException.class, () -> {
            videoMinerService.sendChannelToVideoMiner(mockChannel);
        });

        verify(restTemplate, times(1)).postForEntity(
                "http://localhost:8080/videominer/channels",
                mockChannel,
                String.class);
    }
}
