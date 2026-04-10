package test;


public class IncThread implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < Main.STEPS; i++) {
            Main.X.incrementAndGet();
        }
    }
}
