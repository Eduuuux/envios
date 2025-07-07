package envios.envios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "envio")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEnvio;

    @Column(name = "numero_envio", nullable = false, unique = true, length = 20)
    private String numeroEnvio;

    @Column(name = "direccion_destino", nullable = false, length = 252)
    private String direccionDestino;

    @Column(name = "fecha_envio", nullable = false)
    private String fechaEnvio;

    @Column(name = "fecha_entrega", nullable = true)
    private String fechaEntrega;

    @Column(name = "estado", nullable = false)
    private Estado estado;

    @Column(name = "cant_producto", nullable = false)
    private int cantidadProducto;

    @Column(name = "valorTotal", nullable = false)
    private int valorTotal;

    @Column(name = "id_cliente", nullable = false)
    private int idCliente;

    @Column(name = "id_producto", nullable = false)
    private int idProducto;
}
