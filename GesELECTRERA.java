import java.util.Scanner;

public class GesELECTRERA {

    private GestorElectrolineras gestor;
    private Scanner teclado;

    public GesELECTRERA() {
        gestor = new GestorElectrolineras();
        gestor.inicializar();
        teclado = new Scanner(System.in);
    }

    public void ejecutar() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            String opcion = teclado.nextLine().trim().toUpperCase();
            switch (opcion) {
                case "E": editarElectrolinera(); break;
                case "P": editarPuntoRecarga(); break;
                case "R": reservarPuntoRecarga(); break;
                case "L": listarReservasElectrolinera(); break;
                case "M": listarServicioMensualPunto(); break;
                case "S": continuar = false; System.out.println("Hasta luego."); break;
                default: System.out.println("Opcion no valida. Introduzca E, P, R, L, M o S."); break;
            }
        }
    }

    // -------------------------------------------------------------------------
    // UC-01: Editar electrolinera
    // -------------------------------------------------------------------------
    private void editarElectrolinera() {
        System.out.print("Identificador de electrolinera (1-10): ");
        int id = leerEntero();
        if (id < 1 || id > 10) {
            System.out.println("ERROR: Identificador fuera de rango.");
            return;
        }

        System.out.print("Nombre (1-20 caracteres): ");
        String nombre = teclado.nextLine().trim();
        if (nombre.isEmpty() || nombre.length() > 20) {
            System.out.println("ERROR: El nombre debe tener entre 1 y 20 caracteres.");
            return;
        }

        System.out.print("Numero de puntos Nivel 1 (AC 2-4 kW): ");
        int n1 = leerEntero();
        System.out.print("Numero de puntos Nivel 2 (AC 11-22 kW): ");
        int n2 = leerEntero();
        System.out.print("Numero de puntos Nivel 3 (DC 50-300 kW): ");
        int n3 = leerEntero();

        if (n1 < 0 || n2 < 0 || n3 < 0) {
            System.out.println("ERROR: Las capacidades no pueden ser negativas.");
            return;
        }

        if (n1 == 0 && n2 == 0 && n3 == 0) {
            if (!gestor.existeElectrolinera(id)) {
                System.out.println("ERROR: La electrolinera " + id + " no existe para dar de baja.");
                return;
            }
            System.out.print("Confirma dar de baja la electrolinera " + id + " con todos sus puntos y reservas? (S/N): ");
            String conf = teclado.nextLine().trim().toUpperCase();
            if (!conf.equals("S")) {
                System.out.println("Operacion cancelada.");
                return;
            }
            gestor.darDeBajaElectrolinera(id);
            System.out.println("Electrolinera " + id + " dada de baja correctamente.");
            return;
        }

        System.out.print("Tipo de estacion (U=Urbana, R=Ruta, M=Mixta): ");
        String tipoStr = teclado.nextLine().trim().toUpperCase();
        TipoEstacion tipo;
        if (tipoStr.equals("U")) tipo = TipoEstacion.Urbana;
        else if (tipoStr.equals("R")) tipo = TipoEstacion.Ruta;
        else if (tipoStr.equals("M")) tipo = TipoEstacion.Mixta;
        else { System.out.println("ERROR: Tipo de estacion no valido."); return; }

        System.out.print("Latitud (WGS84): ");
        double latitud = leerDouble();
        System.out.print("Longitud (WGS84): ");
        double longitud = leerDouble();
        Coordenadas ubicacion = new Coordenadas(latitud, longitud);

        System.out.print("Confirma la operacion? (S/N): ");
        String conf = teclado.nextLine().trim().toUpperCase();
        if (!conf.equals("S")) {
            System.out.println("Operacion cancelada.");
            return;
        }

        ResultadoEdicion resultado = gestor.crearOActualizarElectrolinera(id, nombre, n1, n2, n3, tipo, ubicacion);
        if (resultado.esOk()) {
            System.out.println("Electrolinera " + id + " (" + nombre + ") creada/modificada correctamente.");
        } else {
            System.out.println("ERROR: " + resultado.getMensaje());
        }
    }

    // -------------------------------------------------------------------------
    // UC-02: Editar punto de recarga
    // -------------------------------------------------------------------------
    private void editarPuntoRecarga() {
        System.out.print("Identificador de electrolinera (1-10): ");
        int idE = leerEntero();
        if (!gestor.existeElectrolinera(idE)) {
            System.out.println("ERROR: La electrolinera " + idE + " no existe.");
            return;
        }

        System.out.print("Identificador de punto (1-20): ");
        int idP = leerEntero();
        if (idP < 1 || idP > 20) {
            System.out.println("ERROR: Identificador de punto fuera de rango.");
            return;
        }

        System.out.print("Corriente (AC/DC): ");
        String corrStr = teclado.nextLine().trim().toUpperCase();
        Corriente corriente;
        if (corrStr.equals("AC")) corriente = Corriente.AC;
        else if (corrStr.equals("DC")) corriente = Corriente.DC;
        else { System.out.println("ERROR: Tipo de corriente no valido."); return; }

        System.out.print("Potencia nominal (kW): ");
        int potencia = leerEntero();

        System.out.print("Rodaja minima (minutos): ");
        int rodaja = leerEntero();
        if (rodaja <= 0) {
            System.out.println("ERROR: La rodaja debe ser mayor que cero.");
            return;
        }

        System.out.print("Confirma la operacion? (S/N): ");
        String conf = teclado.nextLine().trim().toUpperCase();
        if (!conf.equals("S")) {
            System.out.println("Operacion cancelada.");
            return;
        }

        ResultadoEdicion resultado = gestor.editarPuntoRecarga(idE, idP, corriente, potencia, rodaja);
        if (resultado.esOk()) {
            System.out.println("Punto " + idP + " de la electrolinera " + idE + " editado correctamente.");
        } else {
            System.out.println("ERROR: " + resultado.getMensaje());
        }
    }

    // -------------------------------------------------------------------------
    // UC-03: Reservar punto de recarga
    // -------------------------------------------------------------------------
    private void reservarPuntoRecarga() {
        System.out.print("Identificador de electrolinera (1-10): ");
        int idE = leerEntero();
        if (!gestor.existeElectrolinera(idE)) {
            System.out.println("ERROR: La electrolinera " + idE + " no existe.");
            return;
        }

        System.out.print("Nivel solicitado (1, 2 o 3): ");
        int numNivel = leerEntero();
        Nivel nivel;
        if (numNivel == 1) nivel = Nivel.Nivel1;
        else if (numNivel == 2) nivel = Nivel.Nivel2;
        else if (numNivel == 3) nivel = Nivel.Nivel3;
        else { System.out.println("ERROR: Nivel no valido."); return; }

        System.out.print("Dia: ");
        int dia = leerEntero();
        System.out.print("Mes: ");
        int mes = leerEntero();
        System.out.print("Anio: ");
        int ano = leerEntero();
        System.out.print("Hora (0-23): ");
        int hora = leerEntero();
        System.out.print("Minuto (0-59): ");
        int minuto = leerEntero();
        System.out.print("Duracion solicitada (minutos): ");
        int duracion = leerEntero();

        System.out.print("Confirma la reserva? (S/N): ");
        String conf = teclado.nextLine().trim().toUpperCase();
        if (!conf.equals("S")) {
            System.out.println("Operacion cancelada.");
            return;
        }

        Reserva reserva = gestor.reservarPuntoRecarga(idE, nivel, dia, mes, ano, hora, minuto, duracion);
        if (reserva != null) {
            System.out.println("Reserva confirmada.");
            System.out.println("  Identificador : " + reserva.identificador(nivel));
            System.out.println("  Fecha         : " + reserva.dia() + "/" + reserva.mes() + "/" + reserva.ano());
            System.out.println("  Hora inicio   : " + String.format("%02d:%02d", reserva.hora(), reserva.minuto()));
            System.out.println("  Duracion      : " + reserva.duracion() + " minutos");
        } else {
            System.out.println("La reserva no se puede realizar: no hay disponibilidad para el nivel " + numNivel + ".");
        }
    }

    // -------------------------------------------------------------------------
    // UC-04: Listar reservas de electrolinera
    // -------------------------------------------------------------------------
    private void listarReservasElectrolinera() {
        System.out.print("Identificador de electrolinera (1-10): ");
        int idE = leerEntero();
        if (!gestor.existeElectrolinera(idE)) {
            System.out.println("ERROR: La electrolinera " + idE + " no existe.");
            return;
        }

        System.out.print("Mes: ");
        int mes = leerEntero();
        System.out.print("Anio: ");
        int ano = leerEntero();

        ListadoReservas listado = gestor.listarReservasElectrolinera(idE, mes, ano);

        System.out.println("--- Reservas de electrolinera " + idE + " ("
            + gestor.obtenerElectrolinera(idE).nombre() + ") - "
            + String.format("%02d", mes) + "/" + ano + " ---");

        boolean hayReservas = false;
        hayReservas |= imprimirSecuenciaReservas("Nivel 1", listado.getNivel1(), Nivel.Nivel1);
        hayReservas |= imprimirSecuenciaReservas("Nivel 2", listado.getNivel2(), Nivel.Nivel2);
        hayReservas |= imprimirSecuenciaReservas("Nivel 3", listado.getNivel3(), Nivel.Nivel3);

        if (!hayReservas) {
            System.out.println("No hay reservas para este mes.");
        }
        listado.destruir();
    }

    private boolean imprimirSecuenciaReservas(String etiquetaNivel, SecuenciaReservas sec, Nivel nivel) {
        if (sec.estaVacia()) return false;
        System.out.println("  [ " + etiquetaNivel + " ]");
        Reserva r = sec.primera();
        while (r != null) {
            System.out.println("    " + r.identificador(nivel)
                + "  " + r.dia() + "/" + String.format("%02d", r.mes()) + "/" + r.ano()
                + "  " + String.format("%02d:%02d", r.hora(), r.minuto())
                + "  " + r.duracion() + " min");
            if (sec.enFin()) break;
            r = sec.siguiente();
        }
        return true;
    }

    // -------------------------------------------------------------------------
    // UC-05: Listar servicio mensual de punto
    // -------------------------------------------------------------------------
    private void listarServicioMensualPunto() {
        System.out.print("Identificador de electrolinera (1-10): ");
        int idE = leerEntero();
        if (!gestor.existeElectrolinera(idE)) {
            System.out.println("ERROR: La electrolinera " + idE + " no existe.");
            return;
        }

        System.out.print("Identificador de punto (1-20): ");
        int idP = leerEntero();
        if (gestor.obtenerElectrolinera(idE).puntoRecarga(idP) == null) {
            System.out.println("ERROR: El punto " + idP + " no esta configurado en la electrolinera " + idE + ".");
            return;
        }

        CalendarioMes calendario = new CalendarioMes();
        calendario.inicializar();
        calendario.establecerMargen(2);

        boolean seguir = true;
        while (seguir) {
            System.out.print("Mes: ");
            int mes = leerEntero();
            System.out.print("Anio: ");
            int ano = leerEntero();

            EtiquetasOcupacion etiquetas = gestor.construirEtiquetasOcupacionMensual(idE, idP, mes, ano);
            calendario.imprimir(mes, ano, etiquetas.getEtiquetas());
            System.out.println("  Dia de maxima ocupacion: " + etiquetas.getDiaMayorOcupacion());

            System.out.print("Consultar otro mes? (S/N): ");
            String resp = teclado.nextLine().trim().toUpperCase();
            seguir = resp.equals("S");
        }
    }

    // -------------------------------------------------------------------------
    // Auxiliares de lectura
    // -------------------------------------------------------------------------
    private void mostrarMenu() {
        System.out.println();
        System.out.println("=== GesELECTRERA ===");
        System.out.println("  E - Editar electrolinera");
        System.out.println("  P - Editar punto de recarga");
        System.out.println("  R - Reservar punto de recarga");
        System.out.println("  L - Listar reservas de electrolinera");
        System.out.println("  M - Listar servicio mensual de punto");
        System.out.println("  S - Salir");
        System.out.print("Opcion: ");
    }

    private int leerEntero() {
        String linea = teclado.nextLine().trim();
        try {
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double leerDouble() {
        String linea = teclado.nextLine().trim();
        try {
            return Double.parseDouble(linea);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static void main(String[] args) {
        GesELECTRERA app = new GesELECTRERA();
        app.ejecutar();
    }
}
