/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cajeromodelo;

import java.util.HashMap;

public class CajeroModelo {

    // HashMap para las denominaciones y cantidades de billetes
    private HashMap<Integer, Integer> dispensador;

    public CajeroModelo() {
        dispensador = new HashMap<>();
        inicializarCajero();
    }

    public void inicializarCajero() {
        dispensador.put(10000, 100);
        dispensador.put(20000, 100);
        dispensador.put(50000, 100);
        dispensador.put(100000, 100);
    }

    public HashMap<Integer, Integer> getDispensador() {
        return dispensador;
    }

    public long obtenerSaldoTotal() {
        long total = 0;
        for (int den : dispensador.keySet()) {
            total += (long) den * dispensador.get(den);
        }
        return total;
    }

    public String procesarRetiro(int monto) {
        if (monto < 10000 || monto > 1000000) {
            return "ERROR: El retiro mínimo es $10.000 y el máximo es $1.000.000.";
        }
        if (monto % 10000 != 0) {
            return "ERROR: El monto debe ser múltiplo de $10.000.";
        }
        if (monto > obtenerSaldoTotal()) {
            return "ERROR: El cajero no cuenta con saldo suficiente.";
        }

        HashMap<Integer, Integer> entrega = calcularDistribucion(monto);
        if (entrega == null) {
            return "ERROR: No hay suficiente combinación de billetes disponible.";
        }

        // Actualizar el HashMap principal
        for (int den : entrega.keySet()) {
            dispensador.replace(den, dispensador.get(den) - entrega.get(den));
        }

        StringBuilder reporte = new StringBuilder();
        reporte.append("✓ RETIRO EXITOSO\n");
        reporte.append("Monto: $").append(String.format("%,d", monto)).append("\n");
        reporte.append("Billetes entregados:\n");

        int[] orden = {100000, 50000, 20000, 10000};
        for (int den : orden) {
            if (entrega.containsKey(den) && entrega.get(den) > 0) {
                reporte.append(" • $").append(String.format("%,d", den))
                       .append(" : ").append(entrega.get(den)).append(" billete(s)\n");
            }
        }
        return reporte.toString();
    }

    private HashMap<Integer, Integer> calcularDistribucion(int monto) {
        HashMap<Integer, Integer> copia = new HashMap<>(dispensador);
        HashMap<Integer, Integer> entrega = new HashMap<>();
        entrega.put(100000, 0);
        entrega.put(50000, 0);
        entrega.put(20000, 0);
        entrega.put(10000, 0);

        int restante = monto;

        // Reglas especiales
        if (monto == 50000) {
            if (copia.get(20000) >= 2 && copia.get(10000) >= 1) {
                entrega.replace(20000, 2);
                entrega.replace(10000, 1);
                return entrega;
            }
        } else if (monto == 100000) {
            if (copia.get(50000) >= 1 && copia.get(20000) >= 2 && copia.get(10000) >= 1) {
                entrega.replace(50000, 1);
                entrega.replace(20000, 2);
                entrega.replace(10000, 1);
                return entrega;
            }
        } else if (monto > 100000) {
            if (copia.get(50000) >= 1 && copia.get(20000) >= 2 && copia.get(10000) >= 1) {
                entrega.replace(50000, 1);
                entrega.replace(20000, 2);
                entrega.replace(10000, 1);

                copia.replace(50000, copia.get(50000) - 1);
                copia.replace(20000, copia.get(20000) - 2);
                copia.replace(10000, copia.get(10000) - 1);

                restante -= 100000;
            }
        }

        int[] denoms = {100000, 50000, 20000, 10000};
        for (int den : denoms) {
            if (restante >= den) {
                int req = restante / den;
                int disp = copia.get(den);
                int tomar = Math.min(req, disp);

                entrega.replace(den, entrega.get(den) + tomar);
                restante -= (tomar * den);
            }
        }

        return (restante == 0) ? entrega : null;
    }
}