package ex2.task1;

import java.util.Random;

public class Student implements Runnable {

    private final KitchenCounter kc;

    public Student(KitchenCounter kc) {
        this.kc = kc;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(new Random().nextInt(6000));
                this.kc.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
