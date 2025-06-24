package envios.envios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import envios.envios.model.Envio;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {

    Envio findByIdEnvio(Long idEnvio);

    Envio save(Envio envio);

    List<Envio> findAll();

    List<Envio> findByNumeroEnvio(int numeroEnvio);

}
