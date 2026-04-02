package hilos;

import monitor.Monitor;

public class Entrada extends HiloDisparador {
    public Entrada(Monitor monitor) {
        super(monitor, new int[]{0, 1});
    }

    @Override
    protected String getNombre() {
        return "Entrada";
    }
}
