import java.util.Random;
import javax.swing.JPanel;

public class Jugador {

    private final int TOTAL_CARTAS = 10;
    private final int MARGEN_SUPERIOR = 10;
    private final int MARGEN_IZQUIERDO = 10;
    private final int DISTANCIA = 40;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private boolean[] usadas = new boolean[TOTAL_CARTAS];
    private Random r = new Random();

    public void repartir() {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int posicion = calcularPosicionInicial();

        for (Carta carta : cartas) {
            carta.mostrar(pnl, posicion, MARGEN_SUPERIOR);
            posicion -= DISTANCIA;
        }
        pnl.repaint();
    }

    private int calcularPosicionInicial() {
        return MARGEN_IZQUIERDO + DISTANCIA * (TOTAL_CARTAS - 1);
    }

    public String getGrupos() {
        int[] contadores = contarPorNombre();
        String resultado = "";

        for (int i = 0; i < contadores.length; i++) {
            if (contadores[i] >= 2) {
                resultado += construirGrupo(contadores[i], i);
                marcarUsadasPorNombre(i);
            }
        }

        if (resultado.isEmpty()) {
            return "No hay Grupos.";
        }
        return resultado;
    }

    private int[] contarPorNombre() {
        int[] contadores = new int[NombreCarta.values().length];
        for (Carta carta : cartas) {
            contadores[carta.getNombre().ordinal()]++;
        }
        return contadores;
    }

    private String construirGrupo(int cantidad, int nombreIndex) {
        return Grupo.values()[cantidad] + " de " + NombreCarta.values()[nombreIndex] + "\n";
    }

    private void marcarUsadasPorNombre(int nombreIndex) {
        for (int j = 0; j < cartas.length; j++) {
            if (cartas[j].getNombre().ordinal() == nombreIndex) {
                usadas[j] = true;
            }
        }
    }

    private int[] contarPorPinta() {
        int[] contadores = new int[Pinta.values().length];
        for (Carta carta : cartas) {
            contadores[carta.getPinta().ordinal()]++;
        }
        return contadores;
    }

    private void obtenerValoresEIndices(int pintaIndex, int[] valores, int[] indices) {
        int k = 0;
        for (int j = 0; j < cartas.length; j++) {
            if (cartas[j].getPinta().ordinal() == pintaIndex) {
                valores[k] = cartas[j].getNombre().ordinal() + 1;
                indices[k] = j;
                k++;
            }
        }
    }

    private void ordenar(int[] valores, int[] indices) {
        for (int a = 0; a < valores.length - 1; a++) {
            for (int b = 0; b < valores.length - 1 - a; b++) {
                if (valores[b] > valores[b + 1]) {
                    int temp = valores[b];
                    valores[b] = valores[b + 1];
                    valores[b + 1] = temp;

                    int tempI = indices[b];
                    indices[b] = indices[b + 1];
                    indices[b + 1] = tempI;
                }
            }
        }
    }

    private boolean casoEspecial(int[] valores) {
        boolean hayK = false, hayA = false, hay2 = false;

        for (int v : valores) {
            if (v == 13) hayK = true;
            if (v == 1) hayA = true;
            if (v == 2) hay2 = true;
        }

        return hayK && hayA && hay2;
    }

    public String analizarPintas() {
        int[] contadores = contarPorPinta();
        String resultado = "";

        for (int i = 0; i < contadores.length; i++) {

            int cantidad = contadores[i];

            if (cantidad >= 2) {

                int[] valores = new int[cantidad];
                int[] indices = new int[cantidad];

                obtenerValoresEIndices(i, valores, indices);
                ordenar(valores, indices);

                int consecutivas = 1, max = 1;
                int inicio = valores[0], fin = valores[0];
                int tempInicio = valores[0];

                for (int j = 0; j < valores.length - 1; j++) {

                    if (valores[j] + 1 == valores[j + 1]) {

                        consecutivas++;

                        if (consecutivas > max) {
                            max = consecutivas;
                            inicio = tempInicio;
                            fin = valores[j + 1];
                        }

                        if (consecutivas >= 2) {
                            usadas[indices[j]] = true;
                            usadas[indices[j + 1]] = true;
                        }

                    } else {
                        consecutivas = 1;
                        tempInicio = valores[j + 1];
                    }
                }

                if (casoEspecial(valores) && max < 3) {
                    for (int j = 0; j < valores.length; j++) {
                        if (valores[j] == 13 || valores[j] == 1 || valores[j] == 2) {
                            usadas[indices[j]] = true;
                        }
                    }
                    max = 3;
                    inicio = 13;
                    fin = 2;
                }

                if (max >= 2 && max < Grupo.values().length) {
                    String tipo = Grupo.values()[max].toString();
                    resultado += tipo + " de " + Pinta.values()[i]
                            + " de " + NombreCarta.values()[inicio - 1]
                            + " a " + NombreCarta.values()[fin - 1] + "\n";
                }
            }
        }

        if (resultado.isEmpty()) {
            return "No hay Escaleras\n";
        }
        return resultado;
    }

    public void resetUsadas() {
        for (int i = 0; i < usadas.length; i++) {
            usadas[i] = false;
        }
    }

    private int calcularPuntos(int valor) {
        if (valor == 1) return 10;
        if (valor >= 11) return 10;
        return valor;
    }

    public String getSobrantes() {
        String resultado = "";
        int total = 0;

        for (int i = 0; i < cartas.length; i++) {

            if (!usadas[i]) {

                int valor = cartas[i].getNombre().ordinal() + 1;
                int puntos = calcularPuntos(valor);

                total += puntos;

                resultado += cartas[i].getNombre()
                        + " de " + cartas[i].getPinta()
                        + " (" + puntos + " pts)\n";
            }
        }

        if (resultado.isEmpty()) {
            return "Total puntos sobrantes: " + total;
        }

        resultado += "Total puntos sobrantes: " + total;
        return resultado;
    }
}