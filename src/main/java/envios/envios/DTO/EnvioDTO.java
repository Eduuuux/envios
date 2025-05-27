package envios.envios.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvioDTO {
    private int idEnvio;
    private int numeroEnvio;
    private String direccionDestino;
    private String fechaEnvio;
    private String fechaEntrega;
    private String estado;
    private int paqueteId;
    private int clienteId;
}