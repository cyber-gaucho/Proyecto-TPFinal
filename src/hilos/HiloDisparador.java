package hilos;

import monitor.Monitor;

public abstract class HiloDisparador implements Runnable {
    private static int idCounter = 1;
    protected final int id;
    protected final Monitor monitor;
    protected final int[] transiciones;

    public HiloDisparador(Monitor monitor, int[] transiciones) {
        this.id = idCounter++;
        this.monitor = monitor;
        this.transiciones = transiciones;
    }

    protected abstract String obtenerNombre();

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                for (int t : transiciones) {
                    // Intentar disparar la transición
                    monitor.fireTransition(t);
                    // Continuamos a la siguiente transición sin importar si se disparó o no
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }
                }
            }
        } finally {
            System.out.println("Hilo " + obtenerNombre() + " detenido.");
        }
    }

    @Override
    public String toString() {
        return obtenerNombre() + " " + id;
    }
}