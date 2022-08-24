package com.uteq.app_smart_pills_dispenser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uteq.app_smart_pills_dispenser.Activities.CarerAddActivity;

public class MainActivity extends AppCompatActivity {

    Button singup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singup = findViewById(R.id.buttonSingup);

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CarerAddActivity.class);
                startActivity(intent);
            }
        });
    }


}