package ex3.task2;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Helper implements Runnable {

    private final Socket socket;

    public Helper(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream is = this.socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            OutputStream os = this.socket.getOutputStream();
            PrintWriter writer = new PrintWriter(os);

            String fileName = reader.readLine().split("/")[1].split("HTTP")[0].trim();
            StringBuilder message = new StringBuilder();

            Files.readAllLines(Paths.get("./src/ex3/task2/" + fileName))
                    .forEach(l -> message.append(l).append("\n"));

            String out = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: "
                    + message.toString().getBytes().length + "\r\n\r\n"
                    + message;

            writer.print(out);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
