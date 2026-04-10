package rs.raf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ThreadSafeMain {

    public static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {

        List<String> list = new ArrayList<>();
        //List<String> list = Collections.synchronizedList(new ArrayList<>()); //sinhronizuje pojedinačne metode, ali iteracija je više operacija hasNext next next next
        //List<String> list = new CopyOnWriteArrayList<>(); //kopiraj pri pisanju
        Map<String, String> map = new ConcurrentHashMap<>();
        list.add("item1");
        list.add("item2");
        list.add("item3");
        list.add("item4");
        list.add("item5");

        Thread thread1 = new Thread(() -> {
//            synchronized (LOCK) {
                list.iterator().forEachRemaining((String item) -> {
                    System.out.println(item);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
//            }
        });

        Thread thread2 = new Thread(() -> {
//            synchronized (LOCK) {
                list.remove(0);
                list.remove(1);
                list.remove(2);
//            }
        });

        thread1.start();
        Thread.sleep(100);
        thread2.start();

    }

}
