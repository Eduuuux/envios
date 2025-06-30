package envios.envios.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import envios.envios.assemblers.EnvioAssembler;
import envios.envios.model.Envio;
import envios.envios.model.Estado;
import envios.envios.service.EnvioService;

@WebMvcTest(EnvioController.class)
public class EnvioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioService envioService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EnvioAssembler assembler;

    @BeforeEach
    public void setUp() {
        
    }

    @Test
    void testGetAllEnvios() throws Exception {

    Envio envio1 = new Envio();
    envio1.setIdEnvio(1L);
    envio1.setNumeroEnvio(1001);
    envio1.setEstado(Estado.ENVIADO);
    envio1.setDireccionDestino("Calle 1, Ciudad A");

    Envio envio2 = new Envio();
    envio2.setIdEnvio(2L);
    envio2.setNumeroEnvio(1002);
    envio2.setEstado(Estado.ENVIADO);
    envio2.setDireccionDestino("Calle 2, Ciudad B");

    List<Envio> envios = List.of(envio1, envio2);

    Mockito.when(envioService.findAll()).thenReturn(envios);
    Mockito.when(assembler.toModel(envio1)).thenReturn(EntityModel.of(envio1));
    Mockito.when(assembler.toModel(envio2)).thenReturn(EntityModel.of(envio2));
    
    mockMvc.perform(get("/api/envio"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.entityModelList.length()").value(2))
        .andExpect(jsonPath("$._embedded.entityModelList[0].numeroEnvio").value(1001))
        .andExpect(jsonPath("$._embedded.entityModelList[0].direccionDestino").value("Calle 1, Ciudad A"))
        .andExpect(jsonPath("$._embedded.entityModelList[1].numeroEnvio").value(1002))
        .andExpect(jsonPath("$._embedded.entityModelList[1].direccionDestino").value("Calle 2, Ciudad B"));
    }

    @Test
    void testGetById() throws Exception {

        Envio envio = new Envio();
        envio.setIdEnvio(1L);
        envio.setNumeroEnvio(1001);
        envio.setEstado(Estado.ENVIADO);
        envio.setDireccionDestino("Calle 1, Ciudad A");
        envio.setFechaEnvio("2025-06-30");
        envio.setFechaEntrega("2025-07-02");

        EntityModel<Envio> envioModel = EntityModel.of(envio);

        Mockito.when(envioService.findByIdEnvio(1L)).thenReturn(envio);
        Mockito.when(assembler.toModel(envio)).thenReturn(envioModel);

        mockMvc.perform(get("/api/envio/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idEnvio").value(1))
            .andExpect(jsonPath("$.numeroEnvio").value(1001))
            .andExpect(jsonPath("$.direccionDestino").value("Calle 1, Ciudad A"))
            .andExpect(jsonPath("$.fechaEnvio").value("2025-06-30"))
            .andExpect(jsonPath("$.fechaEntrega").value("2025-07-02"));
    }



    @Test
    void testGetByNumeroEnvio_OK() throws Exception {
        Envio envio = new Envio();
        envio.setIdEnvio(1L);
        envio.setNumeroEnvio(12345);
        envio.setDireccionDestino("123 Main St");
        envio.setFechaEnvio("2023-10-01");
        envio.setFechaEntrega("2023-10-10");
        envio.setEstado(Estado.EN_PROCESO);

        Mockito.when(envioService.findByNumeroEnvio(12345)).thenReturn(envio);

        mockMvc.perform(get("/api/envio/buscar/12345"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idEnvio").value(1))
            .andExpect(jsonPath("$.numeroEnvio").value(12345))
            .andExpect(jsonPath("$.direccionDestino").value("123 Main St"))
            .andExpect(jsonPath("$.fechaEnvio").value("2023-10-01"))
            .andExpect(jsonPath("$.fechaEntrega").value("2023-10-10"))
            .andExpect(jsonPath("$.estado").value("EN_PROCESO"));
    }

    @Test
    public void testCrearEnvio() throws Exception {
        Envio envio = new Envio();
        envio.setIdEnvio(1L);
        envio.setNumeroEnvio(12345);
        envio.setDireccionDestino("Calle Nueva 456");
        envio.setFechaEnvio("2025-07-01");
        envio.setFechaEntrega("2025-07-05");
        envio.setEstado(Estado.EN_PROCESO);

        Mockito.when(envioService.findByIdEnvio(envio.getIdEnvio())).thenReturn(null);
        Mockito.when(envioService.save(Mockito.any(Envio.class))).thenReturn(envio);
        Mockito.when(assembler.toModel(envio)).thenReturn(EntityModel.of(envio));

        mockMvc.perform(post("/api/envio/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroEnvio").value(12345))
                .andExpect(jsonPath("$.direccionDestino").value("Calle Nueva 456"))
                .andExpect(jsonPath("$.fechaEnvio").value("2025-07-01"))
                .andExpect(jsonPath("$.fechaEntrega").value("2025-07-05"))
                .andExpect(jsonPath("$.estado").value("EN_PROCESO"));
    }
    @Test
    public void testUpdateEnvioPorId_OK() throws Exception {

        Envio envioBD = new Envio();
        envioBD.setIdEnvio(1L);
        envioBD.setNumeroEnvio(12345);
        envioBD.setDireccionDestino("Calle Nueva 456");
        envioBD.setFechaEnvio("2025-07-01");
        envioBD.setFechaEntrega("2025-07-05");
        envioBD.setEstado(Estado.EN_PROCESO);

        Envio envioActualizado = new Envio();
        envioActualizado.setIdEnvio(1L);
        envioActualizado.setNumeroEnvio(12345);
        envioActualizado.setDireccionDestino("Calle Nueva 789");
        envioActualizado.setFechaEnvio("2025-07-02");
        envioActualizado.setFechaEntrega("2025-07-06");
        envioActualizado.setEstado(Estado.ENVIADO);

        Mockito.when(envioService.findByIdEnvio(1L)).thenReturn(envioBD);
        Mockito.when(envioService.save(Mockito.any(Envio.class))).thenReturn(envioActualizado);
        Mockito.when(assembler.toModel(envioActualizado)).thenReturn(EntityModel.of(envioActualizado));
        
        mockMvc.perform(put("/api/envio/actualizar/id/{idEnvio}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envioActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccionDestino").value("Calle Nueva 789"))
                .andExpect(jsonPath("$.fechaEnvio").value("2025-07-02"))
                .andExpect(jsonPath("$.fechaEntrega").value("2025-07-06"))
                .andExpect(jsonPath("$.estado").value("ENVIADO"));
    }

        @Test
        public void testUpdateEnvioPorNumero_OK() throws Exception {

            Envio envioBD = new Envio();
            envioBD.setIdEnvio(1L);
            envioBD.setNumeroEnvio(12345);
            envioBD.setDireccionDestino("Calle Nueva 456");
            envioBD.setFechaEnvio("2025-07-01");
            envioBD.setFechaEntrega("2025-07-05");
            envioBD.setEstado(Estado.EN_PROCESO);

            Envio envioActualizado = new Envio();
            envioActualizado.setIdEnvio(1L);
            envioActualizado.setNumeroEnvio(12345);
            envioActualizado.setDireccionDestino("Calle Nueva 999");
            envioActualizado.setFechaEnvio("2025-07-10");
            envioActualizado.setFechaEntrega("2025-07-15");
            envioActualizado.setEstado(Estado.RECIBIDO);

            Mockito.when(envioService.findByNumeroEnvio(12345)).thenReturn(envioBD);
            Mockito.when(envioService.save(Mockito.any(Envio.class))).thenReturn(envioActualizado);
            Mockito.when(assembler.toModel(envioActualizado)).thenReturn(EntityModel.of(envioActualizado));

            mockMvc.perform(put("/api/envio/actualizar/{numeroEnvio}", 12345)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(envioActualizado)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.direccionDestino").value("Calle Nueva 999"))
                    .andExpect(jsonPath("$.fechaEnvio").value("2025-07-10"))
                    .andExpect(jsonPath("$.fechaEntrega").value("2025-07-15"))
                    .andExpect(jsonPath("$.estado").value("RECIBIDO"));
        }

    @Test
    public void testDeleteEnvioById_Exitoso() throws Exception {
        Envio envio = new Envio();
        envio.setIdEnvio(1L);
        envio.setNumeroEnvio(12345);
        envio.setDireccionDestino("Calle Falsa 123");
        envio.setFechaEnvio("2025-07-01");
        envio.setFechaEntrega("2025-07-05");
        envio.setEstado(Estado.ENVIADO);

        Mockito.when(envioService.findByIdEnvio(1L)).thenReturn(envio);
        Mockito.doNothing().when(envioService).deleteByIdEnvio(1L);

        mockMvc.perform(delete("/api/envio/eliminar/{idEnvio}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(envioService, Mockito.times(1)).deleteByIdEnvio(1L);
    }
    }








