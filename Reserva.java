public class Reserva {

    private int dia;
    private int mes;
    private int ano;
    private int hora;
    private int minuto;
    private int duracion;
    private int idPunto;
    private int numeroSecuencial;

    public void inicializar(int dia, int mes, int ano, int hora, int minuto,
                            int duracion, int idPunto, int numeroSecuencial) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.hora = hora;
        this.minuto = minuto;
        this.duracion = duracion;
        this.idPunto = idPunto;
        this.numeroSecuencial = numeroSecuencial;
    }

    public String identificador(Nivel nivel) {
        int numNivel = 0;
        if (nivel == Nivel.Nivel1) numNivel = 1;
        else if (nivel == Nivel.Nivel2) numNivel = 2;
        else if (nivel == Nivel.Nivel3) numNivel = 3;
        // Devuelve la cadena formateada
        return String.format("N%d-%d-%04d-%02d-%d", numNivel, idPunto, numeroSecuencial, mes, ano);
    }

    public Instante instanteInicio() {
        return new Instante(dia, mes, ano, hora, minuto);
    }

    public Instante instanteFin() {
        return instanteInicio().masMinutos(duracion);
    }

    public int dia() { return dia; }
    public int mes() { return mes; }
    public int ano() { return ano; }
    public int hora() { return hora; }
    public int minuto() { return minuto; }
    public int duracion() { return duracion; }
    public int idPunto() { return idPunto; }
        public int numeroSecuencial() { return numeroSecuencial; }
}
