package monitor;

public class Monitor implements MonitorInterface {

    public Monitor() {
    }

    @Override
    public boolean fireTransition(int transition) {
        System.out.println("Intentando disparar T" + transition);
        return false;
    }
    
}
