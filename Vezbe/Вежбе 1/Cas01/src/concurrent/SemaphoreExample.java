package concurrent;

import java.util.concurrent.Semaphore;

public class SemaphoreExample implements Runnable{

    int redniBroj;

    Semaphore semaphore;

    public SemaphoreExample(int redniBroj, Semaphore semaphore) {
        this.redniBroj = redniBroj;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        System.out.println("Nit broj " +redniBroj +  " ceka na semaforu.");
//        System.out.println("Nit broj " +Thread.currentThread().getId() +  " ceka na semaforu.");
        try {
            // tred uzima jednu od dostupnih dozvola
            // ukoliko nema vise slobodnih dozvola, ovde se zablokira
            semaphore.acquire();
//            semaphore.tryAcquire();   //  Vraca true ili false u zavisnosti od dobijene dozvole, ali nema blokiranja niti.
            System.out.println("Semaphore permits available: " + semaphore.availablePermits());
            Thread.sleep(3000);
            System.out.println("Nit broj " + redniBroj+" oslobadja dozvolu.");
            semaphore.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3, true);

        for(int i = 0; i < 5; i++){
            (new Thread(new SemaphoreExample(i, semaphore))).start();
        }
    }
}
