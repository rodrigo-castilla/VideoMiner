package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.service.DailymotionService;
import aiss.dailymotionminer.service.VideoMinerService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DailymotionController.class)
class DailymotionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DailymotionService dailymotionService;

    @MockBean
    private VideoMinerService videoMinerService;

    // STATUS
    @Test
    void statusEndpointReturnsRunning() throws Exception {
        mockMvc.perform(get("/api/dailymotion/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("DailymotionMiner is running"));
    }

    // CHANNEL
    @Test
    void createChannelReturnsCreatedAndEchoesChannel() throws Exception {

        String channelJson = """
        {
            "id":"c1",
            "name":"Test Channel",
            "description":"Channel desc"
        }
        """;

        mockMvc.perform(post("/api/dailymotion/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(channelJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("c1"))
                .andExpect(jsonPath("$.name").value("Test Channel"))
                .andExpect(jsonPath("$.description").value("Channel desc"));
    }

    // VIDEO
    @Test
    void createVideoReturnsCreatedAndEchoesVideo() throws Exception {

        String videoJson = """
        {
            "id":"v1",
            "title":"Test Video",
            "description":"A sample video"
        }
        """;

        mockMvc.perform(post("/api/dailymotion/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(videoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("v1"))
                .andExpect(jsonPath("$.title").value("Test Video"))
                .andExpect(jsonPath("$.description").value("A sample video"));
    }

    // COMMENT
    @Test
    void createCommentFromTagReturnsCreated() throws Exception {

        String tagJson = """
        {
            "text":"music"
        }
        """;

        mockMvc.perform(post("/api/dailymotion/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tagJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("music"));
    }

    // CAPTION
    @Test
    void createCaptionReturnsCreatedAndEchoesCaption() throws Exception {

        String captionJson = """
        {
            "url":"https://example.com/captions.srt",
            "language":"en"
        }
        """;

        mockMvc.perform(post("/api/dailymotion/captions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(captionJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").value("https://example.com/captions.srt"))
                .andExpect(jsonPath("$.language").value("en"));
    }
}