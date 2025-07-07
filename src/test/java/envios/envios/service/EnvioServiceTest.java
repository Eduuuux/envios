package envios.envios.service;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import envios.envios.DTO.ProductoDTO;
import envios.envios.DTO.UsuarioDTO;
import envios.envios.model.Envio;
import envios.envios.model.Estado;
import envios.envios.repository.EnvioRepository;

public class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EnvioService envioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    private Envio crearEnvio(int idCliente, int idProducto, int cantidadProducto) {
    Envio envio = new Envio();
        envio.setIdCliente(idCliente);
        envio.setIdProducto(idProducto);
        envio.setCantidadProducto(cantidadProducto);
    return envio;
    }
    private ProductoDTO crearProductoDTO(int stock, int valor) {
        ProductoDTO producto = new ProductoDTO();
        producto.setId(10);
        producto.setStock(stock);
        producto.setValor(valor);
        return producto;
    }

        private UsuarioDTO crearUsuarioDTO(boolean estado, int permiso) {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1);
        usuario.setEstado(estado);
        usuario.setPermiso(permiso);
        usuario.setDireccion("Calle Falsa 123");
        return usuario;
    }






    @Test
        void testFindUsuarioById() {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(10);
        usuario.setEstado(true);
        usuario.setPermiso(2);
        usuario.setDireccion("123 Main St");

        when(restTemplate.getForObject("http://localhost:8081/usuario/10", UsuarioDTO.class)).thenReturn(usuario);

        UsuarioDTO result = envioService.findUsuarioById(10);

        assertNotNull(result);
        assertThat(result.getId()).isEqualTo(10);
        assertThat(result.getDireccion()).isEqualTo("123 Main St");
        verify(restTemplate).getForObject("http://localhost:8081/usuario/10", UsuarioDTO.class);
    }

    @Test
        void testFindProductoById() {
            ProductoDTO producto = new ProductoDTO();
            producto.setId(20);
            producto.setValor(100);
            producto.setStock(5);

            when(restTemplate.getForObject("http://localhost:8085/producto/20", ProductoDTO.class)).thenReturn(producto);

            ProductoDTO result = envioService.findProductoById(20);

            assertNotNull(result);
            assertThat(result.getId()).isEqualTo(20);
            assertThat(result.getValor()).isEqualTo(100);
            assertThat(result.getStock()).isEqualTo(5);
            verify(restTemplate).getForObject("http://localhost:8085/producto/20", ProductoDTO.class);
        }

    @Test
    void testFindById() {
        Envio envio = new Envio(
            1L,
            "12345", // numeroEnvio es String, no int
            "123 Main St",
            "2023-10-01",
            null,
            Estado.ENVIADO,
            5, // cantidadProducto
            10000, // valorTotal
            1, // idCliente
            1  // idProducto
        );

        when(envioRepository.findByIdEnvio(1L)).thenReturn(envio);
        Envio res = envioService.findByIdEnvio(1L);

        assertThat(res).isEqualTo(envio);
        verify(envioRepository).findByIdEnvio(1L);
    }


    @Test
    void testSave() {
        Envio envio = new Envio();
        envio.setIdCliente(10);
        envio.setIdProducto(20);
        envio.setCantidadProducto(2);

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(10);
        usuario.setEstado(true);
        usuario.setPermiso(2);
        usuario.setDireccion("123 Main St");

        ProductoDTO producto = new ProductoDTO();
        producto.setId(20);
        producto.setValor(100);
        producto.setStock(5);

        Envio envioGuardado = new Envio();
        envioGuardado.setIdEnvio(1L);
        envioGuardado.setIdCliente(10);
        envioGuardado.setIdProducto(20);
        envioGuardado.setCantidadProducto(2);
        envioGuardado.setDireccionDestino("123 Main St");
        envioGuardado.setEstado(Estado.EN_PROCESO);
        envioGuardado.setValorTotal(200);

        when(restTemplate.getForObject("http://localhost:8081/usuario/10", UsuarioDTO.class)).thenReturn(usuario);
        when(restTemplate.getForObject("http://localhost:8085/producto/20", ProductoDTO.class)).thenReturn(producto);
        when(envioRepository.save(any(Envio.class))).thenReturn(envioGuardado);

        Envio result = envioService.save(envio);

        assertNotNull(result);
        assertThat(result.getIdEnvio()).isEqualTo(1L);
        assertThat(result.getDireccionDestino()).isEqualTo("123 Main St");
        assertThat(result.getValorTotal()).isEqualTo(200);
        assertThat(result.getEstado()).isEqualTo(Estado.EN_PROCESO);

        verify(restTemplate).getForObject("http://localhost:8081/usuario/10", UsuarioDTO.class);
        verify(restTemplate).getForObject("http://localhost:8085/producto/20", ProductoDTO.class);
        verify(envioRepository).save(any(Envio.class));
    }
    
    @Test
    void testSaveUsuarioNoEncontrado() {
        Envio envio = crearEnvio(1, 10, 5);
        when(restTemplate.getForObject("http://localhost:8081/usuario/" + envio.getIdCliente(), UsuarioDTO.class))
            .thenReturn(null);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> envioService.save(envio));
        assertTrue(ex.getMessage().contains("Usuario no encontrado"));
    }
    @Test
    public void testSaveUsuarioDeshabilitado() {
        UsuarioDTO usuario = crearUsuarioDTO(false, 5);
        Envio envio = crearEnvio(1, 10, 5);

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + envio.getIdCliente(), UsuarioDTO.class))
            .thenReturn(usuario);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> envioService.save(envio));
        assertTrue(ex.getMessage().contains("Cuenta deshabilitada"));
    }

    @Test
        public void testSaveProductoNoEncontrado() {
        UsuarioDTO usuario = crearUsuarioDTO(true, 5);
        Envio envio = crearEnvio(1, 10, 5);

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + envio.getIdCliente(), UsuarioDTO.class))
            .thenReturn(usuario);
        when(restTemplate.getForObject("http://localhost:8085/producto/" + envio.getIdProducto(), ProductoDTO.class))
            .thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> envioService.save(envio));
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
        public void testSaveStockInsuficiente() {
        UsuarioDTO usuario = crearUsuarioDTO(true, 5);
        ProductoDTO producto = crearProductoDTO(3, 100);
        Envio envio = crearEnvio(1, 10, 5); // Cantidad > stock

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + envio.getIdCliente(), UsuarioDTO.class))
            .thenReturn(usuario);
        when(restTemplate.getForObject("http://localhost:8085/producto/" + envio.getIdProducto(), ProductoDTO.class))
            .thenReturn(producto);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> envioService.save(envio));
        assertTrue(ex.getMessage().contains("Stock insuficiente"));
    }


    @Test
    void testFindAll() {
        Envio envio1 = new Envio(
            1L,
            "12345", // numeroEnvio debe ser String
            "123 Main St",
            "2023-10-01",
            "2023-10-10",
            Estado.EN_PROCESO,
            5,    // cantidadProducto
            10000, // valorTotal
            1,    // idCliente
            1     // idProducto
        );

        Envio envio2 = new Envio(
            2L,
            "67890",
            "456 Elm St",
            "2023-10-02",
            "2023-10-11",
            Estado.ENVIADO,
            3,    // cantidadProducto
            5000, // valorTotal
            2,    // idCliente
            2     // idProducto
        );

        when(envioRepository.findAll()).thenReturn(Arrays.asList(envio1, envio2));
        List<Envio> envios = envioService.findAll();

        assertThat(envios).hasSize(2).contains(envio1, envio2);
        verify(envioRepository).findAll();
    }

@Test
void testFindByNumeroEnvio() {
    Envio envio = new Envio(
        1L,
        "12345", // numeroEnvio debe ser String
        "123 Main St",
        "2023-10-01",
        "2023-10-10",
        Estado.EN_PROCESO,
        5,    // cantidadProducto
        10000, // valorTotal
        1,    // idCliente
        1     // idProducto
    );

        // Mock: simula que el repositorio retorna el objeto envio solicitado
        when(envioRepository.findByNumeroEnvio("12345")).thenReturn(List.of(envio));
        // Llama al servicio con el número de envío
        Envio result = envioService.findByNumeroEnvio("12345");

        // Verifica que el resultado sea el mismo que el mock
        assertThat(result).isEqualTo(envio);

        // Verifica que el repositorio fue llamado correctamente
        verify(envioRepository).findByNumeroEnvio("12345");
    }


    @Test
    public void testUpdateByIdEnvio_Success() {
        Long idEnvio = 1L;
        Envio envioUpdate = crearEnvio(1, 10, 3);

        Envio existingEnvio = new Envio();
        existingEnvio.setIdEnvio(idEnvio);
        existingEnvio.setCantidadProducto(2);

        UsuarioDTO usuario = crearUsuarioDTO(true, 5);
        ProductoDTO producto = crearProductoDTO(10, 100);

        when(envioRepository.findByIdEnvio(idEnvio)).thenReturn(existingEnvio);
        when(restTemplate.getForObject("http://localhost:8081/usuario/" + envioUpdate.getIdCliente(), UsuarioDTO.class)).thenReturn(usuario);
        when(restTemplate.getForObject("http://localhost:8085/producto/" + envioUpdate.getIdProducto(), ProductoDTO.class)).thenReturn(producto);
        when(envioRepository.save(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Envio result = envioService.updateByIdEnvio(idEnvio, envioUpdate);

        assertNotNull(result);
        assertEquals(usuario.getId(), result.getIdCliente());
        assertEquals(producto.getId(), result.getIdProducto());
        assertEquals(Estado.EN_PROCESO, result.getEstado());
        assertEquals(3, result.getCantidadProducto());
    }

    @Test
    void testUpdateByIdEnvio_NotFound() {
    Long idEnvio = 1L;
    Envio envioToUpdate = crearEnvio(10, 20, 2);

    when(envioRepository.findByIdEnvio(idEnvio)).thenReturn(null);

    Envio result = envioService.updateByIdEnvio(idEnvio, envioToUpdate);

    assertThat(result).isNull();
    verify(envioRepository).findByIdEnvio(idEnvio);
}

    @Test
    void testUpdateByIdEnvio_UsuarioNoEncontrado() {
        Long idEnvio = 1L;
        Envio existingEnvio = new Envio();
        existingEnvio.setIdEnvio(idEnvio);

        Envio envioToUpdate = crearEnvio(10, 20, 2);

        when(envioRepository.findByIdEnvio(idEnvio)).thenReturn(existingEnvio);
        when(restTemplate.getForObject("http://localhost:8081/usuario/" + envioToUpdate.getIdCliente(), UsuarioDTO.class)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> envioService.updateByIdEnvio(idEnvio, envioToUpdate));
        assertTrue(ex.getMessage().contains("Usuario no encontrado"));
    }



    @Test
    void testUpdateByIdEnvio_StockInsuficiente() {
        Long idEnvio = 1L;
        Envio existingEnvio = new Envio();
        existingEnvio.setIdEnvio(idEnvio);

        Envio envioToUpdate = crearEnvio(10, 20, 5); // Solicita más de lo disponible

        UsuarioDTO usuario = crearUsuarioDTO(true, 5);
        ProductoDTO producto = crearProductoDTO(2, 100); // Stock insuficiente

        when(envioRepository.findByIdEnvio(idEnvio)).thenReturn(existingEnvio);
        when(restTemplate.getForObject("http://localhost:8081/usuario/" + envioToUpdate.getIdCliente(), UsuarioDTO.class)).thenReturn(usuario);
        when(restTemplate.getForObject("http://localhost:8085/producto/" + envioToUpdate.getIdProducto(), ProductoDTO.class)).thenReturn(producto);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> envioService.updateByIdEnvio(idEnvio, envioToUpdate));
        assertTrue(ex.getMessage().contains("Stock insuficiente"));
    }

    @Test
    void testUpdateByIdEnvio_ProductoNoEncontrado() {
        Long idEnvio = 1L;
        Envio existingEnvio = new Envio();
        existingEnvio.setIdEnvio(idEnvio);

        Envio envioToUpdate = crearEnvio(10, 20, 2);

        UsuarioDTO usuario = crearUsuarioDTO(true, 5);

        when(envioRepository.findByIdEnvio(idEnvio)).thenReturn(existingEnvio);
        when(restTemplate.getForObject("http://localhost:8081/usuario/" + envioToUpdate.getIdCliente(), UsuarioDTO.class)).thenReturn(usuario);
        when(restTemplate.getForObject("http://localhost:8085/producto/" + envioToUpdate.getIdProducto(), ProductoDTO.class)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> envioService.updateByIdEnvio(idEnvio, envioToUpdate));
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

        @Test
    void testUpdateByNumeroEnvio_Success() {
        Envio envioExistente = new Envio();
        envioExistente.setNumeroEnvio("ABC123");
        envioExistente.setCantidadProducto(2);
        envioExistente.setIdCliente(10);
        envioExistente.setIdProducto(20);
        envioExistente.setEstado(Estado.EN_PROCESO);

        Envio envioActualizado = new Envio();
        envioActualizado.setCantidadProducto(3);
        envioActualizado.setIdCliente(15);
        envioActualizado.setIdProducto(25);

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEstado(true);
        usuario.setPermiso(5);
        usuario.setId(15);
        usuario.setDireccion("Calle Test 123");

        ProductoDTO producto = new ProductoDTO();
        producto.setStock(10);
        producto.setValor(100);
        producto.setId(25);

        when(envioRepository.findByNumeroEnvio("ABC123")).thenReturn(List.of(envioExistente));
        when(restTemplate.getForObject("http://localhost:8081/usuario/15", UsuarioDTO.class)).thenReturn(usuario);
        when(restTemplate.getForObject("http://localhost:8085/producto/25", ProductoDTO.class)).thenReturn(producto);
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArguments()[0]);

        Envio result = envioService.updateByNumeroEnvio("ABC123", envioActualizado);

        assertNotNull(result);
        assertEquals(usuario.getId(), result.getIdCliente());
        assertEquals(producto.getId(), result.getIdProducto());
        assertEquals(Estado.EN_PROCESO, result.getEstado());
        assertEquals(producto.getValor() * envioActualizado.getCantidadProducto(), result.getValorTotal());
        assertEquals(envioActualizado.getCantidadProducto(), result.getCantidadProducto());

        verify(envioRepository).findByNumeroEnvio("ABC123");
        verify(restTemplate).getForObject("http://localhost:8081/usuario/15", UsuarioDTO.class);
        verify(restTemplate).getForObject("http://localhost:8085/producto/25", ProductoDTO.class);
        verify(envioRepository).save(any(Envio.class));
    }
    @Test
    void testUpdateByNumeroEnvio_EnvioNoExiste() {
        when(envioRepository.findByNumeroEnvio("NO_EXISTE")).thenReturn(List.of());

        Envio envioUpdate = new Envio();
        envioUpdate.setIdCliente(10);
        envioUpdate.setIdProducto(20);
        envioUpdate.setCantidadProducto(2);

        Envio resultado = envioService.updateByNumeroEnvio("NO_EXISTE", envioUpdate);

        assertNull(resultado);

    }


    @Test
    void testUpdateByNumeroEnvio_UsuarioNoEncontrado() {
        Envio envioExistente = new Envio();
        envioExistente.setNumeroEnvio("ABC123");

        when(envioRepository.findByNumeroEnvio("ABC123")).thenReturn(List.of(envioExistente));

        Envio envioUpdate = new Envio();
        envioUpdate.setIdCliente(10);
        envioUpdate.setIdProducto(20);
        envioUpdate.setCantidadProducto(2);

        when(restTemplate.getForObject("http://localhost:8081/usuario/10", UsuarioDTO.class))
            .thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            envioService.updateByNumeroEnvio("ABC123", envioUpdate);
        });

        assertTrue(ex.getMessage().contains("Usuario no encontrado"));

    }
    
        @Test
    void testUpdateByNumeroEnvio_ProductoNoEncontrado() {
        Envio envio = new Envio();
        envio.setNumeroEnvio("ABC123");
        envio.setCantidadProducto(2);
        envio.setIdCliente(10);
        envio.setIdProducto(20);

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEstado(true);
        usuario.setPermiso(5);
        usuario.setId(10);
        usuario.setDireccion("Calle Test 123");

        when(envioRepository.findByNumeroEnvio("ABC123")).thenReturn(List.of(envio));
        when(restTemplate.getForObject("http://localhost:8081/usuario/10", UsuarioDTO.class)).thenReturn(usuario);
        when(restTemplate.getForObject("http://localhost:8085/producto/20", ProductoDTO.class)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> envioService.updateByNumeroEnvio("ABC123", envio));

        assertTrue(exception.getMessage().contains("Producto no encontrado"));

        verify(envioRepository).findByNumeroEnvio("ABC123");
        verify(restTemplate).getForObject("http://localhost:8081/usuario/10", UsuarioDTO.class);
        verify(restTemplate).getForObject("http://localhost:8085/producto/20", ProductoDTO.class);
        verifyNoMoreInteractions(restTemplate);
    }

        @Test
    void testUpdateByNumeroEnvio_StockInsuficiente() {
        Envio envio = new Envio();
        envio.setNumeroEnvio("ABC123");
        envio.setCantidadProducto(5);
        envio.setIdCliente(10);
        envio.setIdProducto(20);

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEstado(true);
        usuario.setPermiso(5);
        usuario.setId(10);
        usuario.setDireccion("Calle Test 123");

        ProductoDTO producto = new ProductoDTO();
        producto.setStock(3);
        producto.setValor(100);
        producto.setId(20);

        when(envioRepository.findByNumeroEnvio("ABC123")).thenReturn(List.of(envio));
        when(restTemplate.getForObject("http://localhost:8081/usuario/10", UsuarioDTO.class)).thenReturn(usuario);
        when(restTemplate.getForObject("http://localhost:8085/producto/20", ProductoDTO.class)).thenReturn(producto);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> envioService.updateByNumeroEnvio("ABC123", envio));

        assertTrue(exception.getMessage().contains("Stock insuficiente"));

        verify(envioRepository).findByNumeroEnvio("ABC123");
        verify(restTemplate).getForObject("http://localhost:8081/usuario/10", UsuarioDTO.class);
        verify(restTemplate).getForObject("http://localhost:8085/producto/20", ProductoDTO.class);
        verifyNoMoreInteractions(restTemplate);
    }


    @Test
    void testDeleteByIdEnvio() {
        Envio envio = new Envio();
        envio.setIdEnvio(1L);
        envio.setNumeroEnvio("12345");
        envio.setDireccionDestino("123 Main St");
        envio.setFechaEnvio("2023-10-01");
        envio.setFechaEntrega("2023-10-10");
        envio.setEstado(Estado.EN_PROCESO);
        when(envioRepository.findByIdEnvio(1L)).thenReturn(envio);
        envioService.deleteByIdEnvio(1L);
        verify(envioRepository).findByIdEnvio(1L);
        verify(envioRepository).delete(envio);
    }
    @Test
    void testDeleteByIdEnvio_NotFound() {
    Long idEnvio = 1L;

    when(envioRepository.findByIdEnvio(idEnvio)).thenReturn(null);

    RuntimeException ex = assertThrows(RuntimeException.class, () -> envioService.deleteByIdEnvio(idEnvio));
    System.out.println("Mensaje excepción: " + ex.getMessage());

    assertTrue(ex.getMessage().contains("Envío no encontrado"));


    verify(envioRepository).findByIdEnvio(idEnvio);
}


}



