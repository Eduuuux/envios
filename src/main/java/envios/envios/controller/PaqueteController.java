package envios.envios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import envios.envios.model.Paquete;
import envios.envios.service.PaqueteService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/paquete")
public class PaqueteController {
    @Autowired
    private PaqueteService paqueteService;

    @GetMapping()
    public ResponseEntity<List<Paquete>> getAll() {
        List<Paquete> paquetes = paqueteService.findAll();
        if (!paquetes.isEmpty()) {
            return new ResponseEntity<>(paquetes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping("/{idPaquete}")
        public ResponseEntity<Paquete> getById(@PathVariable int idPaquete) {
        Paquete paquete = paqueteService.findById(idPaquete);
        if (paquete != null) {
            return new ResponseEntity<>(paquete, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> createPaquete(@RequestBody Paquete paquete) {
    Paquete existingPaquete = paqueteService.findById(paquete.getId());
    if (existingPaquete == null) {
        Paquete nuevoPaquete = paqueteService.save(paquete);
        return new ResponseEntity<>(nuevoPaquete, HttpStatus.CREATED);
    } else {
        return new ResponseEntity<>("El paquete ya existe", HttpStatus.CONFLICT);
    }
    }

    @PutMapping("actualizar/{id}")
    public ResponseEntity<?> updatePaquete(@PathVariable int id, @RequestBody Paquete paquete) {
        Paquete existingPaquete = paqueteService.findById(id);
        if (existingPaquete != null) {
            paquete.setId(id);
            Paquete updatedPaquete = paqueteService.save(paquete);
            return new ResponseEntity<>(updatedPaquete, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("El paquete no existe", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("eliminar/{idPaquete}")
    public ResponseEntity<?> deletePaquete(@PathVariable int idPaquete) {
        Paquete existingPaquete = paqueteService.findById(idPaquete);
        if (existingPaquete != null) {
            paqueteService.deleteById(idPaquete);
            return new ResponseEntity<>("Paquete eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("El paquete no existe", HttpStatus.NOT_FOUND);
        }
    }
        


    
}
