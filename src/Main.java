import monitor.Monitor;

public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"Simulador de Red de Petri - TP Concurrente 2025\"")
        .append("\n ___        _                                                      ___                ")
        .append("\n| __| __ _ (_) _ _  _ _   ___  ___ ___       __  ___  _ _         / __| ___  __  __ _ ")
        .append("\n| _| / _` || || '_|| ' \\ / -_)(_-/(_-/      / _|/ _ \\| ' \\       | (__ / _ \\/ _|/ _` |")
        .append("\n|_|  \\__/_||_||_|  |_||_|\\___|/__//__/      \\__|\\___/|_||_|       \\___|\\___/\\__|\\__/_|\n");
        System.out.println(sb.toString());

        Monitor monitor = new Monitor();

        for (int i = 0; i < 10; i++) {
            monitor.fireTransition(i);
            try {
                Thread.sleep(1000); // Simula un retraso de 1 segundo entre transiciones
            } catch (InterruptedException e) {
                System.err.println("Error al dormir el hilo\n" + e.getStackTrace());
            }
        }
    }
}