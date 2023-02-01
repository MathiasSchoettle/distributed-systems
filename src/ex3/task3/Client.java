package ex3.task3;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        try (Socket s = new Socket("localhost", 8080)) {
            InputStream is = s.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            OutputStream os = s.getOutputStream();
            PrintWriter writer = new PrintWriter(os);

            new Thread(new Receiver(reader)).start();
            new Thread(new Sender(writer, Integer.toString(new Random().nextInt(40)))).start();
        }
    }
}
