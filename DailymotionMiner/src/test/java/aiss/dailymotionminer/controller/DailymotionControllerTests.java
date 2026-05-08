package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.videominer.Channel;
import aiss.dailymotionminer.service.DailymotionService;
import aiss.dailymotionminer.service.VideoMinerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailymotionController.class)
class DailymotionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DailymotionService dailymotionService;

    @MockBean
    private VideoMinerService videoMinerService;

    @Test
    void createChannelReturnsCreatedChannel() throws Exception {
        Channel mockChannel = new Channel();
        mockChannel.setId("dm-c1");
        mockChannel.setName("Dailymotion Tech");
        mockChannel.setDescription("Tech Channel");

        when(dailymotionService.getChannel(anyString(), anyInt(), anyInt())).thenReturn(mockChannel);

        mockMvc.perform(post("/api/dailymotion/channels/dm-c1")
                .param("maxVideos", "5")
                .param("maxPages", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("dm-c1"))
                .andExpect(jsonPath("$.name").value("Dailymotion Tech"));

        verify(videoMinerService).sendChannelToVideoMiner(mockChannel);
    }

    @Test
    void getChannelReturnsOk() throws Exception {
        Channel mockChannel = new Channel();
        mockChannel.setId("dm-c2");
        mockChannel.setName("Dailymotion Gaming");

        when(dailymotionService.getChannel(anyString(), anyInt(), anyInt())).thenReturn(mockChannel);

        mockMvc.perform(get("/api/dailymotion/channels/dm-c2")
                .param("maxVideos", "5")
                .param("maxPages", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("dm-c2"))
                .andExpect(jsonPath("$.name").value("Dailymotion Gaming"));
    }
}
