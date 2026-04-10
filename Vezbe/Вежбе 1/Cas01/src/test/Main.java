package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger X = new AtomicInteger(0);
    public static int STEPS = 100000;

    public static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

    public static final String LOCK1 = "LOCK1";

    public static void main(String[] args) {
        Thread t1 = new Thread(new Producer());
        Thread t2 = new Thread(new Consumer());
        t1.start();
        t2.start();
    }
}
