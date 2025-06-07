package red;

import java.util.Map;

public class Transicion {
    private int id;
    private int delay;
    private int[] plazasEntrada;
    private int[] plazasSalida;
    private int contador;

    public Transicion(int id, int delay, int[] plazasEntrada, int[] plazasSalida) {
        this.id = id;
        this.delay = delay;
        this.plazasEntrada = plazasEntrada;
        this.plazasSalida = plazasSalida;
    }


    /**
     * Dispara la transición si es posible. Para que una transición se dispare, todas las plazas de entrada deben tener al menos un token.
     * Si la transición se dispara, se eliminan los tokens de las plazas de entrada y se añaden a las plazas de salida.
     * @param Map<Integer, Plaza> plazas
     * @return boolean true si la transición se disparó correctamente, false si no se pudo disparar.
     */
    public boolean disparar(Map<Integer, Plaza> plazas) {
        if(isSencivilizada(plazas)) {
            for (int entrada : plazasEntrada) {
                plazas.get(entrada).removeToken();
            }
            for (int salida : plazasSalida) {
                plazas.get(salida).addToken();
            }
            
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

    public int getDelay() {
        return delay;
    }

    public int getContador() {
        return contador;
    }
}
