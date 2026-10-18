package cajeromodelo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class InterfazCajero extends JFrame {

    private JTextField pantalla;
    private JTextArea areaResultado;
    private JTable tablaBilletes;
    private DefaultTableModel modeloTabla;

    private CajeroModelo modelo;

    public InterfazCajero() {
        modelo = new CajeroModelo();

        setTitle("Cajero Electrónico - Taller HashMap");
        setSize(480, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Pantalla
        JPanel panelPantalla = new JPanel(new BorderLayout(5, 5));
        panelPantalla.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        pantalla = new JTextField();
        pantalla.setFont(new Font("Monospaced", Font.BOLD, 22));
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        pantalla.setEditable(false);
        pantalla.setBackground(Color.BLACK);
        pantalla.setForeground(Color.GREEN);

        areaResultado = new JTextArea(5, 30);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultado.setEditable(false);
        JScrollPane scrollResultado = new JScrollPane(areaResultado);

        panelPantalla.add(pantalla, BorderLayout.NORTH);
        panelPantalla.add(scrollResultado, BorderLayout.CENTER);
        add(panelPantalla, BorderLayout.NORTH);

        // Teclado
        JPanel panelTeclado = new JPanel(new GridLayout(4, 3, 5, 5));
        String[] botones = {
            "7", "8", "9",
            "4", "5", "6",
            "1", "2", "3",
            "←", "0", "C"
        };

        for (String txt : botones) {
            JButton btn = new JButton(txt);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.addActionListener(new ManejadorTeclado());
            panelTeclado.add(btn);
        }

        JButton btnRetirar = new JButton("RETIRAR");
        btnRetirar.setFont(new Font("Arial", Font.BOLD, 16));
        btnRetirar.setBackground(new Color(40, 167, 69));
        btnRetirar.setForeground(Color.WHITE);
        btnRetirar.addActionListener(e -> ejecutarRetiro());

        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panelCentro.add(panelTeclado, BorderLayout.CENTER);
        panelCentro.add(btnRetirar, BorderLayout.SOUTH);
        add(panelCentro, BorderLayout.CENTER);

        // Tabla
        String[] columnas = {"Denominación", "Cantidad", "Total en Dinero"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaBilletes = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaBilletes);
        scrollTabla.setPreferredSize(new Dimension(450, 130));
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Billetes Disponibles en Cajero"));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        panelInferior.add(scrollTabla, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        actualizarTabla();
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        HashMap<Integer, Integer> dispensador = modelo.getDispensador();
        int[] orden = {10000, 20000, 50000, 100000};

        for (int den : orden) {
            if (dispensador.containsKey(den)) {
                int cant = dispensador.get(den);
                modeloTabla.addRow(new Object[]{
                    "$" + String.format("%,d", den),
                    cant,
                    "$" + String.format("%,d", (long) den * cant)
                });
            }
        }
    }

    private class ManejadorTeclado implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (cmd.equals("C")) {
                pantalla.setText("");
            } else if (cmd.equals("←")) {
                String txt = pantalla.getText();
                if (!txt.isEmpty()) {
                    pantalla.setText(txt.substring(0, txt.length() - 1));
                }
            } else {
                pantalla.setText(pantalla.getText() + cmd);
            }
        }
    }

    private void ejecutarRetiro() {
        String texto = pantalla.getText().trim();
        if (texto.isEmpty()) {
            areaResultado.setText("Error: Ingrese un monto a retirar.");
            return;
        }

        try {
            int monto = Integer.parseInt(texto);
            String respuesta = modelo.procesarRetiro(monto);
            areaResultado.setText(respuesta);

            if (respuesta.startsWith("✓")) {
                pantalla.setText("");
                actualizarTabla();
            }
        } catch (NumberFormatException ex) {
            areaResultado.setText("ERROR: Ingrese un valor válido.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazCajero().setVisible(true));
    }
}