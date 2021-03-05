package app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public
class EchoClient extends Thread {
    private int name;
    private Socket socket;

    public EchoClient(int name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    public void run() {
        String msg = "EchoServer: Connecting " + name;
        System.out.println(msg + " connected");
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write((msg + "\r\n").getBytes());
            int c;
            while ((c = in.read()) != -1) {
                out.write((char) c);
                System.out.print((char) c);
            }
            System.out.println("Conneciton " + name + " will be closed");
            socket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}