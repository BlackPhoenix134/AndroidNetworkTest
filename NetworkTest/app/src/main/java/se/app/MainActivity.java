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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import se.concurrent.Dispatcher;
import se.misc.Action;
import se.misc.Disposable;
import se.network.NetworkClient;
import se.network.SimpleNetworkClient;

public class MainActivity extends AppCompatActivity {
    private TextView tvServerResponse;
    private EditText inStudNr;
    private NetworkClient client = null;
    private Dispatcher dispatcher = new Dispatcher(this);


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
        findViewById(R.id.btnSend).setOnClickListener(view -> {
            SimpleNetworkClient snc = new SimpleNetworkClient("se2-isys.aau.at", 53212, inStudNr.getText().toString());
            snc.setMessageReceivedEvent(message ->
                    dispatcher.onUi(() -> tvServerResponse.setText(message)));
            dispatcher.dispatch(snc);
        });

        findViewById(R.id.btnAction4).setOnClickListener(view -> {
            String nr = inStudNr.getText().toString();
            List<Character> filtered = filterPrimes(nr.toCharArray());
            Collections.sort(filtered);
            tvServerResponse.setText(Arrays.toString(filtered.toArray()));
        });
    }
    private List<Character> filterPrimes(char[] chars) {
        List<Character> retVal = new ArrayList<>();
        for(char c : chars) {
            if(!isPrime(c))
                retVal.add(c);
        }
        return retVal;
    }

    private boolean isPrime(int n) {
        if(n < 2) return false;
        if(n == 2 || n == 3) return true;
        if(n%2 == 0 || n%3 == 0) return false;
        long sqrtN = (long)Math.sqrt(n)+1;
        for(long i = 6L; i <= sqrtN; i += 6) {
            if(n%(i-1) == 0 || n%(i+1) == 0)
                return false;
        }
        return true;
    }
}