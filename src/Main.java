import javax.management.monitor.Monitor;

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
       // Monitor monitor = new Monitor(redDePetri);

       while(redDePetri.getInvariables() < 200){

            redDePetri.dispararRandom(1);
            if(!redDePetri.invariantesPlazaCheck()) {
                System.out.println("Error: Invariantes de plaza no cumplidos.");
                break;
            }

       }
         System.out.println("Invariables alcanzadas: " + redDePetri.getInvariables());
         System.out.println("Simulación finalizada.");
         System.out.println("Estado final de la red:\n" + redDePetri.getEstado());

    }
}

/*
 * TO DO: 
 * - Crear clases:
 *   - [DONE] Plaza: int id, int tokens, void addToken(), void removeToken()
 *   - [DONE] Transicion: int id, int[] plazasEntrada, int[] plazasSalida, int delay, bool isSencivilizada(), void disparar(). (No soporta peso de arco por ahora)
 *   - Sublcase TransicionDelay: hereda de Transicion, agrega delay y Override disparar() 
 *   - [DONE] RedDePetri: vector o mapa para almacenar plazas y transiciones
 *   - Marcado (?) sería clase o un vector de enteros?
 * 
 * - Como hacer esperar al hilo desde la transición? (usar Thread.sleep(delay) en el método disparar de la transición).
 * 
 * - Consultar:
 *   - Cómo se debe definir la estructura de la red de Petri?
 *     - Método 1: hardcodeada, con un constructor que inicialice las plazas y transiciones.
 *     - Método 2: configurable, con un archivo de configuración: JSON, XML, etc.   
 *     - Método 3: hardcodeada, con un método que inicialice las plazas y transiciones a partir de una matriz configurable.
 * 
 *   - Cantidad de hilos = 3 ta bien?
 *     - Si los hilos representan los distintos modos de procesamiento, los tokens en la plaza 3 (buffer de los datos a procesar) también son recursos compartidos? (habría competencia sobre ellos).
 *     - Si los hilos representan los distintos modos de procesamiento, habría un cuarto hilo para las transiciones T0, T1 y T11?? (elimina competencia sobre el acceso al bus de datos pero permite competencia sobre los tokens del buffer).
 * 
 *   - Cómo se debe configurar la simulación? Politica, cantidad de invariantes, marcado inicial, delay, etc.
 * 
 *   - Cómo se deben lanzar las transiciones?
 *     - Método 1: disparar transiciones sensibilizadas al azar.
 *     - Método 2: disparar transiciones en un orden específico.
 *     - Método 3: intentar disparar al mismo tiempo todas las transiciones que estén sensibilizadas, como si fuera operacion de matrices (genera condiciones de carrera). NOO
 *       - Cómo se hace para disparar todas las transiciones al mismo tiempo? (encapsular en un hilo cada transición y dispararlas todas al mismo tiempo, o usar un ExecutorService para manejar los hilos).
 *       - Cómo se debe manejar la concurrencia? (usar synchronized, semáforos, monitores, etc.)
 *     
 *   - Cómo se deben manejar los errores?
 * 
 *   - El método del monitor fireTransition(int transition) debería ser llamado desde la clase RedDePetri? No
 *   - El método del monitor fireTransition(int transition) tiene como argumento el id de la transición o la cantidad de transiciones a disparar? el id
 *     - Id de la transición: porque el monitor debería ser capaz de disparar una transición específica indicada por la red de Petri.
 *     - Cantidad de transiciones a disparar: porque el monitor debería ser capaz de disparar una cantidad específica de transiciones según política.
 * 
 *   - Cómo se implementa la política de disparo de transiciones?
 *     - Pasar como argumento al constructor de la red de Petri => Pasar como argumento al constructor del monitor => implementar la política en el monitor.
 *     - 
 *   - Política 1: disparar transiciones al azar.
 *   - Política 2: dar prioridad al modo simple cuando haya conflicto.
 * 
 * ===========================================================================
 * 
 * Respuestas:
 * Los hilos lanzan las transiciones por el monitor, que se encarga de verificar si la transición está sensibilizada y dispararla.
 * Los hilos tienen una lista de las transisiones que quiere disparar y las itera con un for dentro de un while que se ejecuta hasta que se cumplan las invariantes de plaza.
 * La estructura de la red de Petri y el marcado inicial se definen a través de un archivo de configuración JSON, que se lee al iniciar la simulación. Matrices de incidencia I+ y I-.
 * La política de disparo de transiciones se debe definir en el Main, a través de un parámetro en el constructor de la red de Petri, que se pasa al monitor.
 * 
 * Semáforo Mutex
 * cada transición tiene un semáforo asociado que se adquiere al disparar la transición y se libera al finalizar.
 * 
 * Volver a ver clase 10/06 para ver tiempo de transición 
 * El monitor hace que los hilos esperen el alfa de la transición, y luego los hilos disparan la transición.
 * Disparo, cambio de estado y guardo el tiempo en eque se sensibilizan la transiciones. (vectores wasSensitized, isSensitized, timeSensitized)
 * Análisis del tiempo de la ejecución de la red de Petri.
 * 18:43 dice Ventre que no tendríamos varios hilos disparando la misma transición.
 */