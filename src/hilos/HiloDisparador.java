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

    protected abstract String getNombre();

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                for (int t : transiciones) {
                    monitor.fireTransition(t);
                }
            }
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            // Optionally handle interruption or logging
        }
    }

    @Override
    public String toString() {
        return getNombre() + " " + id;
    }
}