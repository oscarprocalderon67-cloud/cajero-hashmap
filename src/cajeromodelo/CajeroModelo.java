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
}