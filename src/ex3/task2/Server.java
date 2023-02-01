package ex3.task2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {

        ExecutorService es = Executors.newFixedThreadPool(5);

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                try {
                    Socket s = serverSocket.accept();
                    es.execute(new Helper(s));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
        }
    }
}
