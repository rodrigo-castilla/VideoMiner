package aiss.peertubeminer.service;

import aiss.peertubeminer.model.PTChannelDTO;
import aiss.peertubeminer.model.videominer.Channel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PeerTubeMinerServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PeerTubeService peerTubeService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(peerTubeService, "peertubeApiUrl", "https://peertube.tv");
    }

    @Test
    void getChannel() {
        String channelId = "pt-ch-1";
        PTChannelDTO fakePTChannel = new PTChannelDTO();
        fakePTChannel.setId(channelId);
        fakePTChannel.setName("Arch Linux Global");
        fakePTChannel.setDescription("Tutoriales del sistema");

        Map<String, Object> fakeVideo1 = new HashMap<>();
        fakeVideo1.put("uuid", "video-uuid-999");
        fakeVideo1.put("name", "Actualización del Kernel");
        fakeVideo1.put("publishedAt", "2026-05-08T12:00:00Z");

        Map<String, Object> fakeVideosResponse = new HashMap<>();
        fakeVideosResponse.put("data", List.of(fakeVideo1));

        when(restTemplate.getForObject(
                "https://peertube.tv/api/v1/video-channels/" + channelId,
                PTChannelDTO.class))
                .thenReturn(fakePTChannel);

        when(restTemplate.getForObject(
                "https://peertube.tv/api/v1/video-channels/" + channelId + "/videos?count=10",
                Map.class))
                .thenReturn(fakeVideosResponse);

        Channel result = peerTubeService.getChannel(channelId, 10, 0);

        assertNotNull(result);
        assertEquals("pt-ch-1", result.getId());
        assertEquals("Arch Linux Global", result.getName());
        assertNotNull(result.getVideos());
        assertEquals(1, result.getVideos().size());
        assertEquals("video-uuid-999", result.getVideos().get(0).getId());
        assertEquals("Actualización del Kernel", result.getVideos().get(0).getName());
    }

    @Test
    void notFoundChannel() {
        String channelId = "invalid-channel";

        // Simulamos que el canal no existe y devuelve null
        when(restTemplate.getForObject(anyString(), eq(PTChannelDTO.class)))
                .thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            peerTubeService.getChannel(channelId, 10, 0);
        });

        // Comprobamos que devuelve un 404
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        // IMPORTANTE: Buscamos el mensaje que lanza el catch(), no el del if()
        assertTrue(exception.getReason().contains("Error al contactar con la API de PeerTube"));
    }

    @Test
    void notFoundChannelApiError() {
        String channelId = "error-channel";

        // Simulamos un error de conexión real a internet
        when(restTemplate.getForObject(anyString(), eq(PTChannelDTO.class)))
                .thenThrow(new RestClientException("Connection timed out"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            peerTubeService.getChannel(channelId, 10, 0);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Error al contactar con la API de PeerTube"));
    }
}
