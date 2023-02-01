package ex2.task1;

import java.util.Random;

public class Waiter implements Runnable {

    private final KitchenCounter kitchenCounter;

    public Waiter(KitchenCounter kitchenCounter) {
        this.kitchenCounter = kitchenCounter;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(new Random().nextInt(1000));
                this.kitchenCounter.put();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
