public class GestorElectrolineras {

    public static final int NUM_ELECTROLINERAS_MAX = 10;

    private Electrolinera[] electrolineras;

    public void inicializar() {
        electrolineras = new Electrolinera[NUM_ELECTROLINERAS_MAX + 1];
    }

    public boolean existeElectrolinera(int idElectrolinera) {
        if (idElectrolinera < 1 || idElectrolinera > NUM_ELECTROLINERAS_MAX) return false;
        return electrolineras[idElectrolinera] != null;
    }

    public Electrolinera obtenerElectrolinera(int idElectrolinera) {
        return electrolineras[idElectrolinera];
    }

    public ResultadoEdicion crearOActualizarElectrolinera(int idElectrolinera, String nombre,
            int n1, int n2, int n3, TipoEstacion tipo, Coordenadas ubicacion) {
        if (n1 + n2 + n3 > 20) {
            return new ResultadoEdicion(ResultadoEdicion.RECHAZADO_CAPACIDAD_EXCEDIDA,
                "La suma de capacidades declaradas excede 20.");
        }
        if (!existeElectrolinera(idElectrolinera)) {
            electrolineras[idElectrolinera] = new Electrolinera();
            electrolineras[idElectrolinera].inicializar(idElectrolinera);
        }
        electrolineras[idElectrolinera].editar(nombre, n1, n2, n3, tipo, ubicacion);
        return new ResultadoEdicion(ResultadoEdicion.OK, "Electrolinera creada/modificada correctamente.");
    }

    public boolean darDeBajaElectrolinera(int idElectrolinera) {
        electrolineras[idElectrolinera].destruir();
        electrolineras[idElectrolinera] = null;
        return true;
    }

    public ResultadoEdicion editarPuntoRecarga(int idElectrolinera, int idPunto,
            Corriente corriente, int potencia, int rodaja) {
        return electrolineras[idElectrolinera].editarPunto(idPunto, corriente, potencia, rodaja);
    }

    public Reserva reservarPuntoRecarga(int idElectrolinera, Nivel nivel, int dia, int mes,
            int ano, int hora, int minuto, int duracion) {
        return electrolineras[idElectrolinera].reservarPuntoRecarga(
            nivel, dia, mes, ano, hora, minuto, duracion);
    }

    public ListadoReservas listarReservasElectrolinera(int idElectrolinera, int mes, int ano) {
        return electrolineras[idElectrolinera].listarReservasMes(mes, ano);
    }

    public EtiquetasOcupacion construirEtiquetasOcupacionMensual(int idElectrolinera,
            int idPunto, int mes, int ano) {
        PuntoRecarga punto = electrolineras[idElectrolinera].puntoRecarga(idPunto);
        EtiquetasOcupacion resultado = new EtiquetasOcupacion();
        resultado.inicializar();

        int diasDelMes = diasEnMes(mes, ano);
        int maxOcupacion = -1;
        int diaMayorOcupacion = 1;

        for (int dia = 1; dia <= diasDelMes; dia++) {
            int ocupacion = punto.ocupacionDia(dia, mes, ano);
            String etiqueta;
            if (ocupacion == 0) {
                etiqueta = "00";
            } else if (ocupacion < 360) {
                etiqueta = "Ba";
            } else if (ocupacion <= 720) {
                etiqueta = "Me";
            } else {
                etiqueta = "Al";
            }
            resultado.setEtiqueta(dia, etiqueta);
            if (ocupacion > maxOcupacion) {
                maxOcupacion = ocupacion;
                diaMayorOcupacion = dia;
            }
        }
        resultado.setDiaMayorOcupacion(diaMayorOcupacion);
        return resultado;
    }

    private int diasEnMes(int mes, int ano) {
        switch (mes) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12: return 31;
            case 4: case 6: case 9: case 11: return 30;
            case 2:
                boolean bisiesto = (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
                return bisiesto ? 29 : 28;
            default: return 30;
        }
    }
}
