package threadBasic;

public class AnonymousThread {

    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                while(true){
//
//                }
                System.out.println("Anonymous thread implementation.");
            }
        });

        thread.start();
        System.out.println("Thread je zavrsio sa poslom");
    }
}
