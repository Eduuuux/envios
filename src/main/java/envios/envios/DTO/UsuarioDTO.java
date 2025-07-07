package envios.envios.DTO;

import lombok.Data;

@Data
public class UsuarioDTO {
    private int id;
    private String rut;
    private String nombre;
    private String p_apellido;
    private String s_apellido;
    private String usuario;
    private String contrasenna;
    private String correo;
    private String direccion;
    private int permiso; // 1 = cliente, 2 = vendedor, 3 = gerente, 4 = administrador
    private boolean estado = true; // true = activo, false = inactivo
    private String numero;
}
