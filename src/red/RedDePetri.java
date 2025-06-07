package red;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RedDePetri {
    
    private Map<Integer, Plaza> plazas; // Mapa de plazas con su ID y cantidad de tokens
    private Map<Integer, Transicion> transiciones; // Mapa de transiciones con su ID y cantidad de tokens
    private Random random = new Random();
    private int contadorDisparadas; // Contador de transiciones disparadas

    public RedDePetri() {

        inicializar();
        contadorDisparadas = 0;


    }

    private void inicializar() {
        // Inicializar las plazas
        plazas = new HashMap<>();
        plazas.put(0, new Plaza(0, 3));
        plazas.put(1, new Plaza(1, 0));
        plazas.put(2, new Plaza(2, 1));
        plazas.put(3, new Plaza(3, 0));
        plazas.put(4, new Plaza(4, 0));
        plazas.put(5, new Plaza(5, 0));
        plazas.put(6, new Plaza(6, 1));
        plazas.put(7, new Plaza(7, 0));
        plazas.put(8, new Plaza(8, 0));
        plazas.put(9, new Plaza(9, 0));
        plazas.put(10, new Plaza(10, 0));
        plazas.put(11, new Plaza(11, 0));

        // Inicializar las transiciones
        transiciones = new HashMap<>();
        // T0: entradas = {0,2}, salidas = {1}
        transiciones.put(0, new Transicion(0, 0, new int[]{0, 2}, new int[]{1}));
        // T1: entradas = {1}, salidas = {2,3}
        transiciones.put(1, new Transicion(1, 1, new int[]{1}, new int[]{2, 3}));
        // T2: entradas = {3,6}, salidas = {4}
        transiciones.put(2, new Transicion(2, 0, new int[]{3, 6}, new int[]{4}));
        // T3: entradas = {4}, salidas = {5}
        transiciones.put(3, new Transicion(3, 1, new int[]{4}, new int[]{5}));
        // T4: entradas = {5}, salidas = {11,6}
        transiciones.put(4, new Transicion(4, 1, new int[]{5}, new int[]{11, 6}));
        // T5: entradas = {3,6}, salidas = {7}
        transiciones.put(5, new Transicion(5, 0, new int[]{3, 6}, new int[]{7}));
        // T6: entradas = {7}, salidas = {11,6}
        transiciones.put(6, new Transicion(6, 1, new int[]{7}, new int[]{11, 6}));
        // T7: entradas = {3,6}, salidas = {8}
        transiciones.put(7, new Transicion(7, 0, new int[]{3, 6}, new int[]{8}));
        // T8: entradas = {8}, salidas = {9}
        transiciones.put(8, new Transicion(8, 1, new int[]{8}, new int[]{9}));
        // T9: entradas = {9}, salidas = {10}
        transiciones.put(9, new Transicion(9, 1, new int[]{9}, new int[]{10}));
        // T10: entradas = {10}, salidas = {11,6}
        transiciones.put(10, new Transicion(10, 1, new int[]{10}, new int[]{11, 6}));
        // T11: entradas = {11}, salidas = {0}
        transiciones.put(11, new Transicion(11, 0, new int[]{11}, new int[]{0}));
    }

    public String getEstado() {
        StringBuilder estado = new StringBuilder();
        estado.append("(");
        for (int i = 0; i < plazas.size(); i++) {
            estado.append(plazas.get(i).getTokens()).append(" - ");
        }
        // borrar el último " - "
        if (estado.length() > 3) {
            estado.setLength(estado.length() - 3);
        }
        estado.append(")");

        return estado.toString();
    }

    public void dispararRandom(int cantidad) {
        if (cantidad <= 0) {
            System.out.println("Cantidad debe ser mayor que 0.");
            return;
        }
        //System.out.println("Se disparan " + cantidad + " transiciones al azar:");
        //System.out.println("Estado inicial de la red:\n" + getEstado());
        for (int i = 1; i <= cantidad; i++) {
            int transicionId = random.nextInt(transiciones.size());
            if(transiciones.get(transicionId).disparar(plazas)) {
                contadorDisparadas++;
                System.out.printf( "%4d : T" + transicionId + "\n" /* + getEstado() + "\n"*/, contadorDisparadas);
            } else {
                // Si la transición no se puede disparar, se elige otra
                i--; // Decrementar el contador para intentar de nuevo
            }
        }
        //System.out.println("Estado final de la red:\n" + getEstado());
    }

    public int getInvariables() {
        return transiciones.get(11).getContador();
    }

    @Override
    public String toString() {
        return "";
    }
    /**
     * Verifica las invariantes de plaza de la red de Petri.
     * 
     * @return true si todas las invariantes se cumplen, false en caso contrario.
     */
    public boolean invariantesPlazaCheck() {
        // Verifica si las plazas tienen la cantidad de tokens esperada
        /*
         * Tokens: M(P0) + M(P1) + M(P3) + M(P4) + M(P5) + M(P7) + M(P8) + M(P9) + M(P10) + M(P11) = 3
         * Acceso al buffer: M(P1) + M(P2) = 1
         * Unidad de procesamiento: M(P4) + M(P5) + M(P6) + M(P7) + M(P8) + M(P9) + M(P10) = 1
         */
        boolean boole = true;
        int invTokens = plazas.get(0).getTokens() + plazas.get(1).getTokens() + plazas.get(3).getTokens() +
                        plazas.get(4).getTokens() + plazas.get(5).getTokens() + plazas.get(7).getTokens() + 
                        plazas.get(8).getTokens() + plazas.get(9).getTokens() + plazas.get(10).getTokens() + 
                        plazas.get(11).getTokens();
        int invAccesoBuffer = plazas.get(1).getTokens() + plazas.get(2).getTokens();
        int invUnidadProcesamiento = plazas.get(4).getTokens() + plazas.get(5).getTokens() + plazas.get(6).getTokens() +                                     
                                     plazas.get(7).getTokens() + plazas.get(8).getTokens() + plazas.get(9).getTokens() +
                                     plazas.get(10).getTokens();

        if (invTokens != 3) {
            //System.out.println("Invariante de tokens no cumplida: " + invTokens + " tokens en total, se esperaba 3.");
            boole = false;
        }
        if (invAccesoBuffer != 1) {
            //System.out.println("Invariante de acceso al buffer no cumplida: " + invAccesoBuffer + " tokens, se esperaba 1.");
            boole = false;
        }
        if (invUnidadProcesamiento != 1) {
            //System.out.println("Invariante de unidad de procesamiento no cumplida: " + invUnidadProcesamiento + " tokens, se esperaba 1.");
            boole = false;
        }
        // if (boole) {
        //     System.out.println("Todas las invariantes de plaza se cumplen.");
        // }
        return boole;
    }
}
