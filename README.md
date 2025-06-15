  # Trabajo Práctico Final - Programación Concurrente

  ### Objetivo:
  Simular una red de Petri con concurrencia controlada, en la que múltiples hilos representan
  procesos de distinto nivel de complejidad (simple, medio, alto). El sistema modela 
  procesamiento de datos mediante transiciones sincronizadas que acceden a recursos compartidos 
  como un bus (P2) y una unidad de procesamiento (P6).

  ### Componentes principales:

  - RedDePetri: Representa la estructura lógica (plazas y transiciones) de la red.
  - Plaza: Nodo que almacena tokens.
  - Transicion: Nodo que consume y produce tokens, con entradas/salidas definidas.
  - Monitor: Punto central de sincronización. Implementa fireTransition(int id), verificando si la transición está habilitada y aplicando los cambios en el marcado. Usa wait/notify o Condition para evitar espera activa.
  - TransitionThread: Hilos concurrentes que ejecutan una secuencia de transiciones según el modo de procesamiento. Se crean 3 hilos, uno por tipo de invariante (simple, medio, alto).
  - Politica: Decide qué modo usar (aleatorio o prioritario) para procesar un nuevo dato.
  - Simulacion: Orquesta todo el sistema. Carga config, lanza los hilos, cuenta invariantes completados y termina.
  - Logger: Registra eventos importantes (disparos de transiciones, tiempos, errores) en archivo o consola.

  ### Requisitos:
  - Ejecutar 200 invariantes completos.
  - Sincronizar acceso a recursos compartidos (P2, P6) correctamente.
  - Aplicar wait/notify para espera pasiva en el monitor.
  - Loggear cada transición disparada.
  - Permitir configuraciones externas vía archivo JSON (tiempos, política, etc.).
  - Cumplir con buenas prácticas de POO, modularidad y concurrencia segura.

  ### Ejecución esperada:
  1. Main carga la red, el monitor y la configuración.
  2. Se lanzan los 3 hilos correspondientes a los modos de complejidad.
  3. Cada hilo intenta disparar su secuencia de transiciones según la política.
  4. El monitor asegura que solo transiciones habilitadas se disparen, de forma segura.
  5. Se registran los eventos.
  6. La simulación finaliza al completar los 200 caminos completos (invariantes).