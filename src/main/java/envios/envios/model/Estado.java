package envios.envios.model;

public enum Estado {
    EN_PROCESO("EN PROCESO"),
    ENVIADO("ENVIADO"),
    RECIBIDO("RECIBIDO"),
    DESCONOCIDO("DESCONOCIDO");

    private final String descripcion;

    Estado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
