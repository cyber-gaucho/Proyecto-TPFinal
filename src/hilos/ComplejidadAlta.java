package hilos;

import monitor.Monitor;

public class ComplejidadAlta extends HiloDisparador {
    public ComplejidadAlta(Monitor monitor) {
        super(monitor, new int[]{7, 8, 9, 10});
    }

    @Override
    protected String getNombre() {
        return "ComplejidadAlta";
    }
}