package red;

import java.util.Map;
/**
 * Clase que representa una transición en una red de Petri. 
 * Una transición tiene referencias a las plazas de entrada y salida.
 * La transición puede dispararse si todas las plazas de entrada tienen al menos un token.
 * Cuando se dispara, se eliminan los tokens de las plazas de entrada y se añaden a las plazas de salida.
 * La transición también lleva un contador que indica cuántas veces se ha disparado.
 */
public class Transicion {
    private int id;
    private long delayMs;
    private int[] plazasEntrada;
    private int[] plazasSalida;
    private int contador;

    /**
     * Constructor de la clase Transicion.
     * Crea una transición con un identificador, un retraso en milisegundos,
     * y arrays de enteros que representan las plazas de entrada y salida.
     * 
     * @param id
     * @param delayMs
     * @param plazasEntrada
     * @param plazasSalida
     */
    public Transicion(int id, long delayMs, int[] plazasEntrada, int[] plazasSalida) {
        this.id = id;
        this.plazasEntrada = plazasEntrada;
        this.plazasSalida = plazasSalida;
        this.contador = 0;

        if (delayMs < 0) {
            this.delayMs = 0;
        } else {
            this.delayMs = delayMs;
        }
    }


    /**
     * Dispara la transición si es posible. Para que una transición se dispare, todas las plazas de entrada deben tener al menos un token.
     * Si la transición se dispara, se eliminan los tokens de las plazas de entrada y se añaden a las plazas de salida.
     * @param Map<Integer, Plaza> plazas
     * @return boolean true si la transición se disparó correctamente, false si no se pudo disparar.
     */
    public boolean disparar(Map<Integer, Plaza> plazas) {
        if(isSencivilizada(plazas)) {
            // Revisar si el delay va antes de mover los tokens.
            if (delayMs > 0) {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int entrada : plazasEntrada) {
                plazas.get(entrada).removeToken();
            }
            for (int salida : plazasSalida) {
                plazas.get(salida).addToken();
            }
            contador++;
            return true;
        } else {
            return false;
        }
    }

    private boolean isSencivilizada(Map<Integer, Plaza> plazas) {
        boolean isSencivilizada = true;
        for (int entrada : plazasEntrada) {
            if (!(plazas.get(entrada).getTokens() >  0)) {
                isSencivilizada = false;
                break;
            }
        }
        return isSencivilizada;
    }

    public int getId() {
        return id;
    }   

    public double getDelayMs() {
        return delayMs;
    }

    public int getContador() {
        return contador;
    }
}
