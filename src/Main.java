import monitor.Monitor;
import red.RedDePetri;

public class Main {
    public static void main(String[] args) {
        // Imprimir encabezado del programa
        printHeader();

        // Crear una instancia de RedDePetri y Monitor
        RedDePetri red = new RedDePetri();
        Monitor monitor = new Monitor(red);
        Thread[] hilos = {
            new Thread(new hilos.Entrada(monitor)), // Hilo para las transiciones T0 y T1
            new Thread(new hilos.ComplejidadSimple(monitor)), // Hilo para las transiciones T5 y T6
            new Thread(new hilos.ComplejidadMedia(monitor)), // Hilo para las transiciones T2, T3 y T4
            new Thread(new hilos.ComplejidadAlta(monitor)), // Hilo para las transiciones T7, T8, T9 y T10
            new Thread(new hilos.Salida(monitor)), // Hilo para la transición T11
            new Thread(new hilos.Salida(monitor)), // Hilo para la transición T11
            new Thread(new hilos.Salida(monitor)) // Hilo para la transición T11
        };
         
        // Iniciar los hilos
        for (Thread hilo : hilos) {
            hilo.start();
        }

        while(red.getInvariables() < 200) {
            // Esperar a que se alcance el número de invariantes
            try {
                Thread.sleep(100); // Esperar un poco antes de verificar nuevamente
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Interrumpir los hilos si aún están en ejecución
        for (Thread t : hilos) {
            if (t.isAlive()) {
                t.interrupt();
            }
        }

        // Esperar a que los hilos terminen
        try {
            for (Thread t : hilos) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        printResults(red);

        // System.out.println(red.toString());
    }

    public static void printHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"Simulador de Red de Petri - TP Concurrente 2025\"")
        .append("\n ___        _                                                      ___                ")
        .append("\n| __| __ _ (_) _ _  _ _   ___  ___ ___       __  ___  _ _         / __| ___  __  __ _ ")
        .append("\n| _| / _` || || '_|| ' \\ / -_)(_-/(_-/      / _|/ _ \\| ' \\       | (__ / _ \\/ _|/ _` |")
        .append("\n|_|  \\__/_||_||_|  |_||_|\\___|/__//__/      \\__|\\___/|_||_|       \\___|\\___/\\__|\\__/_|\n");
        System.out.println(sb.toString());
    }

    public static void printResults(RedDePetri red) {
        System.out.println("\nSimulación finalizada.");
        System.out.println("Invariables alcanzadas: " + red.getInvariables());
        System.out.println("Estadística de los modos de procesamiento:");
        System.out.println("Modo de complejidad simple: " + red.getContSimple());
        System.out.println("Modo de complejidad media: " + red.getContMedia());
        System.out.println("Modo de complejidad alta: " + red.getContAlta());
        
        System.out.println("Estado final de la red:\n" + red.getEstado());
        
        if(red.invariantesPlazaCheck()) {
            System.out.println("Invariantes de plaza cumplidas.");
        } else {
            System.out.println("Invariantes de plaza NO cumplidas.");
        }
    }
}



/*
 * TO DO: 
 * - Crear clase PolíticaDisparo:
 *      para definir la política de disparo de transiciones.
 * - Solucionar problema de thread scheduling (ya uso fairness en el lock del monitor y sigue sin andar):
 *      - Ver si es necesario usar un ExecutorService para manejar los hilos.
 *      - Ver si es necesario usar un semáforo para controlar el acceso a los recursos compartidos.
 * - Ver si es necesario el synchronized que le metí en el método disparar de las transiciones.       
 * 
 * - REVISAR ERROR GRAVE: Actualmente los hilos disparan las transiciones desde adentro del monitor,
 * 
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
 * El monitor tiene Colas de hilos bloqueados. Su responsabilidad es bloquear los hilos, con las
solicitudes de disparo de una transición requerida. Es necesario que a cada transición
se le asigne una cola, puesto que cada transición opera como una variable de
condición
 */

        // Thread t0 = new Thread(() -> {
        //     while (red.getInvariables() < 200 && !Thread.currentThread().isInterrupted()) {
        //         monitor.fireTransition(0); // T0
        //         monitor.fireTransition(1); // T1
        //         // monitor.fireTransition(11); // T11
        //     }
        //     // System.out.println("Hilo 0 ha terminado.");
        // });

        // // Iniciar la simulación con tres hilos que disparan transiciones
        // Thread t1 = new Thread(() -> {
        //     while (red.getInvariables() < 200 && !Thread.currentThread().isInterrupted()) {
        //         // monitor.fireTransition(0); // T0
        //         // monitor.fireTransition(1); // T1
        //         monitor.fireTransition(5); // T5
        //         monitor.fireTransition(6); // T6
        //         // monitor.fireTransition(11); // T11
        //     }
        //     // System.out.println("Hilo 1 ha terminado.");
        // });

        // Thread t2 = new Thread(() -> {
        //     while (red.getInvariables() < 200 && !Thread.currentThread().isInterrupted()) {
        //         // monitor.fireTransition(0); // T0
        //         // monitor.fireTransition(1); // T1
        //         monitor.fireTransition(2); // T2
        //         monitor.fireTransition(3); // T3
        //         monitor.fireTransition(4); // T4
        //         // monitor.fireTransition(11); // T11
        //     }
        //     // System.out.println("Hilo 2 ha terminado.");
        // });

        // Thread t3 = new Thread(() -> {
        //     while (red.getInvariables() < 200 && !Thread.currentThread().isInterrupted()) {
        //         // monitor.fireTransition(0); // T0
        //         // monitor.fireTransition(1); // T1
        //         monitor.fireTransition(7); // T7
        //         monitor.fireTransition(8); // T8
        //         monitor.fireTransition(9); // T9
        //         monitor.fireTransition(10); // T10
        //         // monitor.fireTransition(11); // T11
        //     }
        //     // System.out.println("Hilo 3 ha terminado.");
        // });

        // Thread t4 = new Thread(() -> {
        //     while (red.getInvariables() < 200 && !Thread.currentThread().isInterrupted()) {
        //         monitor.fireTransition(11); // T11
        //     }
        //     // System.out.println("Hilo 4 ha terminado.");
        // });

        // // Iniciar los hilos
        // t0.start();
        // t3.start();
        // t1.start();
        // t2.start();
        // t4.start();