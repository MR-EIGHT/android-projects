package com.eight.objtr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = this.getIntent();
        Person a = intent.getParcelableExtra("Pr");
        Student b = (Student) getIntent().getSerializableExtra("St");

        String data = getIntent().getStringExtra("Em");
        Employee c = new Gson().fromJson(data, Employee.class);


        int code = this.getIntent().getIntExtra("code", 0);

        TextView om = (TextView) findViewById(R.id.textView3);

        TextView name = (TextView) findViewById(R.id.textView4);
        TextView age = (TextView) findViewById(R.id.textView5);
        TextView occupation = (TextView) findViewById(R.id.textView6);

        if (code == 0) {
            name.setText(a.name);
            age.setText(String.valueOf(a.age));
            occupation.setText(a.occupation);
        } else if (code == 1) {
            om.setText("Major");
            name.setText(b.getName());
            age.setText(String.valueOf(b.getAge()));
            occupation.setText(b.getMajor());
        } else {

            om.setText("Section");
            name.setText(c.getName());
            age.setText(String.valueOf(c.getAge()));
            occupation.setText(c.getSection());
        }


    }
}
