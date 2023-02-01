package ex2.task2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Browser extends JFrame implements ActionListener {
    private final JButton startButton;

    private final ArrayList<Thread> downloadThreads = new ArrayList<>();

    private final CountDownLatch finished;

    public Browser(int downloads) {
        super("Download-Browser");

        this.finished = new CountDownLatch(downloads);

        var bars = new JProgressBar[downloads];
        JPanel rows = new JPanel(new GridLayout(downloads, 1));

        for (int i = 0; i < downloads; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 10));
            bars[i] = new JProgressBar(0, 100);
            bars[i].setPreferredSize(new Dimension(500, 20));
            row.add(bars[i]);
            rows.add(row);

            this.downloadThreads.add(new Download(bars[i], this.finished));
        }

        this.startButton = new JButton("Download");
        this.startButton.addActionListener(this);

        this.add(rows, BorderLayout.CENTER);
        this.add(this.startButton, BorderLayout.SOUTH);

        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        for (Thread t : this.downloadThreads)
            t.start();

        this.startButton.setEnabled(false);
        this.startButton.setSelected(false);
        this.startButton.setText("Downloads running...");

        new Thread(() -> {
            try {
                this.finished.await();
                this.startButton.setText("Finished");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws InterruptedException {
        new Browser(6);
    }
}
