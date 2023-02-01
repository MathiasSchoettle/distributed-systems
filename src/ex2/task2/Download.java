package ex2.task2;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Download extends Thread {

    private final JProgressBar bar;

    private final CountDownLatch finished;

    private final int TIME_PER_PERCENT = new Random().nextInt(100);

    public Download(JProgressBar bar, CountDownLatch finished) {
        this.finished = finished;
        this.bar = bar;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 100; i++) {
                this.makeProgress(i);
                Thread.sleep(this.TIME_PER_PERCENT);
            }

            this.finished.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void makeProgress(int count) {
        int value = (int) ((float) this.bar.getMaximum() * ((float) count / 100));
        this.bar.setValue(value);
    }
}
