package hilos;

import monitor.Monitor;

public class ComplejidadSimple extends HiloDisparador {
    public ComplejidadSimple(Monitor monitor) {
        super(monitor, new int[]{5, 6});
    }

    @Override
    protected String getNombre() {
        return "ComplejidadSimple";
    }
}