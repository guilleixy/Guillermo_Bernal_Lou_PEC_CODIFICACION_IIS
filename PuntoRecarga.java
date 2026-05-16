public class PuntoRecarga {

    private int id;
    private Corriente corriente;
    private int potencia;
    private int rodaja;
    private int contadorReservas;
    private SecuenciaReservas reservas;

    public void inicializar(int id) {
        this.id = id;
        this.contadorReservas = 0;
        this.reservas = new SecuenciaReservas();
        this.reservas.inicializar();
    }

    public void destruir() {
        reservas.destruir();
    }

    public void editar(Corriente corriente, int potencia, int rodaja) {
        this.corriente = corriente;
        this.potencia = potencia;
        this.rodaja = rodaja;
    }

    public Nivel nivel() {
        if (corriente == Corriente.AC && potencia >= 2 && potencia <= 4) {
            return Nivel.Nivel1;
        } else if (corriente == Corriente.AC && potencia >= 11 && potencia <= 22) {
            return Nivel.Nivel2;
        } else if (corriente == Corriente.DC && potencia >= 50 && potencia <= 300) {
            return Nivel.Nivel3;
        }
        return null;
    }

    public int rodaja() {
        return rodaja;
    }

    public int id() {
        return id;
    }

    public Reserva reservar(int dia, int mes, int ano, int hora, int minuto, int duracion) {
        int duracionAjustada = ajustarDuracion(duracion);
        Reserva r = new Reserva();
        r.inicializar(dia, mes, ano, hora, minuto, duracionAjustada, id, contadorReservas + 1);
        boolean insertada = reservas.insertar(r, false);
        if (insertada) {
            contadorReservas++;
            return r;
        }
        return null;
    }

    public int ocupacionDia(int dia, int mes, int ano) {
        int totalMinutos = 0;
        int inicioDia = dia * 1440;
        int finDia = inicioDia + 1440;

        Reserva r = reservas.primera();
        while (r != null) {
            if (r.mes() == mes && r.ano() == ano) {
                int inicioReserva = r.dia() * 1440 + r.hora() * 60 + r.minuto();
                int finReserva = inicioReserva + r.duracion();
                int solapoInicio = Math.max(inicioReserva, inicioDia);
                int solapoFin = Math.min(finReserva, finDia);
                if (solapoFin > solapoInicio) {
                    totalMinutos += solapoFin - solapoInicio;
                }
            }
            if (reservas.enFin()) break;
            r = reservas.siguiente();
        }
        return totalMinutos;
    }

    public Reserva primeraReserva() {
        return reservas.primera();
    }

    public Reserva siguienteReserva() {
        return reservas.siguiente();
    }

    public boolean finReservas() {
        return reservas.enFin();
    }

    private int ajustarDuracion(int duracion) {
        if (duracion <= 0 || duracion < rodaja) {
            return rodaja;
        }
        int resto = duracion % rodaja;
        if (resto == 0) return duracion;
        return duracion + (rodaja - resto);
    }
}
