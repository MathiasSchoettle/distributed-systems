package ex3.task1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int DEFAULT_PORT = 8808;

    public static void main(String[] args) {
        int port = args.length == 1 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    Socket s = serverSocket.accept();

                    InputStream is = s.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                    OutputStream os = s.getOutputStream();
                    PrintWriter writer = new PrintWriter(os);

                    String msgFromUser = reader.readLine();
                    System.out.println("message received from user = " + msgFromUser);

                    if (msgFromUser.contains("shit"))
                        msgFromUser = msgFromUser.replace("shit", "****");

                    writer.println("*" + msgFromUser + "*");
                    writer.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("done");
    }
}
