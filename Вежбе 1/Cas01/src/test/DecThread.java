package test;

public class DecThread implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < Main.STEPS; i++) {
            Main.X.addAndGet(10);
        }
    }

}
