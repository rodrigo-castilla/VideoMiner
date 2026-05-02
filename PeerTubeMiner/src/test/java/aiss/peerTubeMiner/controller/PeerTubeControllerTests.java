package aiss.peertubeminer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PeerTubeController.class)
class PeerTubeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void statusEndpointReturnsRunning() throws Exception {
        mockMvc.perform(get("/api/peertube/status"))
            .andExpect(status().isOk())
            .andExpect(content().string("PeerTubeMiner is running"));
    }

    @Test
    void createVideoReturnsCreatedAndEchoesVideo() throws Exception {
        String videoJson = "{\"id\":\"v1\",\"name\":\"Test Video\",\"description\":\"A sample video\"}";

        mockMvc.perform(post("/api/peertube/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(videoJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("v1"))
            .andExpect(jsonPath("$.name").value("Test Video"))
            .andExpect(jsonPath("$.description").value("A sample video"));
    }

    @Test
    void createChannelReturnsCreatedAndEchoesChannel() throws Exception {
        String channelJson = "{\"id\":\"c1\",\"name\":\"Test Channel\",\"description\":\"Channel desc\"}";

        mockMvc.perform(post("/api/peertube/channels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(channelJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("c1"))
            .andExpect(jsonPath("$.name").value("Test Channel"))
            .andExpect(jsonPath("$.description").value("Channel desc"));
    }

    @Test
    void createCommentReturnsCreatedAndEchoesComment() throws Exception {
        String commentJson = "{\"id\":\"m1\",\"text\":\"Great video!\"}";

        mockMvc.perform(post("/api/peertube/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commentJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("m1"))
            .andExpect(jsonPath("$.text").value("Great video!"));
    }

    @Test
    void createCaptionReturnsCreatedAndEchoesCaption() throws Exception {
        String captionJson = "{\"fileUrl\":\"https://example.com/captions.srt\",\"language\":\"en\"}";

        mockMvc.perform(post("/api/peertube/captions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(captionJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.fileUrl").value("https://example.com/captions.srt"))
            .andExpect(jsonPath("$.language").value("en"));
    }

    @Test
    void createAccountReturnsCreatedAndEchoesAccount() throws Exception {
        String accountJson = "{\"id\":\"a1\",\"name\":\"Test Account\",\"url\":\"https://example.com/user\",\"avatar\":\"https://example.com/avatar.png\"}";

        mockMvc.perform(post("/api/peertube/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("a1"))
            .andExpect(jsonPath("$.name").value("Test Account"))
            .andExpect(jsonPath("$.url").value("https://example.com/user"))
            .andExpect(jsonPath("$.avatar").value("https://example.com/avatar.png"));
    }
}

