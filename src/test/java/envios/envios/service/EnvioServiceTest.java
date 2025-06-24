package envios.envios.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import envios.envios.model.Envio;
import envios.envios.model.Estado;
import envios.envios.repository.EnvioRepository;

public class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @InjectMocks
    private EnvioService envioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test

    void testFindById() {
        Envio envio = new Envio(1L, 12345, "123 Main St", "2023-10-01", null, Estado.ENVIADO);
        when(envioRepository.findByIdEnvio(1L)).thenReturn(envio);
        Envio res = envioService.findByIdEnvio(1L);
        
        assertThat(res).isEqualTo(envio);
        verify(envioRepository).findByIdEnvio(1L);
    }

    @Test

    void testSave(){
        Envio envio = new Envio(null, 12345, "123 Main St", "2023-10-01", "2023-10-10", Estado.EN_PROCESO);
        Envio envioGuardado = new Envio(1L, 12345, "123 Main St", "2023-10-01", "2023-10-10", Estado.EN_PROCESO);

        when(envioRepository.save(envio)).thenReturn(envioGuardado);
        
        Envio envio2 = envioService.save(envio);

        assertNotNull(envio2);
        assertEquals(envioGuardado.getIdEnvio(), envio2.getIdEnvio());
        assertEquals(envioGuardado.getNumeroEnvio(), envio2.getNumeroEnvio());
        assertEquals(envioGuardado.getDireccionDestino(), envio2.getDireccionDestino());
        assertEquals(envioGuardado.getFechaEnvio(), envio2.getFechaEnvio());
        assertEquals(envioGuardado.getFechaEntrega(), envio2.getFechaEntrega());
        assertEquals(envioGuardado.getEstado(), envio2.getEstado());
        verify(envioRepository).save(envio);

    }


    @Test
    void testFindAll() {
        Envio envio1 = new Envio(1L, 12345, "123 Main St", "2023-10-01", "2023-10-10", Estado.EN_PROCESO);
        Envio envio2 = new Envio(2L, 67890, "456 Elm St", "2023-10-02", "2023-10-11", Estado.ENVIADO);
        when(envioRepository.findAll()).thenReturn(Arrays.asList(envio1, envio2));
        List<Envio> envios = envioService.findAll();

        assertThat(envios).hasSize(2).contains(envio1, envio2);
        verify(envioRepository).findAll();


    }

    @Test
    void testFindByNumeroEnvio() {
        Envio envio = new Envio(1L, 12345, "123 Main St", "2023-10-01", "2023-10-10", Estado.EN_PROCESO);
        when(envioRepository.findByNumeroEnvio(12345)).thenReturn(List.of(envio));
        Envio res = envioService.findByNumeroEnvio(12345);

        assertThat(res).isEqualTo(envio);
        verify(envioRepository).findByNumeroEnvio(12345);
    }

    @Test
    void testUpdateByIdEnvio() {
        Envio envioCreado = new Envio(1L, 12345, "123 Main St", "2023-10-01", "2023-10-10", Estado.EN_PROCESO);
        Envio envioActualizado = new Envio(null, 12345, "456 Elm St", "2023-10-02", "2023-10-11", Estado.ENVIADO);
        when(envioRepository.findByIdEnvio(1L)).thenReturn(envioCreado);
        when(envioRepository.save(envioActualizado)).thenReturn(envioActualizado);

        Envio resultado = envioService.updateByIdEnvio(1L, envioActualizado);

        assertEquals("456 Elm St", resultado.getDireccionDestino());
        assertEquals("2023-10-02", resultado.getFechaEnvio());
        assertEquals("2023-10-11", resultado.getFechaEntrega());
        assertEquals(Estado.ENVIADO, resultado.getEstado());
        verify(envioRepository).findByIdEnvio(1L);
        verify(envioRepository).save(envioCreado);
    }
}


