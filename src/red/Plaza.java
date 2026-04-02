package red;

public class Plaza {
    private final int id;
    private int tokens;
    
    public Plaza(int id, int tokens) {
        this.id = id;
        this.tokens = tokens;
    }

    public Plaza(int id) {
        this(id, 0);
    }

    public int getId() {
        return id;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return "Plaza " + id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Plaza plaza = (Plaza) obj;
        return id == plaza.id && tokens == plaza.tokens;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + tokens;
        return result;
    }

    public boolean hasTokens() {
        return tokens > 0;
    }

    /**
     * Agrega un token a la plaza.
     */
    public void addToken() {
        tokens++;
    }

    /**
     * Agrega una cantidad específica de tokens a la plaza.
     * 
     * @param count Cantidad de tokens a agregar
     * @throws IllegalArgumentException si count es negativo
    */
   public void addTokens(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("No se puede agregar un número negativo de tokens.");
        }
        tokens += count;
    }

    /**
     * Elimina un token de la plaza. Si no hay tokens disponibles, lanza una excepción.
     * 
     * @throws IllegalStateException si no hay tokens para eliminar
     */
    public void removeToken() {
        if (tokens > 0) {
            tokens--;
        } else {
            throw new IllegalStateException("No hay suficientes tokens para eliminar.");
        }
    }

    /**
     * Elimina una cantidad específica de tokens de la plaza.
     * 
     * @param count Cantidad de tokens a eliminar
     * @throws IllegalArgumentException si count es negativo
     * @throws IllegalStateException si no hay suficientes tokens para eliminar
     */
    public void removeTokens(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("No se puede eliminar un número negativo de tokens.");
        }
        if (tokens < count) {
            throw new IllegalStateException("No hay suficientes tokens para eliminar.");
        }
        tokens -= count;
    }

    public void resetTokens() {
        tokens = 0;
    }
}
