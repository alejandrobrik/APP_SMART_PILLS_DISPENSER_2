package com.uteq.app_smart_pills_dispenser.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uteq.app_smart_pills_dispenser.MainActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Pill;
import com.uteq.app_smart_pills_dispenser.services.PillService;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PillAddActivity extends AppCompatActivity {
    PillService pillService;
    EditText txtPillDescription;
    EditText txtPillName;
    Button btnPillSave;
    Button btnBack;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pill_add);

        txtPillName = findViewById(R.id.txtPillName);
        txtPillDescription = findViewById(R.id.txtPillDescription);
        btnBack = findViewById(R.id.btnBack);
        btnPillSave = findViewById(R.id.btnPillSave);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnPillSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pill pill = new Pill();
                pill.setName(txtPillName.getText().toString());
                pill.setDescription(txtPillDescription.getText().toString());
                pill.setState((true));
                addPill(pill);
            }
        });
    }
    public void addPill(Pill pill)
    {
        pillService = Apis.getPillService();
        Call<Pill> call = pillService.addPill(pill);

        call.enqueue(new Callback<Pill>() {
            @Override
            public void onResponse(Call<Pill> call, Response<Pill> response) {
                if(response!=null) {
                    Toast.makeText(PillAddActivity.this, "Se registró con éxito",Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<Pill> call, Throwable t) {
                Log.e("Error:",t.getMessage());

            }
        });
    }
}