package examples.example1;

public class DecrRunnable implements Runnable {

    @Override
    public void run() {

        for (int i = 0 ; i < Main.STEPS ; i++) {
//            synchronized (Main.lock){
                Main.x--;
//            }
        }

        System.out.println("DecrThread ended!");
    }
}
