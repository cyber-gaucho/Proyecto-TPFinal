package monitor;

import red.RedDePetri;
import java.util.List;
import java.util.Random;

/**
 * Política de selección aleatoria.
 * Cuando múltiples transiciones están en competencia, selecciona aleatoriamente
 * una de ellas con igual probabilidad para todas.
 */
public class PoliticaAleatoria implements InterfazPolitica {
    private Random random = new Random();
    
    @Override
    public int seleccionarTransicion(List<Integer> transicionesEnCompetencia, RedDePetri red) {
        if (transicionesEnCompetencia == null || transicionesEnCompetencia.isEmpty()) {
            throw new IllegalArgumentException("La lista de transiciones en competencia no puede estar vacía");
        }
        
        int indiceSeleccionado = random.nextInt(transicionesEnCompetencia.size());
        return transicionesEnCompetencia.get(indiceSeleccionado);
    }
    
    @Override
    public String obtenerNombre() {
        return "Política Aleatoria";
    }
}
