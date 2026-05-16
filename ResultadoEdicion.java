public class ResultadoEdicion {

    public static final int OK = 0;
    public static final int RECHAZADO_CONFIG_INVALIDA = 1;
    public static final int RECHAZADO_CAPACIDAD_EXCEDIDA = 2;

    private int codigo;
    private String mensaje;

    public ResultadoEdicion(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public boolean esOk() {
        return codigo == OK;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getMensaje() {
        return mensaje;
    }
}
