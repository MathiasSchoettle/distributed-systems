package ex4.task2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Dispatcher extends Thread {
    private final int port;
    private final IChatMessageHub hub;

    public Dispatcher(int port, IChatMessageHub hub) {
        this.port = port;
        this.hub = hub;
        this.start();
    }

    /**
     * Listen to port and start new ServerEndPoints for every Client
     */
    @Override
    public void run() {
        try (ServerSocket socket = new ServerSocket(this.port)) {

            while (true) {
                Socket s = socket.accept();
                new ServerEndpoint(s, this.hub);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
