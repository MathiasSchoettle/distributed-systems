package ex4.task2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientEndpoint extends Thread implements IMessageSender {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private IMessageGui messageGui;
    @Override
    public void openChatConnection(String username, String host, int port, IMessageGui messageGui) {
        try {
            this.messageGui = messageGui;
            this.socket = new Socket(host, port);

            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.writer = new PrintWriter(this.socket.getOutputStream());

            this.writer.println("OPEN#" + username);
            this.writer.flush();

            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendChatMessage(String message) {
        this.writer.println("PUBL#" + message);
        this.writer.flush();
    }

    @Override
    public void closeChatConnection() {
        this.writer.println("EXIT");
        this.writer.flush();
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!this.socket.isClosed()) {
            try {
                String message = this.reader.readLine();
                String[] split = null;

                if (message != null && !message.isEmpty()) {
                    split = message.split("#");
                }

                if (split != null && split.length > 0) {
                    if (split[0].equals("SHOW")) {
                        this.messageGui.showNewMessage(split[1], split[2]);
                    } else if (split[0].equals("ADMN")) {
                        this.messageGui.showAdminMessage(split[1]);
                    }
                }
            } catch (IOException e) {
                if (!e.getMessage().equals("Socket closed"))
                    e.printStackTrace();
            }
        }
    }
}
