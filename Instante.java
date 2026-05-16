public class Instante {

    private int dia;
    private int mes;
    private int ano;
    private int hora;
    private int minuto;

    public Instante(int dia, int mes, int ano, int hora, int minuto) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.hora = hora;
        this.minuto = minuto;
    }

    public int dia() { return dia; }
    public int mes() { return mes; }
    public int ano() { return ano; }
    public int hora() { return hora; }
    public int minuto() { return minuto; }

    public int enMinutosTotales() {
        return dia * 1440 + hora * 60 + minuto;
    }

    public Instante masMinutos(int minutos) {
        int totalMinutos = hora * 60 + minuto + minutos;
        int diasExtra = totalMinutos / 1440;
        int restoMinutos = totalMinutos % 1440;
        int nuevaHora = restoMinutos / 60;
        int nuevoMinuto = restoMinutos % 60;
        int nuevoDia = dia + diasExtra;
        return new Instante(nuevoDia, mes, ano, nuevaHora, nuevoMinuto);
    }
}
