package monitor;

import red.RedDePetri;
import java.util.List;

/**
 * Interfaz que define el contrato para las diferentes políticas de selección de transiciones.
 * Las políticas determinan cuál transición disparar cuando existe competencia
 * (múltiples transiciones que comparten plazas de entrada).
 */
public interface InterfazPolitica {
    /**
     * Selecciona una transición de entre las que están en competencia.
     * 
     * @param transicionesEnCompetencia Lista de IDs de transiciones que compiten
     * @param red La red de Petri para obtener información adicional
     * @return El ID de la transición seleccionada
     */
    int seleccionarTransicion(List<Integer> transicionesEnCompetencia, RedDePetri red);
    
    /**
     * Retorna el nombre de la política.
     * 
     * @return Nombre descriptivo de la política
     */
    String obtenerNombre();
}
