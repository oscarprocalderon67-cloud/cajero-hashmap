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
     * Valida los límites y disponibilidad de dinero antes de procesar el retiro
     */
    public String validarRetiro(int monto) {
        if (monto < 10000 || monto > 1000000) {
            return "ERROR: El retiro mínimo es $10.000 y el máximo es $1.000.000.";
        }

        if (monto % 10000 != 0) {
            return "ERROR: El monto ingresado debe ser múltiplo de $10.000.";
        }

        if (monto > obtenerSaldoTotal()) {
            return "ERROR: El cajero no cuenta con saldo suficiente para esta transacción.";
        }

        return "OK";
    }
}