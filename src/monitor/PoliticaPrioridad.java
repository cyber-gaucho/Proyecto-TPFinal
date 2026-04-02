package monitor;

import red.RedDePetri;
import java.util.List;

/**
 * Política de selección por prioridad.
 * STUB - Implementación futura.
 * 
 * Esta política priorizará el modo Simple sobre Media, y Media sobre Alta,
 * cuando exista competencia entre transiciones de diferentes modos de complejidad.
 * 
 * Estructura de la red:
 * - Modo Simple: Transiciones T5 y T6 (plazas P7)
 * - Modo Media: Transiciones T2, T3, T4 (plazas P4, P5)
 * - Modo Alta: Transiciones T7, T8, T9, T10 (plazas P8, P9, P10)
 */
public class PoliticaPrioridad implements InterfazPolitica {
    
    @Override
    public int seleccionarTransicion(List<Integer> transicionesEnCompetencia, RedDePetri red) {
        // TODO: Implementar lógica de prioridad
        // 1. Identificar a cuál modo complejo pertenece cada transición
        // 2. Seleccionar primero de modo Simple (T5, T6)
        // 3. Si no hay Simple, seleccionar de Media (T2, T3, T4)
        // 4. Si no hay Media, seleccionar de Alta (T7-T10)
        
        // Por ahora, usar fallback a selección aleatoria
        if (transicionesEnCompetencia == null || transicionesEnCompetencia.isEmpty()) {
            throw new IllegalArgumentException("La lista de transiciones en competencia no puede estar vacía");
        }
        
        int indiceSeleccionado = new java.util.Random().nextInt(transicionesEnCompetencia.size());
        return transicionesEnCompetencia.get(indiceSeleccionado);
    }
    
    @Override
    public String obtenerNombre() {
        return "Política Prioridad (Stub)";
    }
}
