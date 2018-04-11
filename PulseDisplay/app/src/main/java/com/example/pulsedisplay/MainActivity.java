package com.example.pulsedisplay;

import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.utils.Toaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;

public class MainActivity extends AppCompatActivity {

    private TextView hrDisplay;

    private int hr_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hrDisplay = (TextView) findViewById(R.id.hrDisplay);

        ParticleCloudSDK.init(this);
    }
    @Override
    protected void onResume()
    {
        super.onResume();

        final String email = "sergioeclaure@gmail.com";
        final String password = "StaParticle1";

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            private ParticleDevice mDevice;

            @Override
            public Object callApi(@NonNull ParticleCloud sparkCloud) throws ParticleCloudException, IOException {
                sparkCloud.logIn(email, password);
                sparkCloud.getDevices();
                mDevice = sparkCloud.getDevice("21002c000c47343438323536");

                for (int i = 0; i < 1000000; i++) {

                    try {
                        int intVariable = mDevice.getIntVariable("heartbeat");
                        Log.d("BANANA", "int heartbeat: " + intVariable);

                        hr_num = intVariable;

                        Toaster.l(MainActivity.this, "value of heartbeat = " + hr_num);

//                    hrDisplay.setText("Heart Rate: " + hr_num);

                    } catch (ParticleDevice.VariableDoesNotExistException e) {
                        Toaster.s(MainActivity.this, "Error reading variable");
                    }

                    updateHeartRate();

                }

                return -1;

            }

            @Override
            public void onSuccess(@NonNull Object value) {
                Toaster.l(MainActivity.this, "Logged in");
            }

            @Override
            public void onFailure(@NonNull ParticleCloudException e) {
                Toaster.l(MainActivity.this, e.getBestMessage());
                e.printStackTrace();
                Log.d("info", e.getBestMessage());
            }
        });
    }

    // add random data to graph
    private void updateHeartRate() {
        try {
            // refresh the heartrate value every 6 seconds
            Thread.sleep(6000);

        } catch (InterruptedException e) {
            // manage error ...
        }

//        hrDisplay.setText("Heart Rate: " + hr_num);
    }

}
