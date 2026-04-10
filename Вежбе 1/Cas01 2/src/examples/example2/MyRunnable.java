package examples.example2;

public class MyRunnable implements Runnable{

    private final long countUntil;

    public MyRunnable(long countUntil) {
        this.countUntil = countUntil;
    }

    @Override
    public void run() {
        long sum = 0;

        for(long i = 0 ; i < countUntil ; i++) {
            sum += i;
        }

        System.out.println(sum);
    }
}
