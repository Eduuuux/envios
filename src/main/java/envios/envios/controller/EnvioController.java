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
    @GetMapping("/{idEnvio}")
        public ResponseEntity<EntityModel<Envio>> getById(@PathVariable Long idEnvio) {
            Envio envio = envioService.findByIdEnvio(idEnvio);
            if (envio != null) {
                return ResponseEntity.ok(assembler.toModel(envio));
            } else {     
                return ResponseEntity.noContent().build();
            }
    }

    
    // public ResponseEntity<List<EnvioDTO>> getAll() {
    //     List<Envio> envios = envioService.findAll();
    //     if (!envios.isEmpty()) {
    //         List<EnvioDTO> envioDTOs = envios.stream().map(envio -> {
    //             EnvioDTO dto = new EnvioDTO();
    //             dto.setIdEnvio(envio.getIdEnvio());
    //             dto.setNumeroEnvio(envio.getNumeroEnvio());
    //             //dto.setDireccionDestino(envio.getCliente().getDireccionCliente());
    //             dto.setFechaEntrega(envio.getFechaEntrega().toString());
    //             dto.setEstado(envio.getEstado());
    //             //dto.setPaqueteId(envio.getPaquete().getId());
    //             //dto.setClienteId(envio.getCliente().getId());
    //             return dto;
    //         }).toList();
    //         return new ResponseEntity<>(envioDTOs, HttpStatus.OK);
    //     } else {
    //         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    //     }
    // }





    @GetMapping("/buscar/{numeroEnvio}")
    public ResponseEntity<Envio> getByNumeroEnvio(@PathVariable int numeroEnvio) {
        Envio envio = envioService.findByNumeroEnvio(numeroEnvio);
        if (envio != null) {
            return new ResponseEntity<>(envio, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}
    

    // CREAR CON PAQUETE Y USUARIO

    // @PostMapping("/crear")
    // public ResponseEntity<?> createEnvio(@RequestBody Envio envio) {
    //     Envio existingEnvio = envioService.findById(envio.getIdEnvio());
    //     if (existingEnvio == null) {
    //         try {
    //             if (envio.getPaquete() == null || envio.getPaquete().getId() == 0) {
    //                 return new ResponseEntity<>("El paquete asociado no es válido", HttpStatus.BAD_REQUEST);
    //             }
    //             if (envio.getCliente() != null && envio.getCliente().getId() != 0) {
    //                 Cliente cliente = clienteRepository.findById(envio.getCliente().getId());
    //                 if (cliente == null) {
    //                     return new ResponseEntity<>("El cliente no existe", HttpStatus.BAD_REQUEST);
    //                 }
    //                 envio.setCliente(cliente);
    //                 envio.setDireccionDestino(cliente.getDireccionCliente());
    //             } else {
    //                 return new ResponseEntity<>("El cliente asociado no es válido", HttpStatus.BAD_REQUEST);
    //             }

    //             Envio nuevoEnvio = envioService.save(envio);
    //             return new ResponseEntity<>(nuevoEnvio, HttpStatus.CREATED);
    //         } catch (Exception e) {
    //             return new ResponseEntity<>("Error al crear el envío: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    //         }
    //     } else {
    //         return new ResponseEntity<>("El envío ya existe", HttpStatus.CONFLICT);
    //     }
    // }


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
    // @PutMapping("/actualizar/id/{idEnvio}")
    // public ResponseEntity<?> updateEnvio(@PathVariable int idEnvio, @RequestBody Envio envio) {
    //     Envio existingEnvio = envioService.findById(idEnvio);
    //     if (existingEnvio != null) {
    //         try {
    //             if (envio.getPaquete() == null || envio.getPaquete().getId() == 0) {
    //                 return new ResponseEntity<>("El paquete asociado no es válido", HttpStatus.BAD_REQUEST);
    //             }
    //             if (envio.getCliente() != null && envio.getCliente().getId() != 0) {
    //                 Cliente cliente = clienteRepository.findById(envio.getCliente().getId());
    //                 if (cliente == null) {
    //                     return new ResponseEntity<>("El cliente no existe", HttpStatus.BAD_REQUEST);
    //                 }
    //                 envio.setCliente(cliente);
    //                 envio.setDireccionDestino(cliente.getDireccionCliente());
    //             } else {
    //                 return new ResponseEntity<>("El cliente asociado no es válido", HttpStatus.BAD_REQUEST);
    //             }

    //             Envio updatedEnvio = envioService.save(envio);
    //             return new ResponseEntity<>(updatedEnvio, HttpStatus.OK);
    //         } catch (Exception e) {
    //             return new ResponseEntity<>("Error al actualizar el envío: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    //         }
    //     } else {
    //         return new ResponseEntity<>("El envío no existe", HttpStatus.NOT_FOUND);
    //     }
    // }
    @PutMapping("/actualizar/id/{idEnvio}")
    public ResponseEntity<?> updateEnvio(@PathVariable Long idEnvio, @RequestBody Envio envio) {
    Envio existingEnvio = envioService.findByIdEnvio(idEnvio);
    if (existingEnvio != null) {
        try {
            existingEnvio.setFechaEnvio(envio.getFechaEnvio());
            existingEnvio.setFechaEntrega(envio.getFechaEntrega());
            existingEnvio.setEstado(envio.getEstado());
            existingEnvio.setNumeroEnvio(envio.getNumeroEnvio());
            existingEnvio.setDireccionDestino(envio.getDireccionDestino());
            Envio updatedEnvio = envioService.save(existingEnvio);
            return new ResponseEntity<>(updatedEnvio, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el envío: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } else {
        return new ResponseEntity<>("El envío no existe", HttpStatus.NOT_FOUND);
    }
}

    // @PutMapping("/actualizar/{numeroEnvio}")
    //     public ResponseEntity<?> updateEnvioPorNumero(@PathVariable int numeroEnvio, @RequestBody Envio envio) {
    //         Envio existingEnvio = envioService.findByNumeroEnvio(numeroEnvio);
    //         if (existingEnvio != null) {
    //             try {
    //                 if (envio.getPaquete() == null || envio.getPaquete().getId() == 0) {
    //                     return new ResponseEntity<>("El paquete asociado no es válido", HttpStatus.BAD_REQUEST);
    //                 }
    //                 if (envio.getCliente() != null && envio.getCliente().getId() != 0) {
    //                     Cliente cliente = clienteRepository.findById(envio.getCliente().getId());
    //                     if (cliente == null) {
    //                         return new ResponseEntity<>("El cliente no existe", HttpStatus.BAD_REQUEST);
    //                     }
    //                     existingEnvio.setCliente(cliente);
    //                     existingEnvio.setDireccionDestino(cliente.getDireccionCliente());
    //                 } else {
    //                     return new ResponseEntity<>("El cliente asociado no es válido", HttpStatus.BAD_REQUEST);
    //                 }

    //                 existingEnvio.setFechaEnvio(envio.getFechaEnvio());
    //                 existingEnvio.setFechaEntrega(envio.getFechaEntrega());
    //                 existingEnvio.setEstado(envio.getEstado());
    //                 existingEnvio.setPaquete(envio.getPaquete());

    //                 Envio updatedEnvio = envioService.save(existingEnvio);
    //                 return new ResponseEntity<>(updatedEnvio, HttpStatus.OK);
    //             } catch (Exception e) {
    //                 return new ResponseEntity<>("Error al actualizar el envío: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    //             }
    //         } else {
    //             return new ResponseEntity<>("El envío no existe", HttpStatus.NOT_FOUND);
    //         }
    //     }

    @PutMapping("/actualizar/{numeroEnvio}")
    public ResponseEntity<?> updateEnvioPorNumero(@PathVariable int numeroEnvio, @RequestBody Envio envio) {
        Envio existingEnvio = envioService.findByNumeroEnvio(numeroEnvio);
        if (existingEnvio != null) {
            try {
                existingEnvio.setFechaEnvio(envio.getFechaEnvio());
                existingEnvio.setFechaEntrega(envio.getFechaEntrega());
                existingEnvio.setEstado(envio.getEstado());
                existingEnvio.setNumeroEnvio(envio.getNumeroEnvio());
                existingEnvio.setDireccionDestino(envio.getDireccionDestino());
                Envio updatedEnvio = envioService.save(existingEnvio);
                return new ResponseEntity<>(updatedEnvio, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error al actualizar el envío: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("El envío no existe", HttpStatus.NOT_FOUND);
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
