package envios.envios.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import envios.envios.model.Cliente;
import envios.envios.repository.ClienteRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping()
        public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        if (!clientes.isEmpty()) {
            return new ResponseEntity<>(clientes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping("/{idCliente}")
    public ResponseEntity<Cliente> getById(@PathVariable int idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente);
        if (cliente != null) {
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {
        Cliente existingCliente = clienteRepository.findById(cliente.getId());
        if (existingCliente == null) {
            Cliente nuevoCliente = clienteRepository.save(cliente);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("El cliente ya existe", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("actualizar/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable int id, @RequestBody Cliente cliente) {
        Cliente existingCliente = clienteRepository.findById(id);
        if (existingCliente != null) {
            cliente.setId(id);
            Cliente updatedCliente = clienteRepository.save(cliente);
            return new ResponseEntity<>(updatedCliente, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("El cliente no existe", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("eliminar/{idCliente}")
    public ResponseEntity<?> deleteCliente(@PathVariable int idCliente) {
        Cliente existingCliente = clienteRepository.findById(idCliente);
        if (existingCliente != null) {
            clienteRepository.delete(existingCliente);
            return new ResponseEntity<>("Cliente eliminado con éxito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("El cliente no existe", HttpStatus.NOT_FOUND);
        }
    }
}
