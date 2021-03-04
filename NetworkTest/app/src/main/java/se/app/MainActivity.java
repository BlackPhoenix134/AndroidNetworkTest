package se.app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import se.network.NetworkClient;

public class MainActivity extends AppCompatActivity {
    NetworkClient client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createListener();

        try {
             client = new NetworkClient("se2-isys.aau.at", 53212);
             new Thread(client).start();
        } catch (Exception e) {
             Log.e("App", e.getStackTrace().toString());
             if(client != null)
                 client.close();
        }


    }

    private void createListener() {
        Button button = (Button) findViewById(R.id.btnSend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.sendMessage("11926135");
            }
        });
    }
}