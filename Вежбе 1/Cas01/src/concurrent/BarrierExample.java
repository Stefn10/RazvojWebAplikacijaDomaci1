package concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BarrierExample implements Runnable {

    CyclicBarrier cyclicBarrier;
    int redniBroj;

    public BarrierExample(CyclicBarrier cyclicBarrier, int redniBroj) {
        this.cyclicBarrier = cyclicBarrier;
        this.redniBroj = redniBroj;
    }

    @Override
    public void run() {
        System.out.println("Nit broj " + redniBroj + " ceka.");

        try {
            cyclicBarrier.await();  //  Blokira nit dok barijera ne pukne, tj. dok se ne nakupi dovoljno niti koje cekaju na barijeri.

            System.out.println("Nit" + redniBroj + " vise ne ceka.");
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Runnable() {
            // ovaj Runnable predstavlja zadatak koji ce barijera obaviti kada pukne
            // tj. ovaj run se izvrsava pre nego sto se konretne run metode za niti izvrse.
            @Override
            public void run() {
                System.out.println("Barijera je pukla, niti ce poceti sa radom uskoro...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Niti pocinju sa radom...");
            }
        });

        for (int i = 0; i < 4; i++) {
            new Thread((new BarrierExample(cyclicBarrier, i))).start();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
