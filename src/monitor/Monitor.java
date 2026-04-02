package monitor;

import red.RedDePetri;
import red.Transicion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor implements MonitorInterface {
    private final RedDePetri red;
    private final InterfazPolitica politica;
    private final Lock lock = new ReentrantLock(true); // true for fairness
    private final Condition[] condiciones;
    
    /**
     * Constructor del Monitor.
     * 
     * @param red La red de Petri a monitorear
     * @param politica La política de selección de transiciones a utilizar
     */
    public Monitor(RedDePetri red, InterfazPolitica politica) {
        this.red = red;
        this.politica = politica;
        int numTransiciones = red.getTransiciones().size();
        this.condiciones = new Condition[numTransiciones];
        for (int i = 0; i < numTransiciones; i++) {
            condiciones[i] = lock.newCondition();
        }
    }

    /**
     * Detecta qué transiciones están en competencia con la transición especificada.
     * Dos transiciones están en competencia si comparten al menos una plaza de entrada
     * y ambas están sensibilizadas.
     * 
     * @param transicion ID de la transición
     * @return Lista de IDs de transiciones en competencia (incluyendo la original)
     */
    private List<Integer> detectarConflictos(int transicion) {
        List<Integer> conflictos = new ArrayList<>();
        Transicion t = red.getTransiciones().get(transicion);
        
        if (t == null) {
            return conflictos;
        }
        
        int[] plazasTransicion = t.obtenerPlazasEntrada();
        
        // Iterar todas las transiciones para encontrar conflictos
        for (Transicion otroT : red.getTransiciones().values()) {
            // Verificar si la otra transición comparte plaza entrada y está sensibilizada
            int[] plazasOtro = otroT.obtenerPlazasEntrada();
            
            boolean compartenPlaza = false;
            for (int pT : plazasTransicion) {
                for (int pOtro : plazasOtro) {
                    if (pT == pOtro) {
                        compartenPlaza = true;
                        break;
                    }
                }
                if (compartenPlaza) break;
            }
            
            // Si comparten plaza y está sensibilizada, es una competidora
            if (compartenPlaza && red.isSensitized(otroT.getId())) {
                conflictos.add(otroT.getId());
            }
        }
        
        return conflictos;
    }

    @Override
    public boolean fireTransition(int transicion) {
        lock.lock();
        try {
            // Esperar un tiempo limitado a que la transición esté disponible
            long tiempoEspera = 100; // ms
            long tiempoFinal = System.currentTimeMillis() + tiempoEspera;
            
            while (!red.isSensitized(transicion)) {
                long tiempoRestante = tiempoFinal - System.currentTimeMillis();
                if (tiempoRestante <= 0) {
                    // Timeout alcanzado
                    return false;
                }
                condiciones[transicion].await(tiempoRestante, java.util.concurrent.TimeUnit.MILLISECONDS);
            }
            
            // Detectar conflictos mientras se mantiene el lock
            List<Integer> conflictos = detectarConflictos(transicion);
            int transicionSeleccionada = transicion;
            
            if (conflictos.size() > 1) {
                // Hay competencia, usar política para seleccionar
                transicionSeleccionada = politica.seleccionarTransicion(conflictos, red);
            }
            
            // Disparar la transición seleccionada manteniendo el lock
            boolean disparada = red.dispararT(transicionSeleccionada);

            // Si la transición se disparó, notificar a todos los hilos
            if (disparada) {
                for (Condition c : condiciones) {
                    c.signalAll();
                }
            }
            
            return disparada;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock();
        }
    }
}