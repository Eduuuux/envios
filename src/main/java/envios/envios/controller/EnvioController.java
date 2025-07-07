package envios.envios.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import envios.envios.assemblers.EnvioAssembler;
import envios.envios.model.Envio;
import envios.envios.service.EnvioService;




@RestController
@RequestMapping("api/envio")
public class EnvioController {
    @Autowired
    private EnvioService envioService;

    @Autowired
    private EnvioAssembler assembler;

    @GetMapping()
        public ResponseEntity<CollectionModel<EntityModel<Envio>>> getAll() {
        List<Envio> envios = envioService.findAll();
        if (!envios.isEmpty()) {
            List<EntityModel<Envio>> envioModels = envios.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<Envio>> collectionModel = CollectionModel.of(envioModels);
            return ResponseEntity.ok(collectionModel);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/buscar/{numeroEnvio}")
    public ResponseEntity<Envio> getByNumeroEnvio(@PathVariable String numeroEnvio) {
        Envio envio = envioService.findByNumeroEnvio(numeroEnvio);
        if (envio != null) {
            return new ResponseEntity<>(envio, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}

    @PostMapping("/crear")
    public ResponseEntity<?> createEnvio(@RequestBody Envio envio) {
        Envio existingEnvio = envioService.findByIdEnvio(envio.getIdEnvio());
        if (existingEnvio == null) {
            try {
                Envio nuevoEnvio = envioService.save(envio);
                return new ResponseEntity<>(nuevoEnvio, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>("Error al crear el envío: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("El envío ya existe", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/actualizar/{numeroEnvio}")
    public ResponseEntity<?> updateEnvioPorNumero(@PathVariable String numeroEnvio, @RequestBody Envio envio) {
        try {
            Envio updatedEnvio = envioService.updateByNumeroEnvio(numeroEnvio, envio);

            if (updatedEnvio != null) {
                return new ResponseEntity<>(updatedEnvio, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("El envío no existe", HttpStatus.NOT_FOUND);
            }

        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error al actualizar el envío: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminar/{idEnvio}")
        public ResponseEntity<?> deleteEnvio(@PathVariable Long idEnvio) {
            Envio existingEnvio = envioService.findByIdEnvio(idEnvio);
            if (existingEnvio != null) {
                try {
                    envioService.deleteByIdEnvio(idEnvio);
                    return new ResponseEntity<>("Envío eliminado con éxito", HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>("Error al eliminar el envío: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("El envío no existe", HttpStatus.NOT_FOUND);
            }
        }
    
}
