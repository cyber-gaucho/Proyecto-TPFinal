import monitor.Monitor;
import monitor.PoliticaAleatoria;
import red.RedDePetri;
import hilos.Entrada;
import hilos.Salida;
import hilos.ComplejidadSimple;
import hilos.ComplejidadMedia;
import hilos.ComplejidadAlta;

public class Main {
    public static void main(String[] args) {
        RedDePetri redDePetri = new RedDePetri();
        
        // Crear monitor con política aleatoria
        Monitor monitor = new Monitor(redDePetri, new PoliticaAleatoria());
        
        // Crear los 5 hilos disparadores
        Thread hiloEntrada = new Thread(new Entrada(monitor), "Entrada");
        Thread hiloSalida = new Thread(new Salida(monitor), "Salida");
        Thread hiloSimple = new Thread(new ComplejidadSimple(monitor), "ComplejidadSimple");
        Thread hiloMedia = new Thread(new ComplejidadMedia(monitor), "ComplejidadMedia");
        Thread hiloAlta = new Thread(new ComplejidadAlta(monitor), "ComplejidadAlta");
        
        // Array de todos los hilos para manejo centralizado
        Thread[] hilos = {hiloEntrada, hiloSalida, hiloSimple, hiloMedia, hiloAlta};
        
        // Iniciar todos los hilos
        System.out.println("Iniciando simulación con 5 hilos disparadores...");
        System.out.println("Meta: 200 disparos de salida (T11)");
        System.out.println("Política: " + monitor.getClass().getSimpleName() + "\n");
        
        for (Thread hilo : hilos) {
            hilo.start();
        }
        
        // Thread principal monitorea el progreso
        while (redDePetri.getInvariables() < 200) {
            try {
                Thread.sleep(500); // Verificar cada 500ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        // Se alcanzó la meta, detener todos los hilos
        System.out.println("\n======================================");
        System.out.println("META ALCANZADA: " + redDePetri.getInvariables() + " invariables");
        System.out.println("Interrumpiendo hilos disparadores...");
        
        // Interrumpir todos los hilos
        for (Thread hilo : hilos) {
            hilo.interrupt();
        }
        
        // Esperar a que terminen con timeout
        final int TIMEOUT_MS = 5000;
        for (Thread hilo : hilos) {
            try {
                hilo.join(TIMEOUT_MS);
                if (hilo.isAlive()) {
                    System.out.println("Advertencia: " + hilo.getName() + " no terminó en el timeout");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Mostrar resultados finales
        System.out.println("\n======================================");
        System.out.println("SIMULACIÓN FINALIZADA");
        System.out.println("Invariables alcanzadas: " + redDePetri.getInvariables());
        System.out.println("    Modo simple: " + redDePetri.getContSimple());
        System.out.println("    Modo media: " + redDePetri.getContMedia());
        System.out.println("    Modo alta: " + redDePetri.getContAlta());
        System.out.println("Estado final de la red:\n" + redDePetri.getEstado());
        printArt();
    }

    static void printArt() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\"TP Final: Simulador de Red de Petri - Programación Concurrente 2025\"")
                .append("\n ___        _                                                      ___                ")
                .append("\n| __| __ _ (_) _ _  _ _   ___  ___ ___       __  ___  _ _         / __| ___  __  __ _ ")
                .append("\n| _| / _` || || '_|| ' \\ / -_)(_-/(_-/      / _|/ _ \\| ' \\       | (__ / _ \\/ _|/ _` |")
                .append("\n|_|  \\__/_||_||_|  |_||_|\\___|/__//__/      \\__|\\___/|_||_|       \\___|\\___/\\__|\\__/_|\n");
        System.out.println(sb.toString());
    }
}