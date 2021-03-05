package se.network;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import se.misc.Action;
import se.misc.Disposable;

public class AsyncReader implements Runnable, Disposable {
    private BufferedReader reader;
    private boolean shouldRun = true;
    private Action<String> messageReceivedEvent;

    public AsyncReader(InputStream inputStream) {
        reader =  new BufferedReader(new InputStreamReader(inputStream));
    }

    public AsyncReader(InputStream inputStream, Action<String> messageReceivedEvent) {
        this(inputStream);
        this.messageReceivedEvent = messageReceivedEvent;
    }

    public void setShouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }

    @Override
    public void run() {
        try {
            while(shouldRun) {
                String data = reader.readLine();
                if(data != null) {
                    Log.d("Networking/Read", "Read: " + data);
                    if(messageReceivedEvent != null)
                        messageReceivedEvent.invoke(data);
                }
            }
        } catch (IOException e) {
            Log.e("Networking", e.getMessage());
        }
    }

    @Override
    public void dispose() {
        shouldRun = false;
        if(reader != null) {
            try {
                reader.close();
            } catch (IOException ignored) {
            }
        }
    }
}
