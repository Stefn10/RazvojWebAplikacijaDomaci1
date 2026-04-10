package examples.example1;

import test.DecThread;
import test.IncThread;

public class Main {

    public static int x = 0;
    public static final int STEPS = 100000;
    public static final Object lock = "LOCK";

    public static void main(String[] args) {

        Thread incThread = new Thread(new IncRunnable());
//        Thread incThread = new Thread(new IncThread());
        Thread decrThread = new Thread(new DecrRunnable());
//        Thread decrThread = new Thread(new DecThread());

        incThread.start();
        decrThread.start();

        try {
            incThread.join();
            decrThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("X: " + x);
        System.out.println("End");
    }
}
