package threadBasic;

public class RunnableThread implements Runnable {

    @Override
    public void run() {
        System.out.println("I am running!");
    }

    public static void main(String[] args) {
        Thread t = new Thread(new RunnableThread());
        t.start();
    }
}
