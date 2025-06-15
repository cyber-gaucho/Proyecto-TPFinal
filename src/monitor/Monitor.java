package monitor;

public class Monitor implements MonitorInterface {
    private red.RedDePetri redDePetri;
    
    public Monitor(red.RedDePetri red) {
        this.redDePetri = red;
    }

    @Override
    public boolean fireTransition(int transition) {
        System.out.println("Intentando disparar T" + transition);
        return false;
    }
    
}
