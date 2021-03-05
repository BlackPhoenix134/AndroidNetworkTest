package se.network;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import se.misc.Disposable;

public class AsyncWriter implements Runnable, Disposable {
    private DataOutputStream writer;
    private boolean shouldRun = true;
    //ToDo: more locks for concurrent access
    private Queue<String> packageQueue = new ConcurrentLinkedQueue<>();

    public AsyncWriter(OutputStream outputStream) {
        writer = new DataOutputStream(outputStream);
    }

    public void setShouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }

    @Override
    public void run() {
        try {
            while(shouldRun) {
                if(!packageQueue.isEmpty()) {
                    sendMessage(packageQueue.poll());
                }
            }
        } catch (IOException e) {
            Log.e("Networking", e.getStackTrace().toString());
        }
    }

    public void enqueueMessage(String message) {
        packageQueue.add(message);
    }

    //ToDo: maybe implement simple network library (package management)
    //      probably good for other proj. if not allowed to use libs
    private void sendMessage(String cmd) throws IOException {
        Log.d("Networking", "Sending: " + cmd);
        writer.writeBytes(cmd + "\n");
    }

    @Override
    public void dispose() {
        shouldRun = false;
        if(writer != null) {
            try {
                writer.close();
            } catch (IOException ignored) {
            }
        }
    }
}
