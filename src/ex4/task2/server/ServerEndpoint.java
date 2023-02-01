package ex4.task2.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerEndpoint extends Thread implements IChatListener {
    private String name;
    private final Socket socket;
    private final IChatMessageHub hub;
    private BufferedReader reader;
    private PrintWriter writer;

    public ServerEndpoint(Socket socket, IChatMessageHub hub) {
        this.socket = socket;
        this.hub = hub;

        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.start();
    }

    @Override
    public void onMessage(String fromUser, String message, boolean isAdmin) {
        if (isAdmin) {
            if (message.equals("EXIT")) {
                this.hub.removeChatListener(this);
                this.writer.println(message);
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                this.writer.println("ADMN#" + message);
            }
        } else {
            this.writer.println("SHOW#" + fromUser + "#" + message);
        }

        this.writer.flush();
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public void run() {
        try {
            this.name = this.reader.readLine().split("#")[1];
            this.hub.addChatListener(this);

            while (!this.socket.isClosed()) {

                String message = this.reader.readLine();

                if (message.equals("EXIT")) {
                    this.hub.removeChatListener(this);
                    this.socket.close();
                } else {
                    String[] split = message.split("#");
                    this.hub.publish(this.name, split[1], false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
