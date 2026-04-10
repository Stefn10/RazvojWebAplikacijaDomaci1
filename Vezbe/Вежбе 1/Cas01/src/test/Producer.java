package test;

import java.util.Random;

public class Producer implements Runnable{
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                Main.queue.put((new Random()).nextInt());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
