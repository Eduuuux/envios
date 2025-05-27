package envios.envios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import envios.envios.model.Cliente;
import envios.envios.repository.ClienteRepository;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente findById(int id) {
        return clienteRepository.findById(id);
    }
    
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente updateByIdCliente(int id, Cliente cliente) {
        Cliente existingCliente = clienteRepository.findById(id);
        if (existingCliente != null) {
            if (cliente.getNombreCliente() != null) {
                existingCliente.setNombreCliente(cliente.getNombreCliente());
            }
            if (cliente.getApellidoCliente() != null) {
                existingCliente.setApellidoCliente(cliente.getApellidoCliente());
            }
            if (cliente.getDireccionCliente() != null) {
                existingCliente.setDireccionCliente(cliente.getDireccionCliente());
            }
            if (cliente.getTelefonoCliente() != null) {
                existingCliente.setTelefonoCliente(cliente.getTelefonoCliente());
            }
            if (cliente.getCorreoCliente() != null) {
                existingCliente.setCorreoCliente(cliente.getCorreoCliente());
            }
            return clienteRepository.save(existingCliente);
        }
        return null;
    }

    public Cliente deleteById(int id) {
        Cliente cliente = clienteRepository.findById(id);
        if (cliente != null) {
            clienteRepository.delete(cliente);
            return cliente;
        }
        return null;
    }
}

