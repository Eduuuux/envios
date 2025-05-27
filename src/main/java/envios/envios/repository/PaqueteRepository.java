package envios.envios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import envios.envios.model.Paquete;

@Repository
public interface PaqueteRepository extends JpaRepository<Paquete, Integer> {

    Paquete findById(int id);

    Paquete save(Paquete paquete);

    List<Paquete> findAll();


}
