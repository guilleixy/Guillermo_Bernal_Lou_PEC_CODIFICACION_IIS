public class Electrolinera {

    public static final int NUM_PUNTOS_RECARGA_MAX = 20;

    private int id;
    private String nombre;
    private int numPuntosNivel1;
    private int numPuntosNivel2;
    private int numPuntosNivel3;
    private TipoEstacion tipo;
    private Coordenadas ubicacion;
    private PuntoRecarga[] puntos;

    public void inicializar(int id) {
        this.id = id;
        this.puntos = new PuntoRecarga[NUM_PUNTOS_RECARGA_MAX + 1];
    }

    public void destruir() {
        for (int i = 1; i <= NUM_PUNTOS_RECARGA_MAX; i++) {
            if (puntos[i] != null) {
                puntos[i].destruir();
                puntos[i] = null;
            }
        }
    }

    public void editar(String nombre, int n1, int n2, int n3, TipoEstacion tipo, Coordenadas ubicacion) {
        this.nombre = nombre;
        this.numPuntosNivel1 = n1;
        this.numPuntosNivel2 = n2;
        this.numPuntosNivel3 = n3;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
    }

    public ResultadoEdicion editarPunto(int idPunto, Corriente corriente, int potencia, int rodaja) {
        Nivel nivelResultante = calcularNivel(corriente, potencia);
        if (nivelResultante == null) {
            return new ResultadoEdicion(ResultadoEdicion.RECHAZADO_CONFIG_INVALIDA,
                "Configuracion invalida: el par corriente/potencia no corresponde a ningun nivel valido.");
        }

        boolean esNuevo = (puntos[idPunto] == null);
        Nivel nivelAnterior = esNuevo ? null : puntos[idPunto].nivel();

        int puntosDelNivel = contarPuntosConfiguradosNivel(nivelResultante);
        if (esNuevo || nivelAnterior != nivelResultante) {
            int capacidadDeclarada = capacidadDeclaradaNivel(nivelResultante);
            if (puntosDelNivel >= capacidadDeclarada) {
                return new ResultadoEdicion(ResultadoEdicion.RECHAZADO_CAPACIDAD_EXCEDIDA,
                    "Capacidad declarada para el nivel excedida.");
            }
        }

        if (esNuevo) {
            puntos[idPunto] = new PuntoRecarga();
            puntos[idPunto].inicializar(idPunto);
        }
        puntos[idPunto].editar(corriente, potencia, rodaja);
        return new ResultadoEdicion(ResultadoEdicion.OK, "Punto editado correctamente.");
    }

    public int totalPuntosDeclarados() {
        return numPuntosNivel1 + numPuntosNivel2 + numPuntosNivel3;
    }

    public int contarPuntosConfiguradosNivel(Nivel nivel) {
        int cuenta = 0;
        for (int i = 1; i <= NUM_PUNTOS_RECARGA_MAX; i++) {
            if (puntos[i] != null && puntos[i].nivel() == nivel) {
                cuenta++;
            }
        }
        return cuenta;
    }

    public Reserva reservarPuntoRecarga(Nivel nivel, int dia, int mes, int ano,
                                         int hora, int minuto, int duracion) {
        for (int i = 1; i <= NUM_PUNTOS_RECARGA_MAX; i++) {
            if (puntos[i] != null && puntos[i].nivel() == nivel) {
                Reserva r = puntos[i].reservar(dia, mes, ano, hora, minuto, duracion);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }

    public ListadoReservas listarReservasMes(int mes, int ano) {
        ListadoReservas listado = new ListadoReservas();
        listado.inicializar();

        for (int i = 1; i <= NUM_PUNTOS_RECARGA_MAX; i++) {
            if (puntos[i] != null) {
                Nivel nivelPunto = puntos[i].nivel();
                Reserva r = puntos[i].primeraReserva();
                while (r != null) {
                    if (r.mes() == mes && r.ano() == ano) {
                        listado.insertar(r, nivelPunto);
                    }
                    if (puntos[i].finReservas()) break;
                    r = puntos[i].siguienteReserva();
                }
            }
        }
        return listado;
    }

    public String nombre() {
        return nombre;
    }

    public PuntoRecarga puntoRecarga(int idPunto) {
        if (idPunto < 1 || idPunto > NUM_PUNTOS_RECARGA_MAX) return null;
        return puntos[idPunto];
    }

    public int id() {
        return id;
    }

    private Nivel calcularNivel(Corriente corriente, int potencia) {
        if (corriente == Corriente.AC && potencia >= 2 && potencia <= 4) return Nivel.Nivel1;
        if (corriente == Corriente.AC && potencia >= 11 && potencia <= 22) return Nivel.Nivel2;
        if (corriente == Corriente.DC && potencia >= 50 && potencia <= 300) return Nivel.Nivel3;
        return null;
    }

    private int capacidadDeclaradaNivel(Nivel nivel) {
        if (nivel == Nivel.Nivel1) return numPuntosNivel1;
        if (nivel == Nivel.Nivel2) return numPuntosNivel2;
        if (nivel == Nivel.Nivel3) return numPuntosNivel3;
        return 0;
    }
}
