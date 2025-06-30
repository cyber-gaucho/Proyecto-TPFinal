package hilos;

import monitor.Monitor;

public class Salida extends HiloDisparador {
    public Salida(Monitor monitor) {
        super(monitor, new int[]{11});
    }

    @Override
    protected String getNombre() {
        return "Salida";
    }
}