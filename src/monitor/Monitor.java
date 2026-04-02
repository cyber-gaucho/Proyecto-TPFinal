package monitor;

import red.RedDePetri;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor implements MonitorInterface {
    private final RedDePetri red;
    private final Lock lock = new ReentrantLock(true); // true for fairness
    // private final Lock lock = new ReentrantLock(); // Sin fairness
    // El uso de fairness puede ser útil para evitar starvation.
    // private final Condition cambio = lock.newCondition();
    private final Condition[] condiciones;
    
    public Monitor(RedDePetri red) {
        this.red = red;
        int numTransiciones = red.getTransiciones().size(); // Obtener el número de transiciones
        this.condiciones = new Condition[numTransiciones];
        for (int i = 0; i < numTransiciones; i++) {
            condiciones[i] = lock.newCondition();
        }
    }

    // @Override
    // public boolean fireTransition(int transition) {
    //     lock.lock();
    //     try {
    //         //System.out.println("Intentando disparar transición: " + transition);
    //         boolean disparada = red.dispararT(transition);
    //         if (disparada) {
    //             //System.out.println("Transición disparada: " + transition);
    //             cambio.signalAll(); // Notifica a otros hilos que puede haber cambios
    //         } else {
    //             System.out.println("No se pudo disparar la transición: " + transition);
    //         }
    //         return disparada;
    //     } finally {
    //         lock.unlock(); // Asegura que el lock se libere incluso si ocurre una excepción
    //     }
    // }

    // @Override
    // public boolean fireTransition(int transition) {
    //     lock.lock();
    //     try {
    //         boolean disparada = red.dispararT(transition);
    //         if (disparada) {
    //             condiciones[transition].signalAll(); // Notifica solo a los hilos esperando esta transición
    //         } else {
    //             System.out.println("No se pudo disparar la transición: " + transition);
    //         }
    //         return disparada;
    //     } finally {
    //         lock.unlock();
    //     }
    // }

    // @Override
    // public boolean fireTransition(int transition) {
    //     lock.lock();
    //     try {
    //         while (!red.isSensitized(transition)) {
    //             condiciones[transition].await();
    //         }
    //         boolean disparada = red.dispararT(transition);
    //         if (disparada) {
    //             condiciones[transition].signalAll();
    //         }
    //         return disparada;
    //     } catch (InterruptedException e) {
    //         Thread.currentThread().interrupt();
    //         return false;
    //     } finally {
    //         lock.unlock();
    //     }
    // }

    // @Override
    // public boolean fireTransition(int transition) {
        
    //     lock.lock(); 
    //     // Asegura que el lock se adquiera antes de cualquier operación
    //     // Si el lock no se puede adquirir, el hilo esperará hasta que esté disponible.
    //     // Es decir queda esperando en la puerta del monitor.
    //     try {
    //         while (!red.isSensitized(transition)) {
    //             condiciones[transition].await();
    //             // The lock associated with this Condition is atomically released 
    //             // and the current thread becomes disabled for thread scheduling purposes and lies dormant
    //         }
    //         boolean disparada = red.dispararT(transition);
    //         if (disparada) {
    //             for (int t = 0; t < condiciones.length; t++) {
    //                 // Notifica a todos los hilos que están esperando cualquier transición
    //                 // Esto es útil si hay múltiples transiciones que pueden ser disparadas
    //                 // después de que una se haya disparado.
    //                 condiciones[t].signalAll();
    //             }
    //         }
    //         return disparada;
    //     } catch (InterruptedException e) {
    //         Thread.currentThread().interrupt();
    //         return false;
    //     } finally {
    //         lock.unlock();
    //     }
    // }

    @Override
    public boolean fireTransition(int transition) {
        // Primero, el hilo intenta adquirir el lock para entrar al monitor
        lock.lock();
        try {
            while (!red.isSensitized(transition)) {
                condiciones[transition].await();
            }
            // En este punto, la transición está sensibilizada y el hilo puede intentar dispararla
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock();
        }

        // Disparar transición fuera del lock para permitir que otros hilos puedan intentar disparar otras transiciones
        boolean disparada = red.dispararT(transition);

        // Si la transición se disparó, notificar a todos los hilos que podrían estar esperando cualquier transición
        lock.lock();
        try {
            if (disparada) {
                for (Condition c : condiciones) {
                    c.signalAll();
                }
            }
        } finally {
            lock.unlock();
        }
        return disparada;
    }
    
}