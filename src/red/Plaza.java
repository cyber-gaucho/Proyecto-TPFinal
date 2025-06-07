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

    public void removeToken() {
        if (tokens > 0) {
            tokens--;
        } else {
            throw new IllegalStateException("No hay suficientes tokens para eliminar.");
        }
    }

    public void addToken() {
        tokens++;
    }

    public void addTokens(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("No se puede agregar un número negativo de tokens.");
        }
        tokens += count;
    }

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
