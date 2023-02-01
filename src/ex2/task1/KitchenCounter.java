package ex2.task1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class KitchenCounter {

    private int count;

    private final int MAX_COUNT;

    private final Lock lock = new ReentrantLock();

    private final Condition isEmpty;

    private final Condition isFull;

    public KitchenCounter(int count) {
        this.MAX_COUNT = Math.abs(count);
        this.isEmpty = this.lock.newCondition();
        this.isFull = this.lock.newCondition();
    }

    public void put() throws InterruptedException {
        this.lock.lock();

        while (this.count >= this.MAX_COUNT) {
            this.isFull.await();
            System.out.println("count: " + this.count + " - " + Thread.currentThread().getName() + " waiting to place");
        }

        this.count++;
        System.out.println("count: " + this.count + " - " + Thread.currentThread().getName() + " placed food");

        this.isEmpty.signal();
        this.lock.unlock();
    }

    public void take() throws InterruptedException {
        this.lock.lock();

        while (this.count <= 0) {
            this.isEmpty.await();
            System.out.println("count: " + this.count + " - " + Thread.currentThread().getName() + " waiting to take");
        }

        this.count--;
        System.out.println("count: " + this.count + " - " + Thread.currentThread().getName() + " took food");

        this.isFull.signal();
        this.lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        KitchenCounter kc = new KitchenCounter(4);

        new Thread(new Waiter(kc), "Waiter-1").start();
        new Thread(new Waiter(kc), "Waiter-2").start();

        for (int i = 0; i < 8; i++)
            new Thread(new Student(kc), "Student" + (i + 1)).start();

        Thread.sleep(30000);
        System.exit(1);
    }
}
