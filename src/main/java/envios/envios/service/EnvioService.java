package envios.envios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import envios.envios.DTO.ProductoDTO;
import envios.envios.DTO.UsuarioDTO;
import envios.envios.model.Envio;
import envios.envios.model.Estado;
import envios.envios.repository.EnvioRepository;

@Service
public class EnvioService {
    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private RestTemplate restTemplate;
    
    public Envio findByIdEnvio(Long idEnvio) {
        return envioRepository.findByIdEnvio(idEnvio);
    }

    public UsuarioDTO findUsuarioById(int id) {
        String url = "http://localhost:8081/usuario/" + id;
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }

    public ProductoDTO findProductoById(int id) {
        String url = "http://localhost:8085/producto/" + id;
        return restTemplate.getForObject(url, ProductoDTO.class);
    }

    
    public Envio save(Envio envio) {
        String url = "http://localhost:8081/usuario/" + envio.getIdCliente();
        UsuarioDTO usuario = restTemplate.getForObject(url, UsuarioDTO.class);
        String urlProducto = "http://localhost:8085/producto/" + envio.getIdProducto();
        ProductoDTO producto = restTemplate.getForObject(urlProducto, ProductoDTO.class);
        
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con id: " + envio.getIdCliente());
        }
        if (usuario.isEstado() == false) {
            throw new RuntimeException("Cuenta deshabilitada para el usuario con id: " + envio.getIdCliente());
        }
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado con id: " + envio.getIdProducto());
        }
        if (producto.getStock() < envio.getCantidadProducto()) {
            throw new RuntimeException("Stock insuficiente para la cantidad solicitada del producto con id: " + envio.getIdProducto());
        }
            
            envio.setIdCliente(usuario.getId());
            envio.setIdProducto(producto.getId());
            envio.setNumeroEnvio(new java.math.BigInteger(70, new java.security.SecureRandom()).toString().substring(0, 20)); // Genera un número de envío aleatorio
            envio.setFechaEnvio(java.time.LocalDate.now().plusDays(1).toString()); // Establece la fecha de envío actual
            envio.setFechaEntrega(java.time.LocalDate.now().plusDays(6).toString()); // Fecha de entrega actual +
            envio.setDireccionDestino(usuario.getDireccion()); // Usa la dirección del usuario
            envio.setEstado(Estado.EN_PROCESO);
            int valorTotal = producto.getValor() * envio.getCantidadProducto();
            envio.setValorTotal(valorTotal);
            producto.setStock(producto.getStock() - envio.getCantidadProducto());
        
        return envioRepository.save(envio);
    }
    
    
    
    
    public List<Envio> findAll() {
        return envioRepository.findAll();
    }

    public Envio findByNumeroEnvio(String numeroEnvio) {
        return envioRepository.findByNumeroEnvio(numeroEnvio).stream().findFirst().orElse(null);
    }
    
    public Envio updateByIdEnvio(Long idEnvio, Envio envio) {
        Envio existingEnvio = envioRepository.findByIdEnvio(idEnvio);
        if (existingEnvio != null) {
            String url = "http://localhost:8081/usuario/" + envio.getIdCliente();
            UsuarioDTO usuario = restTemplate.getForObject(url, UsuarioDTO.class);
            String urlProducto = "http://localhost:8085/producto/" + envio.getIdProducto();
            ProductoDTO producto = restTemplate.getForObject(urlProducto, ProductoDTO.class);

            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado con id: " + envio.getIdCliente());
            }
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado con id: " + envio.getIdProducto());
            }
            if (producto.getStock() < envio.getCantidadProducto()) {
                throw new RuntimeException("Stock insuficiente para la cantidad solicitada del producto con id: " + envio.getIdProducto());
            }

            existingEnvio.setIdCliente(usuario.getId());
            existingEnvio.setIdProducto(producto.getId());
            existingEnvio.setNumeroEnvio(new java.math.BigInteger(70, new java.security.SecureRandom()).toString().substring(0, 20));
            existingEnvio.setFechaEnvio(java.time.LocalDate.now().plusDays(1).toString());
            existingEnvio.setFechaEntrega(java.time.LocalDate.now().plusDays(6).toString());
            existingEnvio.setDireccionDestino(usuario.getDireccion());
            existingEnvio.setEstado(Estado.EN_PROCESO);
            int valorTotal = producto.getValor() * envio.getCantidadProducto();
            existingEnvio.setValorTotal(valorTotal);
            producto.setStock(producto.getStock() - envio.getCantidadProducto());
            existingEnvio.setCantidadProducto(envio.getCantidadProducto());

            return envioRepository.save(existingEnvio);
        }
        return null;
    }
        public Envio updateByNumeroEnvio(String numeroEnvio, Envio envio) {
        Envio existingEnvio = findByNumeroEnvio(numeroEnvio);

        if (existingEnvio != null) {
            String url = "http://localhost:8081/usuario/" + envio.getIdCliente();
            UsuarioDTO usuario = restTemplate.getForObject(url, UsuarioDTO.class);
            String urlProducto = "http://localhost:8085/producto/" + envio.getIdProducto();
            ProductoDTO producto = restTemplate.getForObject(urlProducto, ProductoDTO.class);

            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado con id: " + envio.getIdCliente());
            }
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado con id: " + envio.getIdProducto());
            }
            if (producto.getStock() < envio.getCantidadProducto()) {
                throw new RuntimeException("Stock insuficiente para la cantidad solicitada del producto con id: " + envio.getIdProducto());
            }

            existingEnvio.setIdCliente(usuario.getId());
            existingEnvio.setIdProducto(producto.getId());
            existingEnvio.setNumeroEnvio(new java.math.BigInteger(70, new java.security.SecureRandom()).toString().substring(0, 20));
            existingEnvio.setFechaEnvio(java.time.LocalDate.now().plusDays(1).toString());
            existingEnvio.setFechaEntrega(java.time.LocalDate.now().plusDays(6).toString());
            existingEnvio.setDireccionDestino(usuario.getDireccion());
            existingEnvio.setEstado(Estado.EN_PROCESO);
            int valorTotal = producto.getValor() * envio.getCantidadProducto();
            existingEnvio.setValorTotal(valorTotal);
            producto.setStock(producto.getStock() - envio.getCantidadProducto());
            existingEnvio.setCantidadProducto(envio.getCantidadProducto());

            return envioRepository.save(existingEnvio);
        }
        return null;
    }

    public void deleteByIdEnvio(Long idEnvio) {
        Envio envio = envioRepository.findByIdEnvio(idEnvio);
        if (envio == null) {
            throw new RuntimeException("Envío no encontrado con id: " + idEnvio);
        }
        envioRepository.delete(envio);
    }








}
