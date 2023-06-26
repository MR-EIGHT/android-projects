package com.eight.objtr;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button);
        Button btn2 = (Button) findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);
        final EditText name = (EditText) findViewById(R.id.editText);
        final EditText age = (EditText) findViewById(R.id.editText2);
        final EditText occupation = (EditText) findViewById(R.id.editText3);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Person person = new Person(name.getText().toString(), Integer.parseInt(age.getText().toString()), occupation.getText().toString());


                intent.putExtra("Pr", person);
                intent.putExtra("code", 0);

                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);

//                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Student student = new Student(name.getText().toString(), Integer.parseInt(age.getText().toString()), occupation.getText().toString());


                intent.putExtra("St", student);
                intent.putExtra("code", 1);
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);

//                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Employee employee = new Employee(name.getText().toString(), Integer.parseInt(age.getText().toString()), occupation.getText().toString());
                Gson gson = new Gson();
                String json = gson.toJson(employee);

                Intent intent = new Intent();
                intent.putExtra("Em", json);

                intent.putExtra("code", 3);
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);

//                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });


    }
}


class Person implements Parcelable {
    String name = "";
    String occupation = "";
    int age = 0;


    public Person(String name, int age, String occupation) {
        this.name = name;
        this.age = age;
        this.occupation = occupation;
    }

    public Person(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {

        name = in.readString();
        age = in.readInt();
        occupation = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(age);
        parcel.writeString(occupation);
    }
}


class Student implements Serializable {
    private String name = "";
    private int age = 0;
    private String major = "";

    public Student(String name, int age, String major) {
        this.name = name;
        this.age = age;
        this.major = major;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getMajor() {
        return major;
    }
}


class Employee {
    @SerializedName("name")
    private String name;
    @SerializedName("age")
    private int age;
    @SerializedName("section")
    private String section;

    public Employee(String name, int age, String section) {
        this.name = name;
        this.age = age;
        this.section = section;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getSection() {
        return section;
    }
}