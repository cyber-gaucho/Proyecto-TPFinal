# TO DO:
### Crear clases:
 * [DONE] Plaza: `int id`, `int tokens`, `void addToken()`, `void removeToken()`
 * [DONE] Transicion: `int id`, `int[] plazasEntrada`, `int[] plazasSalida`, `int delay`, `bool isSencivilizada()`, `void disparar()`. (No soporta peso de arco por ahora)
 * Sublcase `TransicionDelay`: hereda de `Transicion`, agrega delay y `@Override` `disparar()` 
 * [DONE] RedDePetri: vector o mapa para almacenar plazas y transiciones
 * Marcado (?) sería clase o un vector de enteros?
 * Como hacer esperar al hilo desde la transición? (usar Thread.sleep(delay) en el método disparar de la transición).
 * Verificar el uso de la **matriz de incidencia**

---

# Consultas
### Cómo se debe definir la estructura de la red de Petri?
 * Método 1: hardcodeada, con un constructor que inicialice las plazas y transiciones.
 * Método 2: configurable, con un archivo de configuración: **JSON**, **XML**, etc.   
 * Método 3: hardcodeada, con un método que inicialice las plazas y transiciones a partir de una matriz configurable.

### Cantidad de hilos
 * Si los hilos representan los distintos modos de procesamiento, los tokens en la plaza 3 (buffer de los datos a procesar) también son recursos compartidos? (habría competencia sobre ellos).
 * Si los hilos representan los distintos modos de procesamiento, habría un cuarto hilo para las transiciones T0, T1 y T11?? (elimina competencia sobre el acceso al bus de datos pero permite competencia sobre los tokens del buffer).
 * **5 HILOS**: Un hilo para el tramo inicial (T0 y T1), y después un hilo para cada modo de procesamiento (T2, T3, T4 para modo simple, T5, T6 para modo medio, T7, T8, T9, T10 para modo alto). Habría un hilo después del joint que va a encargarse de disparar T11, ya que habría hasta un token en la plaza 11.

### Cómo se debe configurar la simulación? Politica, cantidad de invariantes, marcado inicial, delay, etc.


### Cómo se deben lanzar las transiciones?
 * Método 1: disparar transiciones sensibilizadas al azar.
 * Método 2: disparar transiciones en un orden específico.
 * Método 3: intentar disparar al mismo tiempo todas las transiciones que estén sensibilizadas, como si fuera operacion de matrices (genera condiciones de carrera). NOO
### Cómo se debe manejar la concurrencia?
(usar synchronized, semáforos, monitores, etc.)
     
### Cómo se deben manejar los errores?

### El método del monitor fireTransition(int transition) debería ser llamado desde la clase RedDePetri?
No

### El método del monitor fireTransition(int transition) tiene como argumento el id de la transición o la cantidad de transiciones a disparar?
Respuesta: el id
 * Id de la transición: porque el monitor debería ser capaz de disparar una transición específica indicada por la red de Petri.
 * Cantidad de transiciones a disparar: porque el monitor debería ser capaz de disparar una cantidad específica de transiciones según política.

### Cómo se implementa la política de disparo de transiciones?
 * Pasar como argumento al constructor de la red de Petri $\rightarrow$ Pasar como argumento al constructor del monitor $\rightarrow$ implementar la política en el monitor.
 * **Política 1**: disparar transiciones al azar.
 * **Política 2**: dar prioridad al modo simple cuando haya conflicto.
 
## Respuestas:
 * Los hilos lanzan las transiciones por el monitor, que se encarga de verificar si la transición está sensibilizada y dispararla.
 * Los hilos tienen una lista de las transisiones que quiere disparar y las itera con un bucle `for` dentro de un `while` que se ejecuta hasta que se cumplan las invariantes de plaza.
 * La estructura de la red de Petri y el marcado inicial se definen a través de un archivo de configuración JSON, que se lee al iniciar la simulación. Matrices de incidencia I+ y I-.
 * La política de disparo de transiciones se debe definir en el `Main`, a través de un parámetro en el constructor de la red de Petri, que se pasa al monitor.
 * **Semáforo Mutex**: cada transición tiene un semáforo asociado que se adquiere al disparar la transición y se libera al finalizar.
 * Volver a ver clase 10/06 para ver tiempo de transición 
 * El monitor hace que los hilos esperen el alfa de la transición, y luego los hilos disparan la transición.
 * Disparo, cambio de estado y guardo el tiempo en que se sensibilizan la transiciones. (vectores `wasSensitized`, `isSensitized`, `timeSensitized`)
 * Análisis del tiempo de la ejecución de la red de Petri.
 * 18:43 dice Ventre que no tendríamos varios hilos disparando la misma transición. **(Indica que la transición 11 tiene un solo hilo)**
 * La clase `monitor`, no puede contener ninguna referencia a transiciones puntuales de la  red  en  cuestión.  Es  decir,  el  monitor  debe  ser  agnóstico  a  la  red  que  está ejecutando. De esta manera, cambiando la red de Petri, la clase Monitor no sufre ningún cambio.

---

 # Ideas
 * Modificar la clase `HiloDisparador` para que admita un `String nombre` y usarlo en lugar de crar subclases con nombre y las transiciones hardocodeadeas. Usaríamos directamente la super clase para crear los hilos.