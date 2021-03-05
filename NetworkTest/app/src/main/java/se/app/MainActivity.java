package se.app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import se.misc.Action;
import se.network.NetworkClient;
import se.network.SimpleNetworkClient;

public class MainActivity extends AppCompatActivity {
    private MainActivity thisActivity = this;
    EditText inStudNr;
    TextView tvServerResponse;

    NetworkClient client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setElements();
        createListener();
        try {
           // client = new NetworkClient("se2-isys.aau.at", 53212);
            // client = new NetworkClient("localhost", 8080);
             //new Thread(client).start();
        } catch (Exception e) {
             Log.e("App", e.getStackTrace().toString());
             if(client != null)
                 client.close();
        }

    }
    private void setElements() {
        inStudNr=  findViewById(R.id.inStudNr);
        tvServerResponse=  findViewById(R.id.tvServerResponse);
    }
    private void createListener() {
        //ToDo: write dispatcher
        Button button = findViewById(R.id.btnSend);
        button.setOnClickListener(view -> {
             SimpleNetworkClient snc = new SimpleNetworkClient("se2-isys.aau.at", 53212, inStudNr.getText().toString(),
                     message -> thisActivity.runOnUiThread(() -> {
                         tvServerResponse.setText(message);
                     }));
             snc.start();
        });
    }
}