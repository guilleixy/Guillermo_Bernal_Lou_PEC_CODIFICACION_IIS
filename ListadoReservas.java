public class ListadoReservas {

    private SecuenciaReservas nivel1;
    private SecuenciaReservas nivel2;
    private SecuenciaReservas nivel3;

    public void inicializar() {
        nivel1 = new SecuenciaReservas();
        nivel1.inicializar();
        nivel2 = new SecuenciaReservas();
        nivel2.inicializar();
        nivel3 = new SecuenciaReservas();
        nivel3.inicializar();
    }

    public void destruir() {
        nivel1.destruir();
        nivel2.destruir();
        nivel3.destruir();
    }

    public void insertar(Reserva reserva, Nivel nivel) {
        if (nivel == Nivel.Nivel1) nivel1.insertar(reserva, true);
        else if (nivel == Nivel.Nivel2) nivel2.insertar(reserva, true);
        else if (nivel == Nivel.Nivel3) nivel3.insertar(reserva, true);
    }

    public SecuenciaReservas getNivel1() { return nivel1; }
    public SecuenciaReservas getNivel2() { return nivel2; }
    public SecuenciaReservas getNivel3() { return nivel3; }
}
