import monitor.Monitor;
import red.RedDePetri;

public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"Simulador de Red de Petri - TP Concurrente 2025\"")
        .append("\n ___        _                                                      ___                ")
        .append("\n| __| __ _ (_) _ _  _ _   ___  ___ ___       __  ___  _ _         / __| ___  __  __ _ ")
        .append("\n| _| / _` || || '_|| ' \\ / -_)(_-/(_-/      / _|/ _ \\| ' \\       | (__ / _ \\/ _|/ _` |")
        .append("\n|_|  \\__/_||_||_|  |_||_|\\___|/__//__/      \\__|\\___/|_||_|       \\___|\\___/\\__|\\__/_|\n");
        System.out.println(sb.toString());

       RedDePetri redDePetri = new RedDePetri();
       
       redDePetri.dispararRandom(15);

    }
}

/*
 * TO DO: 
 * - Crear clases:
 *   - Plaza: int id, int tokens
 *   - Transicion: int id, int[] plazasEntrada, int[] plazasSalida, int delay, bool isSencivilizada(), void disparar() (no soporta peso de arco por ahora)
 *   - Sublcase TransicionDelay: hereda de Transicion, agrega delay y Override disparar() 
 *   - RedDePetri: vector o mapa para almacenar plazas y transiciones
 *   - Marcado (?) sería clase o un vector de enteros?
 * 
 * - Consultar:
 *   - Cómo se debe definir la estructura de la red de Petrie?
 *     - Método 1: hardcodeada, con un constructor que inicialice las plazas y transiciones.
 *     - Método 2: configurable, con un archivo de configuración: JSON, XML, etc.   
 *   - Cantidad de hilos = 3 ta bien?
 *   - Cómo se debe configurar la simulación? Politica, cantidad de invariantes, marcado inicial, delay, etc.
 *   - -
 * 
 * - -
 * 
 */