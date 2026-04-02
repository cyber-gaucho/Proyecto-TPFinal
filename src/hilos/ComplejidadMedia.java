package hilos;

import monitor.Monitor;

public class ComplejidadMedia extends HiloDisparador {
    public ComplejidadMedia(Monitor monitor) {
        super(monitor, new int[]{2, 3, 4});
    }

    @Override
    protected String getNombre() {
        return "ComplejidadMedia";
    }
}