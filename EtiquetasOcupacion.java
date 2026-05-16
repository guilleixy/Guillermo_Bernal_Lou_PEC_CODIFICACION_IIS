public class EtiquetasOcupacion {

    private String[] etiquetas;
    private int diaMayorOcupacion;

    public void inicializar() {
        etiquetas = new String[31];
        for (int i = 0; i < 31; i++) {
            etiquetas[i] = "00";
        }
        diaMayorOcupacion = 0;
    }

    public void setEtiqueta(int dia, String etiqueta) {
        etiquetas[dia - 1] = etiqueta;
    }

    public String getEtiqueta(int dia) {
        return etiquetas[dia - 1];
    }

    public String[] getEtiquetas() {
        return etiquetas;
    }

    public void setDiaMayorOcupacion(int dia) {
        diaMayorOcupacion = dia;
    }

    public int getDiaMayorOcupacion() {
        return diaMayorOcupacion;
    }
}
