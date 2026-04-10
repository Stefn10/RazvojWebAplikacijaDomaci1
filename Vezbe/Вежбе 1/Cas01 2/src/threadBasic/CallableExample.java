package threadBasic;

import java.util.concurrent.*;

public class CallableExample implements Callable<String> {

    /**
     * Callable interfejs je slican kao Runnable, razlika je sto Callable ima metodu call() koja vraca bilo koji
     * objekat koji nasledjuje klasu Object (String, Integer itd.), dok Runnable ne vraca nista (void).
     */

    /**
     * Future mozete da posmatrate kao objekat koji sadrzi rezultat, mozda ga ne sadrzi sada, ali ce ga sadrzati
     * u buducnosti sigurno. Znaci future daje obecanje da ce sadrzati rezultat, cim se zavrsi thread i dobije rezultat
     * iz thread-a.
     */

    @Override
    public String call() {
        return "Callable result.";
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(new CallableExample());
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdownNow();
    }
}
