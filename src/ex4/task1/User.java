package ex4.task1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class User {


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);


        while (true) {

            String userInput = sc.nextLine();

            try {
                Socket s = new Socket("localhost", 1234);

                InputStream is = s.getInputStream();
                OutputStream os = s.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                PrintWriter writer = new PrintWriter(os);

                writer.println(userInput);
                writer.flush();

                System.out.println(reader.readLine());
                s.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
