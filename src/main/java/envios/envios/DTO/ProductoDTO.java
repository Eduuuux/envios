package envios.envios.DTO;

import lombok.Data;

@Data
public class ProductoDTO {
    private int id;
    private String nombreProducto;
    private String tipoProducto;
    private int valor;
    private int stock;
    private String resena;
}
