import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class FrmJuego extends JFrame {

    private JPanel pnlJugador1, pnlJugador2;
    private JTabbedPane tpJugadores;
    private Jugador jugador1 = new Jugador();
    private Jugador jugador2 = new Jugador();

    public FrmJuego() {
        setSize(500, 300);
        setTitle("Juego de Cartas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton btnRepartir = new JButton("Repartir");
        btnRepartir.setBounds(10, 10, 100, 25);
        add(btnRepartir);

        JButton btnVerificar = new JButton("Verificar");
        btnVerificar.setBounds(120, 10, 100, 25);
        add(btnVerificar);

        tpJugadores = new JTabbedPane();
        tpJugadores.setBounds(10, 50, 460, 200);
        add(tpJugadores);

        pnlJugador1 = new JPanel();
        pnlJugador1.setBackground(new Color(0, 255, 0));
        pnlJugador1.setLayout(null);

        pnlJugador2 = new JPanel();
        pnlJugador2.setBackground(new Color(0, 255, 255));
        pnlJugador2.setLayout(null);

        tpJugadores.addTab("Martín Estrada Contreras", pnlJugador1);
        tpJugadores.addTab("Raúl Vidal", pnlJugador2);

        btnRepartir.addActionListener(e -> {
            repartir();
        });

        btnVerificar.addActionListener(e -> {
            verificar();
        });

    }

    private void repartir() {
        jugador1.repartir();
        jugador2.repartir();
        jugador1.mostrar(pnlJugador1);
        jugador2.mostrar(pnlJugador2);

    }

    private void verificar() {
        if (tpJugadores.getSelectedIndex() == 0) {
            jugador1.resetUsadas();
            String grupos = jugador1.getGrupos();
            String pintas = jugador1.analizarPintas();
            String sobrantes = jugador1.getSobrantes();
            String resultado = "GRUPOS:\n" + grupos + "\nESCALERAS:\n" + pintas + "\nSOBRANTES:\n" + sobrantes;
            JOptionPane.showMessageDialog(null, resultado);
        } else {
            jugador2.resetUsadas();
            String grupos = jugador2.getGrupos();
            String pintas = jugador2.analizarPintas();
            String sobrantes = jugador2.getSobrantes();
            String resultado = "GRUPOS:\n" + grupos + "\nPINTAS:\n" + pintas + "\nSOBRANTES:\n" + sobrantes;
            JOptionPane.showMessageDialog(null, resultado);
        }
    }

}
