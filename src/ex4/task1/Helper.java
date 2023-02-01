package ex4.task1;

import java.io.*;
import java.net.Socket;

public class Helper implements Runnable {

    private final Socket socket;

    private BufferedReader reader;

    private PrintWriter writer;

    private final Server server;

    public Helper(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            InputStream is = this.socket.getInputStream();
            OutputStream os = this.socket.getOutputStream();
            this.reader = new BufferedReader(new InputStreamReader(is));
            this.writer = new PrintWriter(os);
        } catch (IOException ignored) {
        }
    }

    public void write(String message) {
        this.writer.println(message.replace("\n", ""));
        this.writer.flush();
    }

    @Override
    public void run() {
        try {
            String msg = this.reader.readLine();
            MessageType type = MessageType.ERROR;

            if (msg.matches("^REG[a-zA-Z]+$")) {
                type = MessageType.REGISTER;
            } else if (msg.matches("^SND[a-zA-Z]+#[a-zA-Z]+#[a-zA-Z\\s.:,;!?]+$")) {
                type = MessageType.SEND;
            } else if (msg.matches("^RCV[a-zA-Z]+$")) {
                type = MessageType.RECEIVE;
            }

            this.server.processMessage(type, msg.substring(3), this);
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}