package envios.envios.model;

public enum Estado {
    EN_PROCESO,
    ENVIADO,
    RECIBIDO;

    public static Estado fromString(String estado) {
        return Estado.valueOf(estado.trim().toUpperCase());
    }
}