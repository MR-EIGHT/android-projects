package com.eight.alogger;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "ACTION_RECEIVED_ALOGGER";

    MyBroadcastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "Intent: " + intent.getAction());


        String filename = "logs.txt";

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        String content = intent.getAction().toString() +"\t" + formattedTime + "\n";


        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_APPEND)) {
            fos.write(content.getBytes());
            Log.d("wrote", "Intent: " + intent.getAction() + formattedTime);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

