package test;

public class Consumer implements Runnable{
    @Override
    public void run() {
        while (true) {
            try {
                Integer n = Main.queue.take();
                System.out.println(n);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
