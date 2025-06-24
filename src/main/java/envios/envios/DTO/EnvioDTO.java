package envios.envios.DTO;

import envios.envios.model.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvioDTO {
    private Long idEnvio;
    private int numeroEnvio;
    private String direccionDestino;
    private String fechaEnvio;
    private String fechaEntrega;
    private Estado estado;

}