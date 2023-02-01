package ex3.task3;

import java.io.*;
import java.net.Socket;

public class Helper implements Runnable {
    private Server server;

    private BufferedReader reader;

    private PrintWriter writer;

    public Helper(Socket socket, Server server) {
        try {
            this.server = server;

            InputStream is = socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(is));

            OutputStream os = socket.getOutputStream();
            this.writer = new PrintWriter(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getWriter() {
        return this.writer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = this.reader.readLine();
                this.server.gotMessage(message, this.writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
