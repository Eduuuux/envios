package envios.envios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import envios.envios.model.Envio;
import envios.envios.repository.EnvioRepository;

@Service
public class EnvioService {
    @Autowired
    private EnvioRepository envioRepository;

    public Envio findById(int id) {
        return envioRepository.findByIdEnvio(id);
    }
    public Envio save(Envio envio) {
        return envioRepository.save(envio);
    }

    public Envio findByNumeroEnvio(int numeroEnvio) {
        return envioRepository.findByNumeroEnvio(numeroEnvio).stream().findFirst().orElse(null);
    }
    
    public Envio updateByIdEnvio(int id, Envio envio) {
        Envio existingEnvio = envioRepository.findByIdEnvio(id);
        if (existingEnvio != null) {
            if (envio.getNumeroEnvio() != 0) {
                existingEnvio.setNumeroEnvio(envio.getNumeroEnvio());
            }
            if (envio.getFechaEnvio() != null) {
                existingEnvio.setFechaEnvio(envio.getFechaEnvio());
            }
            if (envio.getCliente() != null) {
                existingEnvio.setCliente(envio.getCliente());
            }
            if (envio.getPaquete() != null) {
                existingEnvio.setPaquete(envio.getPaquete());
            }
            return envioRepository.save(existingEnvio);
        }
        return null;
    }








}
