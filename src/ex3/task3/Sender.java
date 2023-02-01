package ex3.task3;

import java.io.PrintWriter;
import java.util.Scanner;

public class Sender implements Runnable {
    private final PrintWriter printWriter;
    private final String name;
    public Sender(PrintWriter printWriter, String name) {
        this.printWriter = printWriter;
        this.name = name;
    }
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            this.printWriter.println(this.name + " - " + message);
            this.printWriter.flush();
        }
    }
}
