package aiss.peertubeminer.controller;

import aiss.peertubeminer.service.PeerTubeService;
import aiss.peertubeminer.service.VideoMinerService;
import aiss.peertubeminer.model.videominer.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PeerTubeController.class)
class PeerTubeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PeerTubeService peerTubeService;

    @MockBean
    private VideoMinerService videoMinerService;

    @Test
    void mineChannelReturnsCreatedChannel() throws Exception {

        // 1. PREPARAR: Creamos los datos falsos que escupirá nuestro Mock
        Channel mockChannel = new Channel();
        mockChannel.setId("arch-channel-1");
        mockChannel.setName("Arch Linux Global");
        mockChannel.setDescription("Canal oficial de tutoriales del sistema");

        // Le decimos al mock: "Cuando alguien llame a getChannel con los parámetros que
        // sea, devuelve mockChannel"
        when(peerTubeService.getChannel(anyString(), anyInt(), anyInt())).thenReturn(mockChannel);

        // 2. ACTUAR & 3. COMPROBAR
        // Usamos mockMvc para simular la petición POST interna
        mockMvc.perform(post("/api/peertube/channels/arch-channel-1")
                .param("maxVideos", "10")
                .param("maxComments", "10"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("arch-channel-1"))
                .andExpect(jsonPath("$.name").value("Arch Linux Global"));

        // Verificación extra: Comprobamos que el controlador realmente le ha pasado la
        // pelota al VideoMinerService
        verify(videoMinerService).sendChannelToVideoMiner(mockChannel);
    }
}
