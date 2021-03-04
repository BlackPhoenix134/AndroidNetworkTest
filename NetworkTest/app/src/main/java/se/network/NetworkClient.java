package se.network;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkClient implements Runnable {
    //ToDo: think about pros/cons of exposing this byte[] or string
    private boolean shouldRun = true;
    private Socket socket;
    private String ip;
    private int port;

    private AsyncWriter toServer;
    private AsyncReader fromServer;

    public NetworkClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ip, port);
            fromServer = new AsyncReader(socket.getInputStream());
            toServer = new AsyncWriter(socket.getOutputStream());
            new Thread(toServer).start();
            new Thread(fromServer).start();
            while (shouldRun) //ToDo: fix this mess
            {
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException ex) {

        }
    }

    public void sendMessage(String message) {
        toServer.enqueueMessage(message);
    }

    public void close() {
        shouldRun = false;
        if (fromServer != null)
            fromServer.dispose();
        if (toServer != null)
            toServer.dispose();
        try {
            if (socket != null)
                socket.close();
        } catch (IOException ignored) {
        }
    }
}
