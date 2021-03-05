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
import java.util.Arrays;

import se.misc.Action;

public class NetworkClient implements Runnable {
    //ToDo: think about pros/cons of exposing this byte[] or string
    private Socket socket;
    private String ip;
    private int port;
    private Action<String> messageReceivedEvent;

    private AsyncWriter toServer;
    private AsyncReader fromServer;

    public NetworkClient(String ip, int port, Action<String> messageReceivedEvent) {
        this.ip = ip;
        this.port = port;
        this.messageReceivedEvent = messageReceivedEvent;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ip, port);
            fromServer = new AsyncReader(socket.getInputStream(), messageReceivedEvent);
            toServer = new AsyncWriter(socket.getOutputStream());
            new Thread(toServer).start();
            new Thread(fromServer).start();
        } catch (IOException e) {
            Log.e("Networking", e.getMessage());
        }
    }

    public void sendMessage(String message) {
        Log.d("Networking/Client", "enqueue message: " + message);
        toServer.enqueueMessage(message);
    }

    public void close() {
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

