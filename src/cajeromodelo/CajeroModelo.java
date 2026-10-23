package cajeromodelo;

import java.util.HashMap;

public class CajeroModelo {

    // HashMap para almacenar la denominación (clave) y la cantidad de billetes (valor)
    private HashMap<Integer, Integer> dispensador;

    public CajeroModelo() {
        dispensador = new HashMap<>();
        inicializarCajero();
    }

    /**
     * Carga inicial del cajero con 100 billetes de cada denominación
     */
    public void inicializarCajero() {
        dispensador.put(10000, 100);
        dispensador.put(20000, 100);
        dispensador.put(50000, 100);
        dispensador.put(100000, 100);
    }

    public HashMap<Integer, Integer> getDispensador() {
        return dispensador;
    }

    /**
     * Calcula el saldo total disponible en el cajero
     */
    public long obtenerSaldoTotal() {
        long total = 0;
        for (int den : dispensador.keySet()) {
            total += (long) den * dispensador.get(den);
        }
        return total;
    }

    /**
     * Valida y procesa el retiro aplicando las reglas del taller
     */
    public String procesarRetiro(int monto) {
        if (monto < 10000 || monto > 1000000) {
            return "ERROR: El retiro mínimo es $10.000 y el máximo es $1.000.000.";
        }

        if (monto % 10000 != 0) {
            return "ERROR: El monto ingresado debe ser múltiplo de $10.000.";
        }

        if (monto > obtenerSaldoTotal()) {
            return "ERROR: El cajero no cuenta con saldo suficiente para esta transacción.";
        }

        HashMap<Integer, Integer> entrega = calcularDistribucionBilletes(monto);

        if (entrega == null) {
            return "ERROR: No hay suficientes billetes de la denominación requerida.";
        }

        // Actualizar el HashMap del dispensador
        for (int den : entrega.keySet()) {
            int nuevaCantidad = dispensador.get(den) - entrega.get(den);
            dispensador.replace(den, nuevaCantidad);
        }

        // Reporte de entrega
        StringBuilder reporte = new StringBuilder();
        reporte.append("✓ RETIRO EXITOSO\n");
        reporte.append("Monto entregado: $").append(String.format("%,d", monto)).append("\n");
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

    /**
     * Algoritmo de desglose con reglas especiales ($50.000 y $100.000)
     */
    private HashMap<Integer, Integer> calcularDistribucionBilletes(int monto) {
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

        // Algoritmo para entregar el resto en la menor cantidad de billetes
        int[] denoms = {100000, 50000, 20000, 10000};
        for (int den : denoms) {
            if (restante >= den) {
                int requeridos = restante / den;
                int disponibles = copia.get(den);
                int aEntregar = Math.min(requeridos, disponibles);

                entrega.replace(den, entrega.get(den) + aEntregar);
                restante -= (aEntregar * den);
            }
        }

        return (restante == 0) ? entrega : null;
    }
}