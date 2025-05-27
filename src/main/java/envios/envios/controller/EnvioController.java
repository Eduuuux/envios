package envios.envios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import envios.envios.DTO.EnvioDTO;
import envios.envios.model.Envio;

import envios.envios.service.EnvioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/envio")
public class EnvioController {
    @Autowired
    private EnvioService envioService;

    @GetMapping()
public ResponseEntity<List<EnvioDTO>> getAll() {
    List<Envio> envios = envioService.findAll();
    if (!envios.isEmpty()) {
        List<EnvioDTO> envioDTOs = envios.stream().map(envio -> {
            EnvioDTO dto = new EnvioDTO();
            dto.setIdEnvio(envio.getIdEnvio());
            dto.setNumeroEnvio(envio.getNumeroEnvio());
            dto.setDireccionDestino(envio.getDireccionDestino());
            dto.setFechaEnvio(envio.getFechaEnvio().toString());
            dto.setFechaEntrega(envio.getFechaEntrega().toString());
            dto.setEstado(envio.getEstado().toString());
            dto.setPaqueteId(envio.getPaquete().getId());
            dto.setClienteId(envio.getCliente().getId());
            return dto;
        }).toList();
        return new ResponseEntity<>(envioDTOs, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

    @GetMapping("/{idEnvio}")
    public ResponseEntity<Envio> getById(@PathVariable int idEnvio) {
    Envio envio = envioService.findById(idEnvio);
    if (envio != null) {
        return new ResponseEntity<>(envio, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> createEnvio(@RequestBody Envio envio) {
        Envio existingEnvio = envioService.findById(envio.getIdEnvio());
        if (existingEnvio == null) {
            try {
                if (envio.getPaquete() == null || envio.getPaquete().getId() == 0) {
                    return new ResponseEntity<>("El paquete asociado no es válido", HttpStatus.BAD_REQUEST);
                }
                Envio nuevoEnvio = envioService.save(envio);
                return new ResponseEntity<>(nuevoEnvio, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>("Error al crear el envío: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("El envío ya existe", HttpStatus.CONFLICT);
        }
    }
    
    

}
