package concurrent;

public class SynchronizedBlock {

    private int a = 0;

    public synchronized void increment() {
        // synchronized u opisu metode je ekvivalentno sa:
//        synchronized (this) {
        System.out.println("....");
        a++;
        System.out.println("a = " + a);
//        }
    }


    public static void main(String[] args) throws InterruptedException {
        SynchronizedBlock synchronizedBlock = new SynchronizedBlock();
        SynchronizedBlock synchronizedBlock1 = new SynchronizedBlock();


        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronizedBlock.increment();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronizedBlock1.increment();
            }
        });

        thread1.start();
        thread2.start();

        // Main thread ceka da thread1 i thread2 zavrse
        thread1.join();
        thread2.join();

        System.out.println(synchronizedBlock1.a);

    }
}
