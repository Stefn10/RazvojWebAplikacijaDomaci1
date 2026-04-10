package threadBasic;

public class ThreadClass extends Thread {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " -> Nasledili smo Thread klasu.");
    }

    public static void main(String[] args) {
        ThreadClass threadClass = new ThreadClass();
        // ISPRAVNO
        // novi thread se pravi samo pozivanjem metode start()
        threadClass.start();
        // POGRESNO
        // izvrsice kod is run() na Main threadu
        threadClass.run();
    }
}
