import javax.management.monitor.Monitor;

import red.RedDePetri;

public class Main {
    public static void main(String[] args) {
        RedDePetri redDePetri = new RedDePetri();
        // Monitor monitor = new Monitor(redDePetri);

        while(redDePetri.getInvariables() < 200){
            redDePetri.dispararRandom(1);
            if(!redDePetri.invariantesPlazaCheck()) {
                System.out.println("Error: Invariantes de plaza no cumplidos.");
                break;
            }
        }
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