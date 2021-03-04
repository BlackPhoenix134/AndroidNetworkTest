package se.network;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import se.misc.Disposable;

public class AsyncReader implements Runnable, Disposable {
    private BufferedReader reader;
    private boolean shouldRun = true;

    public AsyncReader(InputStream inputStream) {
        reader =  new BufferedReader(new InputStreamReader(inputStream));
    }
    public void setShouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }

    @Override
    public void run() {
        try {
            while(shouldRun) {
                Log.d("Networking", "Fetching data");
                String data = reader.readLine();
                Log.d("Networking", data);
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            Log.e("Networking", e.getStackTrace().toString());
        }
    }

    private String getData() throws IOException {
        String userInput;
        while ((userInput = reader.readLine()) != null) {
            Log.d("Networking", "Fragment: " + userInput);
        }
        return userInput;
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
