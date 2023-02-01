package ex1;

import java.util.Random;

public class Garage {

    private int spaceCount;

    private int filledSpaceCount = 0;

    private final Object lock = new Object();

    public static final Random rand = new Random();

    public Garage(int spaceCount) {
        this.spaceCount = spaceCount;
    }

    public void enter() {

        synchronized (this.lock) {
            while (this.filledSpaceCount >= this.spaceCount) {
                try {
                    System.out.println("waiting outside: " + Thread.currentThread().getName());
                    this.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("entered: " + Thread.currentThread().getName());
            this.filledSpaceCount++;
            this.lock.notify();
        }
    }

    @SuppressWarnings("WaitWhileHoldingTwoLocks")
    public synchronized void exit() {

        synchronized (this.lock) {
            while (this.filledSpaceCount <= 2) {
                try {
                    System.out.println("waiting inside: " + Thread.currentThread().getName());
                    this.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            this.filledSpaceCount--;
            System.out.println("exit: " + Thread.currentThread().getName());
            this.lock.notify();
        }
    }
}
