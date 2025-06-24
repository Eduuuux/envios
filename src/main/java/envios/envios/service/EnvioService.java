package envios.envios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import envios.envios.model.Envio;
import envios.envios.repository.EnvioRepository;

@Service
public class EnvioService {
    @Autowired
    private EnvioRepository envioRepository;

    public Envio findByIdEnvio(Long idEnvio) {
        return envioRepository.findByIdEnvio(idEnvio);
    }
    public Envio save(Envio envio) {
        return envioRepository.save(envio);
    }
    public List<Envio> findAll() {
        return envioRepository.findAll();
    }

    public Envio findByNumeroEnvio(int numeroEnvio) {
        return envioRepository.findByNumeroEnvio(numeroEnvio).stream().findFirst().orElse(null);
    }
    
    public Envio updateByIdEnvio(Long idEnvio, Envio envio) {
        Envio existingEnvio = envioRepository.findByIdEnvio(idEnvio);
        if (existingEnvio != null) {
            if (envio.getDireccionDestino() != null) {
                existingEnvio.setDireccionDestino(envio.getDireccionDestino());
            }
            if (envio.getFechaEnvio() != null) {
                existingEnvio.setFechaEnvio(envio.getFechaEnvio());
            }
            if (envio.getFechaEntrega() != null) {
                existingEnvio.setFechaEntrega(envio.getFechaEntrega());
            }
            if (envio.getEstado() != null) {
                existingEnvio.setEstado(envio.getEstado());
            }


            return envioRepository.save(existingEnvio);
        }
        return null;
    }

    public void deleteByIdEnvio(Long idEnvio) {
        Envio envio = envioRepository.findByIdEnvio(idEnvio);
        if (envio != null) {
            envioRepository.delete(envio);
        }
    }








}
