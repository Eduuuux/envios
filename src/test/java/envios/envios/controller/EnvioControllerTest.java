package envios.envios.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import envios.envios.assemblers.EnvioAssembler;
import envios.envios.model.Envio;
import envios.envios.model.Estado;
import envios.envios.service.EnvioService;

public class EnvioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EnvioService envioService;

    @Mock
    private EnvioAssembler assembler;

    @InjectMocks
    private EnvioController envioController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(envioController).build();
    }

    @Test
    void testGetAll_WithEnvios() throws Exception {
        Envio envio = new Envio(1L, "NUM123", "Destino 1", "2025-07-01", "2025-07-07", Estado.EN_PROCESO, 2, 2000, 1, 1);
        EntityModel<Envio> envioModel = EntityModel.of(envio);

        when(envioService.findAll()).thenReturn(List.of(envio));
        when(assembler.toModel(envio)).thenReturn(envioModel);

        mockMvc.perform(get("/api/envio"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.envioList[0].idEnvio").value(1L))
            .andExpect(jsonPath("$._embedded.envioList[0].numeroEnvio").value("NUM123"));

        verify(envioService).findAll();
        verify(assembler).toModel(envio);
    }

    @Test
    void testGetAll_NoEnvios() throws Exception {
        when(envioService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/envio"))
            .andExpect(status().isNoContent());

        verify(envioService).findAll();
    }

    @Test
    void testGetByNumeroEnvio_Found() throws Exception {
        Envio envio = new Envio();
        envio.setNumeroEnvio("NUM123");

        when(envioService.findByNumeroEnvio("NUM123")).thenReturn(envio);

        mockMvc.perform(get("/api/envio/buscar/NUM123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.numeroEnvio").value("NUM123"));

        verify(envioService).findByNumeroEnvio("NUM123");
    }

    @Test
    void testGetByNumeroEnvio_NotFound() throws Exception {
        when(envioService.findByNumeroEnvio("NO_EXISTE")).thenReturn(null);

        mockMvc.perform(get("/api/envio/buscar/NO_EXISTE"))
            .andExpect(status().isNotFound());

        verify(envioService).findByNumeroEnvio("NO_EXISTE");
    }

    @Test
    void testCreateEnvio_Success() throws Exception {
        Envio envio = new Envio();
        envio.setIdEnvio(null); // Nuevo envío sin id
        envio.setIdCliente(1);
        envio.setIdProducto(1);
        envio.setCantidadProducto(2);

        Envio savedEnvio = new Envio();
        savedEnvio.setIdEnvio(1L);

        when(envioService.findByIdEnvio(null)).thenReturn(null);  // No existe aún
        when(envioService.save(any(Envio.class))).thenReturn(savedEnvio);

        mockMvc.perform(post("/api/envio/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idEnvio").value(1L));

        verify(envioService).findByIdEnvio(null);
        verify(envioService).save(any(Envio.class));
    }

    @Test
    void testCreateEnvio_Conflict() throws Exception {
        Envio envio = new Envio();
        envio.setIdEnvio(1L);

        when(envioService.findByIdEnvio(1L)).thenReturn(envio);

        mockMvc.perform(post("/api/envio/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio)))
            .andExpect(status().isConflict())
            .andExpect(content().string("El envío ya existe"));

        verify(envioService).findByIdEnvio(1L);
    }

    @Test
    void testUpdateEnvioPorNumero_Success() throws Exception {
        Envio envio = new Envio();
        envio.setNumeroEnvio("NUM123");
        envio.setIdCliente(1);
        envio.setIdProducto(1);
        envio.setCantidadProducto(2);

        when(envioService.updateByNumeroEnvio(eq("NUM123"), any(Envio.class))).thenReturn(envio);

        mockMvc.perform(put("/api/envio/actualizar/NUM123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.numeroEnvio").value("NUM123"));

        verify(envioService).updateByNumeroEnvio(eq("NUM123"), any(Envio.class));
    }

    @Test
    void testUpdateEnvioPorNumero_NotFound() throws Exception {
        Envio envio = new Envio();
        envio.setNumeroEnvio("NUM123");

        when(envioService.updateByNumeroEnvio(eq("NUM123"), any(Envio.class))).thenReturn(null);

        mockMvc.perform(put("/api/envio/actualizar/NUM123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("El envío no existe"));

        verify(envioService).updateByNumeroEnvio(eq("NUM123"), any(Envio.class));
    }

    @Test
    void testUpdateEnvioPorNumero_BadRequest() throws Exception {
        Envio envio = new Envio();
        envio.setNumeroEnvio("NUM123");

        when(envioService.updateByNumeroEnvio(eq("NUM123"), any(Envio.class)))
            .thenThrow(new RuntimeException("Error de validación"));

        mockMvc.perform(put("/api/envio/actualizar/NUM123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Error al actualizar el envío: Error de validación"));

        verify(envioService).updateByNumeroEnvio(eq("NUM123"), any(Envio.class));
    }

    @Test
    void testDeleteEnvio_Success() throws Exception {
        Envio envio = new Envio();
        envio.setIdEnvio(1L);

        when(envioService.findByIdEnvio(1L)).thenReturn(envio);
        doNothing().when(envioService).deleteByIdEnvio(1L);

        mockMvc.perform(delete("/api/envio/eliminar/1"))
            .andExpect(status().isOk())
            .andExpect(content().string("Envío eliminado con éxito"));

        verify(envioService).findByIdEnvio(1L);
        verify(envioService).deleteByIdEnvio(1L);
    }

    @Test
    void testDeleteEnvio_NotFound() throws Exception {
        when(envioService.findByIdEnvio(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/envio/eliminar/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("El envío no existe"));

        verify(envioService).findByIdEnvio(1L);
        verify(envioService, never()).deleteByIdEnvio(anyLong());
    }

    @Test
    void testDeleteEnvio_InternalServerError() throws Exception {
        Envio envio = new Envio();
        envio.setIdEnvio(1L);

        when(envioService.findByIdEnvio(1L)).thenReturn(envio);
        doThrow(new RuntimeException("Error inesperado")).when(envioService).deleteByIdEnvio(1L);

        mockMvc.perform(delete("/api/envio/eliminar/1"))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Error al eliminar el envío: Error inesperado"));

        verify(envioService).findByIdEnvio(1L);
        verify(envioService).deleteByIdEnvio(1L);
    }
}