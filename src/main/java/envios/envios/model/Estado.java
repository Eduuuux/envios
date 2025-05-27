package envios.envios.model;

public enum Estado {
    EN_PROCESO,
    ENVIADO,
    RECIBIDO,
    DESCONOCIDO; 

    public static Estado fromString(String value) {
        try {
            return Estado.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Estado.DESCONOCIDO;
        }
}
}