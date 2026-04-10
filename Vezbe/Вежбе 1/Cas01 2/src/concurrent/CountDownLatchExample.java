package concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class CountDownLatchExample implements Runnable {
    CountDownLatch countDownLatch;
    int id;

    public CountDownLatchExample(CountDownLatch countDownLatch, int id) {
        this.countDownLatch = countDownLatch;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            countDownLatch.countDown();
//            countDownLatch.countDown();   //  Za razliku od barijere, ista nit moze vise puta da umanji brojac.
//            countDownLatch.countDown();
//            countDownLatch.countDown();
            // zatim se zaglave ovde
            countDownLatch.await();
            // kada brojac dodje do 0, pocinju da rade
            Thread.sleep(2000);
            System.out.println("Thread " + id + " started working");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(4);

        for (int i = 0; i < 5; i++) {
            (new Thread(new CountDownLatchExample(countDownLatch, i))).start();
        }


        // kako niti dolaze na red, dekrementiraju brojac
        countDownLatch.countDown();
        // kako niti dolaze na red, dekrementiraju brojac
        countDownLatch.countDown();
        // kako niti dolaze na red, dekrementiraju brojac
        countDownLatch.countDown();
        // kako niti dolaze na red, dekrementiraju brojac
        countDownLatch.countDown();

    }
}
