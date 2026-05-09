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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PeerTubeController.class)
class PeerTubeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Simulamos los servicios para que el test no haga peticiones HTTP reales a
    // internet
    @MockBean
    private PeerTubeService peerTubeService;

    @MockBean
    private VideoMinerService videoMinerService;

    @Test
    void mineChannelReturnsCreatedChannel() throws Exception {
        // 1. Preparamos un canal de prueba simulado
        Channel mockChannel = new Channel();
        mockChannel.setId("c1");
        mockChannel.setName("Canal de Prueba");
        mockChannel.setDescription("Descripción de prueba");

        // 2. Le decimos al servicio falso que cuando le pidan un canal, devuelva el
        // nuestro
        when(peerTubeService.getChannel(anyString(), anyInt(), anyInt())).thenReturn(mockChannel);

        // 3. Hacemos la petición POST al nuevo endpoint correcto que definimos antes
        mockMvc.perform(post("/api/peertube/channels/c1")
                .param("maxVideos", "10")
                .param("maxComments", "10"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("c1"))
                .andExpect(jsonPath("$.name").value("Canal de Prueba"));
    }
}
