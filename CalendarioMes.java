public class CalendarioMes {

    private int margenIzquierdo;

    private static final String[] NOMBRES_MESES = {
        "", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    public void inicializar() {
        margenIzquierdo = 0;
    }

    public void establecerMargen(int margen) {
        margenIzquierdo = margen;
    }

    public void imprimir(int mes, int ano, String[] etiquetas) {
        String margen = espacios(margenIzquierdo);
        System.out.println(margen + NOMBRES_MESES[mes] + " " + ano);
        System.out.println(margen + " L   M   X   J   V   S   D");

        int diasDelMes = diasEnMes(mes, ano);
        int diaSemanaInicio = diaDeLaSemana(1, mes, ano);

        System.out.print(margen);
        for (int i = 0; i < diaSemanaInicio; i++) {
            System.out.print("    ");
        }

        int columna = diaSemanaInicio;
        for (int dia = 1; dia <= diasDelMes; dia++) {
            System.out.print(" " + etiquetas[dia - 1] + " ");
            columna++;
            if (columna == 7 && dia < diasDelMes) {
                System.out.println();
                System.out.print(margen);
                columna = 0;
            } else if (dia < diasDelMes) {
                System.out.print(" ");
            }
        }
        System.out.println();
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

    // Implementación de la fórmula de Zeller
    private int diaDeLaSemana(int dia, int mes, int ano) {
        if (mes < 3) {
            mes += 12;
            ano--;
        }
        int k = ano % 100;
        int j = ano / 100;
        int h = (dia + (13 * (mes + 1)) / 5 + k + k / 4 + j / 4 + 5 * j) % 7;
        // h ahora sería Sabado,1=Domingo,2=Lunes,...,6=Viernes. Convertimos a convertir a 0=Lunes
        int diaSemana = (h + 5) % 7;
        return diaSemana;
    }

    private String espacios(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(' ');
        return sb.toString();
    }
}
