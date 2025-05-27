package envios.envios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import envios.envios.model.Paquete;
import envios.envios.repository.PaqueteRepository;

@Service
public class PaqueteService {
    @Autowired
    private PaqueteRepository paqueteRepository;

    public Paquete findById(int id) {
        return paqueteRepository.findById(id);
    }

    public Paquete save(Paquete paquete) {
        return paqueteRepository.save(paquete);
    }

    public Paquete updateByIdPaquete(int id, Paquete paquete) {
        Paquete existingPaquete = paqueteRepository.findById(id);
        if (existingPaquete != null) {
            if(paquete.getProducto() != null) {
                existingPaquete.setProducto(paquete.getProducto());
            }
            if(paquete.getPeso() != 0) {
                existingPaquete.setPeso(paquete.getPeso());
            }
            return paqueteRepository.save(existingPaquete);
        }
        return null;
    }

    public Paquete deleteById(int id) {
        Paquete paquete = paqueteRepository.findById(id);
        if (paquete != null) {
            paqueteRepository.delete(paquete);
            return paquete;
        }
        return null;
    }

    

}
