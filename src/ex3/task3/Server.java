package ex3.task3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * When a Server is running a client will connect to a socket.
 * A client can send messages and all other connected clients will read that message with an annotated name (number)
 */
public class Server {

    private final int port;

    private final ArrayList<PrintWriter> writers = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            while (true) {
                try {
                    Socket s = serverSocket.accept();
                    Helper helper = new Helper(s, this);
                    this.writers.add(helper.getWriter());
                    new Thread(helper).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotMessage(String message, PrintWriter writerOfSender) {
        for (PrintWriter writer : this.writers) {
            if (!writer.equals(writerOfSender)) {
                writer.println(message);
                writer.flush();
            }
        }
    }

    public static void main(String[] args) {
        new Server(8080).start();
    }
}
