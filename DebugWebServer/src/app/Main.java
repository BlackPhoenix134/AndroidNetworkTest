package app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        int cnt = 0;
        try {
            ServerSocket echod = new ServerSocket(8080);
            while (true) {
                Socket socket = echod.accept();
                (new EchoClient(++cnt, socket)).start();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }


}
