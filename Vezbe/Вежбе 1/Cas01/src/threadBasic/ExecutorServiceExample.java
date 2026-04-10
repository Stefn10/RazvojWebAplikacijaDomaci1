package threadBasic;

import java.util.concurrent.*;

public class ExecutorServiceExample implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable implementation");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /** THREAD POOLS */

        /**  Thread pool koji kreira nove thread-ove po potrebi, ali ce koristiti prethodno kreirane thread-ove kada je to moguce. */
        ExecutorService executorService = Executors.newCachedThreadPool();

        /** Thread pool sa fiksnim brojem thread-ova u njemu.  */
//        ExecutorService executorServiceFixed = Executors.newFixedThreadPool(5);

        /** Thread pool sa samo jednim thread-om. */
//        ExecutorService executorServiceSingle = Executors.newSingleThreadExecutor();


        /** Thread pool u kom mozete da birate kada ce se neki thread izvrsiti */
//         ScheduledExecutorService executorServiceScheduled = Executors.newScheduledThreadPool(5);
//         executorServiceScheduled.schedule(new ExecutorServiceExample(), 3000, TimeUnit.MILLISECONDS); // Aktivirace ovaj thread za 3 sekunde
//
//        executorService.submit(new ExecutorServiceExample());

        Future<String> future = executorService.submit(new CallableExample());
//        future.get();   //  Blokira izvrsavanje dok se ne dobije vrednost za Future.
        System.out.println("Proveravamo da li je asinhroni zadatak zavrsen: " + future.isDone());
        System.out.println("Rezultat izvrsavanja dobijamo pomocu get() metode: " + future.get());
        executorService.shutdown(); //  Ne dozvoljava izvrsavanje novih niti, ali ne prekida one koje se jos uvek izvrsavaju.
//        executorService.shutdownNow();    //  Ne dozvoljava izvrsavanje novih niti, i prekida one koje se jos uvek izvrsavaju (U njima se javlja InterrupedException).

    }
}

