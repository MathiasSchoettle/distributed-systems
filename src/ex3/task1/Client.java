package ex3.task1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("please enter message to be sent to server");
        String userInput = sc.nextLine();
        int port = args.length == 1 ? Integer.parseInt(args[0]) : Server.DEFAULT_PORT;

        try (Socket s = new Socket("localhost", port)) {

            InputStream is = s.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            OutputStream os = s.getOutputStream();
            PrintWriter writer = new PrintWriter(os);

            writer.println(userInput);
            writer.flush();

            System.out.println("message from server = " + reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("done");
    }
}
