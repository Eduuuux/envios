package envios.envios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombreCliente;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellidoCliente;

    @Column(name = "direccion", nullable = false, length = 255)
    private String direccionCliente;
    
    @Column(name = "telefono", nullable = false, length = 15)
    private String telefonoCliente;

    @Column(name = "correo", nullable = false, length = 100)
    private String correoCliente;

    
}
