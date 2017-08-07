package com.swetabh.serversideapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button buttonStartService, buttonStopService;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStartService = (Button) findViewById(R.id.button_start_service);
        buttonStopService = (Button) findViewById(R.id.button_stop_service);

        buttonStopService.setOnClickListener(this);
        buttonStartService.setOnClickListener(this);
        serviceIntent = new Intent(this, MyService.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_start_service: startService(serviceIntent);
                Toast.makeText(this,"Service Started",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_stop_service: stopService(serviceIntent);
                break;
            default:break;
        }
    }
}
