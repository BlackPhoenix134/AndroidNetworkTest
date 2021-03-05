package se.network;

import android.util.Log;

import androidx.arch.core.util.Function;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;

import se.misc.Action;

public class SimpleNetworkClient extends Thread {
    private String message;
    private String ip;
    private int port;
    private Action<String> messageReceivedEvent;

    public SimpleNetworkClient(String ip, int port, String message) {
        this.ip = ip;
        this.port = port;
        this.message = message;
    }

    public SimpleNetworkClient(String ip, int port, String message, Action<String> onMessageReceived) {
        this.ip = ip;
        this.port = port;
        this.message = message;
        this.messageReceivedEvent = onMessageReceived;
    }

    public void setMessageReceivedEvent(Action<String> messageReceivedEvent) {
        this.messageReceivedEvent = messageReceivedEvent;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(ip, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.writeBytes(message + "\n");
            out.flush();
            if(!socket.isConnected())
                throw new IOException("Socket is not connected");
            String message = in.readLine();
            if(messageReceivedEvent != null)
                messageReceivedEvent.invoke(message);
            socket.close();
        } catch (IOException e) {
            Log.e("Networking", e.getMessage());
        }
    }
}
