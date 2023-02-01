package ex4.task1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ServerSocket serverSocket;
    private final HashMap<String, String> messages = new HashMap<>();

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.startServer();
    }

    private void startServer() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        while (true) {
            try {
                Socket s = this.serverSocket.accept();
                executorService.execute(new Helper(s, this));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean isUserRegistered(String userName) {
        return this.messages.containsKey(userName);
    }

    public synchronized boolean addUserToList(String userName) {
        if (this.isUserRegistered(userName)) {
            return false;
        }

        this.messages.put(userName, "");
        return true;
    }

    public synchronized String getMessagesOfUser(String userName) {
        if (!isUserRegistered(userName)) {
            return "";
        }

        return this.messages.replace(userName, "");
    }

    public synchronized boolean addMessageToUser(String userNameFrom, String userNameTo, String message) {
        if (this.isUserRegistered(userNameFrom) && this.isUserRegistered(userNameTo)) {
            this.messages.replace(userNameTo, this.messages.get(userNameTo) + "\n - " + userNameFrom + ": " + message);
            return true;
        }

        return false;
    }

    public void processMessage(MessageType type, String message, Helper helper) {
        switch (type) {
            case REGISTER:
                boolean successAddUser = this.addUserToList(message);
                helper.write(successAddUser ? "User was registered" : "User already registered");
                break;

            case RECEIVE:
                String response = this.getMessagesOfUser(message);
                helper.write(response.isEmpty() ? "No messages found for that user" : response);
                break;

            case SEND:
                String[] split = message.split("#");
                boolean successAddMessage = this.addMessageToUser(split[0], split[1], split[2]);
                helper.write(successAddMessage ? "Message was added" : "Unable to send message, sender or receiver not registered");
                break;

            case ERROR:
                helper.write("The command you entered could not be processed");
        }
    }

    public static void main(String[] args) throws IOException {
        new Server(1234);
    }
}
