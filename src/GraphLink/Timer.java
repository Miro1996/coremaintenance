package GraphLink;

public class Timer {

    public Timer() {
        s = System.currentTimeMillis();
    }

    public void start() {
        s = System.currentTimeMillis();
    }

    public long elapsed() {
        return System.currentTimeMillis() - s;
    }
    private long s;
}