package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.DMChannelDTO;
import aiss.dailymotionminer.model.videominer.Channel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailymotionServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private DailymotionService dailymotionService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(dailymotionService, "apiUrl", "https://api.dailymotion.com");
    }

    @Test
    void getChannelTransformsDataCorrectly() throws Exception {
        String channelId = "x-tech";

        DMChannelDTO fakeDMChannel = new DMChannelDTO();
        fakeDMChannel.setId(channelId);
        fakeDMChannel.setUsername("Tech Official");
        fakeDMChannel.setDescription("Reviews");
        fakeDMChannel.setCreatedTime("2026-05-08T10:00:00Z");

        String videosJson = "{ \"has_more\": false, \"list\": [ { \"id\": \"v1\", \"title\": \"Linux Install\", \"created_time\": \"2026-05-08\", \"tags\": [\"linux\", \"arch\"] } ] }";
        JsonNode fakeVideosNode = objectMapper.readTree(videosJson);

        String subtitlesJson = "{ \"list\": [ { \"id\": \"s1\", \"language\": \"es\", \"url\": \"http://sub.test\" } ] }";
        JsonNode fakeSubtitlesNode = objectMapper.readTree(subtitlesJson);

        when(restTemplate.getForObject(contains("/user/" + channelId), eq(DMChannelDTO.class)))
                .thenReturn(fakeDMChannel);

        when(restTemplate.getForObject(contains("/user/" + channelId + "/videos"), eq(JsonNode.class)))
                .thenReturn(fakeVideosNode);

        when(restTemplate.getForObject(contains("/video/v1/subtitles"), eq(JsonNode.class)))
                .thenReturn(fakeSubtitlesNode);

        Channel result = dailymotionService.getChannel(channelId, 1, 1);

        assertNotNull(result);
        assertEquals(channelId, result.getId());
        assertEquals("Tech Official", result.getName());
        assertEquals(1, result.getVideos().size());

        assertEquals("v1", result.getVideos().get(0).getId());
        assertEquals("Linux Install", result.getVideos().get(0).getName());

        assertEquals(1, result.getVideos().get(0).getCaptions().size());
        assertEquals("es", result.getVideos().get(0).getCaptions().get(0).getLanguage());

        assertEquals(2, result.getVideos().get(0).getComments().size());
        assertEquals("linux", result.getVideos().get(0).getComments().get(0).getText());
    }

    @Test
    void getChannelThrowsNotFoundWhenChannelIsNull() {
        when(restTemplate.getForObject(anyString(), eq(DMChannelDTO.class)))
                .thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            dailymotionService.getChannel("invalid", 10, 1);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Error al contactar con la API de Dailymotion"));
    }

    @Test
    void getChannelThrowsNotFoundOnHttpClientErrorException() {
        when(restTemplate.getForObject(anyString(), eq(DMChannelDTO.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            dailymotionService.getChannel("error-404", 10, 1);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Canal no encontrado en Dailymotion"));
    }
}
