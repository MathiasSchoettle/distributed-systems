package ex3.task3;

import java.io.BufferedReader;
import java.io.IOException;

public class Receiver implements Runnable {

    private final BufferedReader bufferedReader;

    public Receiver(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String out = this.bufferedReader.readLine();
                System.out.println(out);
            } catch (IOException e) {
                break;
            }
        }
    }
}
