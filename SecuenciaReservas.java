import java.util.ArrayList;

public class SecuenciaReservas {

    private ArrayList<Reserva> lista;
    private int posIterador;

    public void inicializar() {
        lista = new ArrayList<Reserva>();
        posIterador = 0;
    }

    public void destruir() {
        lista.clear();
    }

    public boolean insertar(Reserva reserva, boolean permitirSolapamiento) {
        if (!permitirSolapamiento) {
            if (seSolapa(reserva)) {
                return false;
            }
        }
        int pos = posicionOrdenada(reserva);
        lista.add(pos, reserva);
        return true;
    }

    public boolean estaVacia() {
        return lista.isEmpty();
    }

    public Reserva primera() {
        posIterador = 0;
        if (lista.isEmpty()) return null;
        return lista.get(posIterador);
    }

    public Reserva siguiente() {
        posIterador++;
        if (posIterador >= lista.size()) return null;
        return lista.get(posIterador);
    }

    public boolean enFin() {
        return posIterador >= lista.size() - 1;
    }

    private boolean seSolapa(Reserva nueva) {
        int inicioNueva = nueva.dia() * 1440 + nueva.hora() * 60 + nueva.minuto();
        int finNueva = inicioNueva + nueva.duracion();
        for (Reserva r : lista) {
            int inicioR = r.dia() * 1440 + r.hora() * 60 + r.minuto();
            int finR = inicioR + r.duracion();
            if (inicioNueva < finR && finNueva > inicioR) {
                return true;
            }
        }
        return false;
    }

    private int posicionOrdenada(Reserva reserva) {
        int inicioNueva = reserva.dia() * 1440 + reserva.hora() * 60 + reserva.minuto();
        for (int i = 0; i < lista.size(); i++) {
            Reserva r = lista.get(i);
            int inicioR = r.dia() * 1440 + r.hora() * 60 + r.minuto();
            if (inicioNueva < inicioR) {
                return i;
            }
        }
        return lista.size();
    }
}
