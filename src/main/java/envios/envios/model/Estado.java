package envios.envios.model;

public enum Estado {
    EN_PROCESO(1),
    ENVIADO(2),
    RECIBIDO(3),
    DESCONOCIDO(4);

    private final int descripcion;

    Estado(int descripcion) {
        this.descripcion = descripcion;
    }

    public int getDescripcion() {
        return descripcion;
    }
}
