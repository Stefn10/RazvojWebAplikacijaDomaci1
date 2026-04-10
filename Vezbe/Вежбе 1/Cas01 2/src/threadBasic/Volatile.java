package threadBasic;

import java.util.concurrent.atomic.AtomicInteger;

class Volatile extends Thread {

    boolean keepRunning = true;
//    volatile boolean keepRunning = true;
    AtomicInteger atomicInteger = new AtomicInteger(5);
    public void run() {
        while (this.keepRunning) {
        }
        atomicInteger.incrementAndGet();
        atomicInteger.get();

        System.out.println("Thread terminated.");
    }

    public static void main(String[] args) throws InterruptedException {
        Volatile t = new Volatile();
        t.start();
        Thread.sleep(1000);
        t.keepRunning = false;
        System.out.println("keepRunning set to false.");
    }
}
