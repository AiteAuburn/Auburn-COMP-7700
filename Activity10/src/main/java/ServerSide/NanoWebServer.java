package ServerSide;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class NanoWebServer {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8888);
        System.out.println("WebServer is listening at 8888!");
        while (true) {
            Socket pipe = server.accept();
            PrintWriter out = new PrintWriter(pipe.getOutputStream(), true);
            Scanner in = new Scanner(pipe.getInputStream());
            System.out.println("A client request!!!");
            while (in.hasNext()) {
                System.out.println(in.nextLine());
            }
            String res = "Hello!!!";

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Length: " + res.length());
            out.println();
            out.println(res);
            out.close();
            pipe.close();
        }
    }
}
